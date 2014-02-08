package de.juwo.util;
import com.google.common.base.CharMatcher;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

import javax.swing.*;

import org.apache.uima.collection.CollectionException;
import org.apache.uima.resource.ResourceInitializationException;
import org.archive.io.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import de.juwo.uima.arcreader.ArcFileIterator;
import de.juwo.uima.arcreader.LabelStorage;
import de.juwo.util.Configuration;

import org.archive.io.ArchiveRecord;


/**
 * Program to view PDF content in ARC-Files and to label the shown documents
 * 
 * Program is needed to generate information for training of an SVM-Classificator 
 * @author renehelbig minor changes by Julian Wolfert
 * 
 * To work with multiple classes
 *
 */
public class PDFViewer extends JPanel {
	//Strings for arc-directory and processedArcsLog
	public static final String PARAM_INPUTDIR = Configuration.TRAIN_ARC_PATH;
	public static final String PARAM_PROCESSEDARCSLOG = Configuration.TRAIN_ARC_PATH+"/ProcessedArcsLogfile.txt";

	private static final long serialVersionUID = 1L;
	private static enum Navigation {GO_FIRST_PAGE, FORWARD, BACKWARD, GO_LAST_PAGE, GO_N_PAGE, GO_NEXT_FILE, GO_NEXT_FILE_THIS_ONE_IS_TEACHINGSTUFF}

	private static final CharMatcher POSITIVE_DIGITAL = CharMatcher.anyOf("0123456789");
	private static final String GO_PAGE_TEMPLATE = "%s of %s";
	private static final int FIRST_PAGE = 1;
	private int currentPage = FIRST_PAGE;
	private JButton btnFirstPage;
	private JButton btnPreviousPage;
	private JTextField txtGoPage;
	private JButton btnNextPage;
	private JButton btnLastPage;

	private JButton btnNextFile;
	private JButton btnIsTeachStuffFile;

	private PagePanel pagePanel;
	private static PDFFile pdfFile;
	private static PDFViewer pdfViewer;

	private int mCurrentIndex = 0;
	ArrayList<File> mAllArcFiles = new ArrayList<File>();
	// iterator over records in one arc-file
	private Iterator<ArchiveRecord> arcFileIterator = null;
	private String actualURL;
	private File inputDirectory;
	private List<String> alreadyProcessedFiles = new ArrayList<String>();
	// file to store logging information about processed arc files
	File processedArcLogfile;

	private LabelStorage storage = new LabelStorage(); 
	/**
	 * Constructor
	 */
	public PDFViewer() {
		initial();
	}
	/**
	 * Method to initialize GUI with elements and load first document
	 */
	/**
	 * 
	 */
	private void initial() {
		setLayout(new BorderLayout(0, 0));
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(topPanel, BorderLayout.NORTH);
		btnFirstPage = createButton("|<<");
		topPanel.add(btnFirstPage);
		btnPreviousPage = createButton("<<");
		topPanel.add(btnPreviousPage);
		txtGoPage = new JTextField(10);
		txtGoPage.setHorizontalAlignment(JTextField.CENTER);
		topPanel.add(txtGoPage);
		btnNextPage = createButton(">>");
		topPanel.add(btnNextPage);
		btnLastPage = createButton(">>|");
		topPanel.add(btnLastPage);
		
		
		for (int i=1; i <= Configuration.CLASS_NAMES.size(); i++) {
		
			JButton b = createButton(Configuration.CLASS_NAMES.get("CLASS_"+i));
			b.setPreferredSize(new Dimension(100, 20));
			b.addActionListener(new PageNavigationListener(Configuration.CLASS_NAMES.get("CLASS_"+i)));	
			topPanel.add(b);
			
//		btnNextFile = createButton("Naechste Datei");
//		btnNextFile.setPreferredSize(new Dimension(100, 20));
//		topPanel.add(btnNextFile);
//		btnIsTeachStuffFile = createButton("Datei ist Lehrstoff");
//		btnIsTeachStuffFile.setPreferredSize(new Dimension(120, 20));
//		topPanel.add(btnIsTeachStuffFile);
		
		}
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		JPanel viewPanel = new JPanel(new BorderLayout(0, 0));
		scrollPane.setViewportView(viewPanel);

		pagePanel = new PagePanel();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pagePanel.setPreferredSize(screenSize);
		viewPanel.add(pagePanel, BorderLayout.CENTER);

		disableAllNavigationButton();

		btnFirstPage.addActionListener(new PageNavigationListener(Navigation.GO_FIRST_PAGE));
		btnPreviousPage.addActionListener(new PageNavigationListener(Navigation.BACKWARD));
		btnNextPage.addActionListener(new PageNavigationListener(Navigation.FORWARD));
		btnLastPage.addActionListener(new PageNavigationListener(Navigation.GO_LAST_PAGE));
		txtGoPage.addActionListener(new PageNavigationListener(Navigation.GO_N_PAGE));
		//btnNextFile.addActionListener(new PageNavigationListener(Navigation.GO_NEXT_FILE));
		//btnIsTeachStuffFile.addActionListener(new PageNavigationListener(Navigation.GO_NEXT_FILE_THIS_ONE_IS_TEACHINGSTUFF));
	}
	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(55, 20));

		return button;
	}

	private void disableAllNavigationButton() {
		btnFirstPage.setEnabled(false);
		btnPreviousPage.setEnabled(false);
		btnNextPage.setEnabled(false);
		btnLastPage.setEnabled(false);
	}

	private boolean isMoreThanOnePage() {
		return pdfFile.getNumPages() > 1;
	}

	private class PageNavigationListener implements ActionListener {
		private final Navigation navigation;
		private final String s;

		private PageNavigationListener(Navigation navigation) {
			this.navigation = navigation;
			this.s=null;
		}
		
		private PageNavigationListener(String s) {
			this.navigation = null;
			this.s = s;
		}

		public void actionPerformed(ActionEvent e) {
			if (pdfFile == null) {
				return;
			}
			if(navigation == null){
				labelStuff(this.s);
			}
			int numPages = pdfFile.getNumPages();
			if (numPages <= 1) {
				disableAllNavigationButton();
			} else {
				if (navigation == Navigation.FORWARD && hasNextPage(numPages)) {
					goPage(currentPage, numPages);
				}

				if (navigation == Navigation.GO_LAST_PAGE) {
					goPage(numPages, numPages);
				}

				if (navigation == Navigation.BACKWARD && hasPreviousPage()) {
					goPage(currentPage, numPages);
				}

				if (navigation == Navigation.GO_FIRST_PAGE) {
					goPage(FIRST_PAGE, numPages);
				}
				if (navigation == Navigation.GO_N_PAGE) {
					String text = txtGoPage.getText();
					boolean isValid = false;
					if (!isNullOrEmpty(text)) {
						boolean isNumber = POSITIVE_DIGITAL.matchesAllOf(text);
						if (isNumber) {
							int pageNumber = Integer.valueOf(text);
							if (pageNumber >= 1 && pageNumber <= numPages) {
								goPage(Integer.valueOf(text), numPages);
								isValid = true;
							}
						}
					}

					if (!isValid) {
						JOptionPane.showMessageDialog(PDFViewer.this, format("Invalid page number '%s' in this document", text));
						txtGoPage.setText(format(GO_PAGE_TEMPLATE, currentPage, numPages));
					}
				}
			}
		}
		/**
		 * Method to label shown document as class teach or class noteach
		 * @param isTeachStuff
		 */
		private void labelAsTeachStuff(boolean isTeachStuff){
			//
			if(isTeachStuff){
				labelDocumentInArcFile(actualURL, Configuration.CLASS_1, pdfViewer.mAllArcFiles.get(mCurrentIndex).getName());
			}else{
				labelDocumentInArcFile(actualURL, Configuration.CLASS_2, pdfViewer.mAllArcFiles.get(mCurrentIndex).getName());
			}
			goFile();
		}
		
		/**
		 * Method to label shown document as class teach or class noteach
		 * @param isTeachStuff
		 */
		private void labelStuff(String class_name){
			//
			labelDocumentInArcFile(actualURL, class_name, pdfViewer.mAllArcFiles.get(mCurrentIndex).getName());
			goFile();
		}
		
		/**
		 * Method to show pdf
		 * whenever called next pdf-document in ARC-File is shown
		 */
		private void goFile(){
			currentPage = 0;
			ByteArrayOutputStream bas;
			try {
				bas = pdfViewer.getNext();
				try{
				if(bas.size() > 0){
					pdfFile = new PDFFile(ByteBuffer.wrap(bas.toByteArray()));
				}
				PDFPage page = pdfFile.getPage(0);

				pagePanel.showPage(page);
				int numPages = pdfFile.getNumPages();
				goPage(1, numPages);
				} catch (Exception e) {
					bas.close();
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

		}
		/**
		 * Method to navigate within PDF-Documents
		 * @param pageNumber
		 * @param numPages
		 */
		private void goPage(int pageNumber, int numPages) {
			currentPage = pageNumber;
			PDFPage page = pdfFile.getPage(currentPage);
			pagePanel.showPage(page);
			boolean notFirstPage = isNotFirstPage();
			btnFirstPage.setEnabled(notFirstPage);
			btnPreviousPage.setEnabled(notFirstPage);
			txtGoPage.setText(format(GO_PAGE_TEMPLATE, currentPage, numPages));
			boolean notLastPage = isNotLastPage(numPages);
			btnNextPage.setEnabled(notLastPage);
			btnLastPage.setEnabled(notLastPage);
		}

		private boolean hasNextPage(int numPages) {
			return (++currentPage) <= numPages;
		}

		private boolean hasPreviousPage() {
			return (--currentPage) >= FIRST_PAGE;
		}

		private boolean isNotLastPage(int numPages) {
			return currentPage != numPages;
		}

		private boolean isNotFirstPage() {
			return currentPage != FIRST_PAGE;
		}
	}

	public PagePanel getPagePanel() {
		return pagePanel;
	}

	public void setPDFFile(PDFFile pdfFile) {
		PDFViewer.pdfFile = pdfFile;
		currentPage = FIRST_PAGE;
		disableAllNavigationButton();
		txtGoPage.setText(format(GO_PAGE_TEMPLATE, FIRST_PAGE, pdfFile.getNumPages()));
		boolean moreThanOnePage = isMoreThanOnePage(/*pdfFile*/);
		btnNextPage.setEnabled(moreThanOnePage);
		btnLastPage.setEnabled(moreThanOnePage);
	}
	/**
	 * Substitutes each {@code %s} in {@code template} with an argument. These
	 * are matched by position - the first {@code %s} gets {@code args[0]}, etc.
	 * If there are more arguments than placeholders, the unmatched arguments will
	 * be appended to the end of the formatted message in square braces.
	 *
	 * @param template a non-null string containing 0 or more {@code %s}
	 *     placeholders.
	 * @param args the arguments to be substituted into the message
	 *     template. Arguments are converted to strings using
	 *     {@link String#valueOf(Object)}. Arguments can be null.
	 */
	public static String format(String template, Object... args) {
		template = String.valueOf(template); // null -> "null"
		// start substituting the arguments into the '%s' placeholders
		StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
		int templateStart = 0;
		int i = 0;
		while (i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(template.substring(templateStart, placeholderStart));
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template.substring(templateStart));

		// if we run out of placeholders, append the extra args in square braces
		if (i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while (i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append(']');
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		long heapSize = Runtime.getRuntime().totalMemory();
		System.out.println("Heap Size = " + heapSize);

		JFrame frame = new JFrame("PDF Labeler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		try {
			pdfViewer = new PDFViewer();
			pdfViewer.initialize();
			ByteArrayOutputStream bas = pdfViewer.getNext();

			pdfFile = new PDFFile(ByteBuffer.wrap(bas.toByteArray()));

			pdfViewer.setPDFFile(pdfFile);
			frame.add(pdfViewer);
			frame.pack();
			frame.setVisible(true);

			PDFPage page = pdfFile.getPage(0);
			pdfViewer.getPagePanel().showPage(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * file filter for "arc.gz" files
	 * @author 
	 */
	public static class ArcGzFilter implements FilenameFilter {

		private static final String ARC_GZ_FILEENDING = "arc.gz";

		/**
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		@Override
		public boolean accept(File dir, String name) {
			if (name.endsWith(ARC_GZ_FILEENDING)) {
				return true;
			}
			return false;
		}
	}
	/**
	 * Method to initialize the ARC-File-Iterator
	 */
	public void initialize() throws ResourceInitializationException {
		this.mCurrentIndex = 0;
		this.mAllArcFiles.clear();


		this.inputDirectory = new File(((String) PARAM_INPUTDIR).trim());

		if (!this.inputDirectory.exists() || !this.inputDirectory.isDirectory()) {
			System.out.println("Problem mit Ordner der ARC-Files");
		}
		System.out.println("Reading heritrix arc-files from input directory: "+ this.inputDirectory.getAbsolutePath());

		// initialize the processed Arc-Logfile parameter
		String processedArcLogPath = (String) PARAM_PROCESSEDARCSLOG;
		if (processedArcLogPath != null) {
			this.processedArcLogfile = new File(processedArcLogPath);
			System.out.println("logfile for processed arc-files: " + this.processedArcLogfile.getAbsolutePath());
		}

		if (this.processedArcLogfile != null && this.processedArcLogfile.exists()) {
			// try to get a list of all already processed arc-files.
			this.alreadyProcessedFiles = this.readLogOfProcessedFiles(this.processedArcLogfile);
		}

		// get a (sorted) list of files (not subdirectories) in the specified directory
		File[] allArcFiles = this.inputDirectory.listFiles(new ArcGzFilter());

		if (allArcFiles.length == 0) {
			System.out.println("no files with extension " + ArcGzFilter.ARC_GZ_FILEENDING+ " found in input directory");
		}

		Arrays.sort(allArcFiles);

		for (File arcfile : allArcFiles) {
			if (!arcfile.isDirectory()) {
				// check that arcfiles name is not on the list of already processed files
				String arcFileName = arcfile.getName();
				if (!this.alreadyProcessedFiles.contains(arcFileName)) {
					if (!arcfile.getName().startsWith("._")) {
						this.mAllArcFiles.add(arcfile);
						System.out.println("adding arcfile " + arcFileName + " to list of files to process.");
					}
				} else {
					System.out.println("skipping arcfile " + arcFileName + " because there was an entry in "+ this.processedArcLogfile.getName() + "file was already processed before.");
				}
			}
		}

		if (this.mAllArcFiles.size() > 0) {
			try {
				this.arcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
			} catch (IOException e) {
				throw new ResourceInitializationException(e);
			}
		}
	}
	/**
	 * reads the lines in the current file and returns it to be used as list of already processed arcfiles
	 * @param processedFileLog
	 * @return a list of already processed arc files.
	 */
	public List<String> readLogOfProcessedFiles(File processedFileLog) {
		List<String> processedFileList = new ArrayList<String>();
		try {
			BufferedReader logFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(
					processedFileLog)));
			String line = null;
			while((line = logFileReader.readLine()) != null) {
				processedFileList.add(line);
				System.out.println("added " + line + " to list of already processed arc-files.");
			}
			logFileReader.close();
		} catch (IOException e) {
			System.out.println("Could not read logfile containing processed arc-files. Continuing with empty list."+ e.getMessage());
		}
		return processedFileList;
	}
	private ArchiveRecord record = null;

	/**
	 * Method to extract pdf records of ARC-Files iterates recursively through all arc files 
	 * till pdf found or end reached 
	 * @return returns ByteArrayOutputStream of PDF-Document
	 * @throws IOException
	 * @throws CollectionException
	 */
	public ByteArrayOutputStream getNext() throws IOException, CollectionException {
		//runs through all arc files

		while(this.mCurrentIndex < this.mAllArcFiles.size()){
			String currentArcfileName = this.mAllArcFiles.get(this.mCurrentIndex).getName();

			if (this.arcFileIterator.hasNext()) {
				record = this.arcFileIterator.next();
			}else{
				record = null;
			}

			// runs through all records in arc file
			while (record != null ) {
				ArchiveRecordHeader header = record.getHeader();
				String mimetype = header.getMimetype();
				// skip header
				record.skip(header.getContentBegin());

				if (!mimetype.toLowerCase().startsWith("application/pdf")) {
					String error = "Found non application/pdf document in Arc-File+" + currentArcfileName
							+ ". Mime-Type was: " + mimetype +record.getHeader().getUrl();

					System.out.println(error);	               
				} else{
					System.out.println(record.getHeader().getUrl());
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					record.dump(outStream);
					outStream.close();
					this.actualURL = record.getHeader().getUrl();
					System.out.println("Aktuell:"+this.actualURL);
					return outStream;	
				}

				if (this.arcFileIterator.hasNext()) {
					record = this.arcFileIterator.next();
				}else{
					break;
				}

			}

			// if iterator has no next record, delete it and increase count for Arc-File to process
			if (!this.arcFileIterator.hasNext()) {
				// log current arcfile name to "processedArcs" file 
				this.markArcFileAsProcessed(currentArcfileName);
				this.mCurrentIndex++;
				if (this.mCurrentIndex < this.mAllArcFiles.size()) {
					this.arcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
				}
			}
		}
		return null;
	}
	/**
	 * Method to write result of manual classification
	 * @param pdfURL
	 * @param label
	 * @param arcfileName
	 */
	void labelDocumentInArcFile(String pdfURL, String label, String arcfileName) {
		this.storage.writeClassLabel(pdfURL, label);

	}

	/**
	 * This method adds the given arc file name to a file called "processedArcs" in the arc-files input
	 * folder. If no such file exists yet in the input folder, it is created there
	 * @param arcfileName the name of the processed arc-file
	 */
	void markArcFileAsProcessed(String arcfileName) {
		if (this.processedArcLogfile != null) {
			boolean fileExists = this.processedArcLogfile.exists();

			// create file if it doesn't exist yet
			if (!fileExists) {
				try {
					File parentFile = this.processedArcLogfile.getParentFile();
					if (parentFile != null && (parentFile.isDirectory() || parentFile.mkdirs())) {
						this.processedArcLogfile.createNewFile();
						fileExists = true;
						System.out.println("creating file '" + this.processedArcLogfile.getAbsoluteFile()
								+ " in arc-file input directory.");
					} else {
						System.out.println("error creating file '" + this.processedArcLogfile.getAbsoluteFile()
								+ " in arc-file input directory.");
						return;
					}
				} catch (IOException e) {
					System.out.println("error creating file '" + this.processedArcLogfile.getAbsoluteFile()+ " in arc-file input directory.");
					return;
				}
			}
			if (fileExists) {
				PrintStream outputFile = null;
				try {
					outputFile = new PrintStream(new FileOutputStream(this.processedArcLogfile, true));
					outputFile.println(arcfileName);
				} catch (FileNotFoundException e) {
					System.out.println("error writing to file file '" + this.processedArcLogfile.getAbsoluteFile()+ " in arc-file input directory.");
				} finally {
					if (outputFile != null) {
						outputFile.close();
					}
				}
			}
		} else {
			// only go here if this.processedArcLogfile == null || this.processedArcLogfile.equals("")
			System.out.println("Could not write to arcfile-logfile because it wasn't set correctly for this ArcReader.");
		}
	}

}

