package de.renehelbig.uima.arcreader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.cleartk.util.ViewURIUtil;
import org.uimafit.factory.CollectionReaderFactory;
import org.xml.sax.SAXException;

import de.juwo.uima.cas.DocumentMetadata;
import de.juwo.uima.cas.PDFMetadata;



/**
 * <p>
 * The ArcCollectionReader reads a collection of heritrix archive files (ARC file format) from a configurable
 * input directory initializes new CAS instances with the pdf-documents text.
 * 
 * <p>
 * The input directory is scanned for files ending in '*.arc.gz' during initialization of the collection
 * reader. The reader then iterates over all the heritrix archive files and over the web documents contained
 * in each archive file.
 * 
 * <h2>Input</h2>
 * <p>
 * None
 * 
 * <h2>Output</h2>
 * <p>
 * The reader sets the document text of the default view of the CAS with the web pdf-documents text content. In
 * addition, it sets the following to Feature Structures:
 * 
 * <ul>
 * <item><b>DocumentMetadata</b> - this structure contains features for the document url, a unique id
 * created by computing the md5 hash sum for this url, information about the source arc file name and the
 * mime type of the web document</item> <item><b>SourceDocumentInformation</b> - this structure contains
 * the uri of the web document</item>
 * </ul>
 * 
 * <h2>Parameters</h2>
 * <table>
 * <tr>
 * <td>InputDirectory</td>
 * <td>the path to the input directory</td>
 * </tr>
 * <tr>
 * <td>ProcessedArcsLogfile</td>
 * <td>the path and name of the logfile parameter in the UIMA descriptor used to log the processed arc file
 * status</td>
 * </tr>
 * </table>
 * 
 * @author Julian Wolfert previous versions by Prof. Dr. Herta and Rene Helbig
 * Changes to extract Text and metadata of Documents and to work with clearTK
 * Changes to work with cross validation of clearTK
 */
public class ArcCollectionReader extends CollectionReader_ImplBase {

	private static Logger logger = Logger.getLogger(ArcCollectionReader.class);

	/**
	 * the name of the input directory parameter in the UIMA descriptor
	 */
	public static String PARAM_INPUTDIR = "";
	/**
	 * the path and name of the logfile parameter in the UIMA descriptor used to log the processed arc file
	 * status
	 */
	public static String PARAM_PROCESSEDARCSLOG = PARAM_INPUTDIR.trim()+"/ProcessedArcsLogfile.txt";

	private int mCurrentIndex = 0;
	private static ArrayList<File> mAllArcFiles = new ArrayList<File>();
	// internaly used iterator over records in one arc-file
	private Iterator<ArchiveRecord> arcFileIterator = null;
	private Iterator<ArchiveRecord> hasNextArcFileIterator = null;
	// a counter of all records processed by this reader
	private int recordNumber = 0;
	private File inputDirectory;
	private List<String> alreadyProcessedFiles = new ArrayList<String>();
	// file to store logging information about processed arc files
	File processedArcLogfile;

	// will be set to true in close()
	private boolean isFinished = false;

	/**
	 * file filter for "arc.gz" files
	 * @author 
	 */
	public static class ArcGzFilter implements FilenameFilter {

		private static final String ARC_GZ_FILEENDING = "arc.gz";

		/**
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		public boolean accept(File dir, String name) {
			if (name.endsWith(ARC_GZ_FILEENDING)) {
				return true;
			}
			return false;
		}
	}


	/**
	 * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
	 */
	@Override
	public void initialize() throws ResourceInitializationException {

		//If this is empty we work with all files in the directory
		//Used for training,testing and classification
		//If this not empty, its already set by cross validation
		if (this.mAllArcFiles.isEmpty()) {

			this.mCurrentIndex = 0;
			this.mAllArcFiles.clear();

			// initialize input directory parameter
			this.inputDirectory = new File(((String) PARAM_INPUTDIR).trim());
			// if input directory does not exist or is not a directory, throw
			// exception
			if (!this.inputDirectory.exists()
					|| !this.inputDirectory.isDirectory()) {
				throw new ResourceInitializationException(
						ResourceConfigurationException.DIRECTORY_NOT_FOUND,
						new Object[] { PARAM_INPUTDIR,
								this.getMetaData().getName(),
								this.inputDirectory.getPath() });
			}
			logger.info("Reading heritrix arc-files from input directory: "
					+ this.inputDirectory.getAbsolutePath());

			// initialize the processed Arc-Logfile parameter
			String processedArcLogPath = (String) PARAM_PROCESSEDARCSLOG;
			if (processedArcLogPath != null) {
				this.processedArcLogfile = new File(processedArcLogPath);
				logger.info("logfile for processed arc-files: "
						+ this.processedArcLogfile.getAbsolutePath());
			}

			if (this.processedArcLogfile != null
					&& this.processedArcLogfile.exists()) {
				// try to get a list of all already processed arc-files.
				this.alreadyProcessedFiles = this
						.readLogOfProcessedFiles(this.processedArcLogfile);
			}

			// get a (sorted) list of files (not subdirectories) in the
			// specified directory
			File[] allArcFiles = this.inputDirectory
					.listFiles(new ArcGzFilter());

			System.out.println("CollectionReader: Read collection from: " + this.inputDirectory);

			if (allArcFiles.length == 0) {
				logger.warn("no files with extension "
						+ ArcGzFilter.ARC_GZ_FILEENDING
						+ " found in input directory");
			}

			Arrays.sort(allArcFiles);

			for (File arcfile : allArcFiles) {
				if (!arcfile.isDirectory()) {
					// check that arcfiles name is not on the list of already
					// processed files
					String arcFileName = arcfile.getName();
					if (!this.alreadyProcessedFiles.contains(arcFileName)) {
						this.mAllArcFiles.add(arcfile);
						logger.info("adding arcfile " + arcFileName
								+ " to list of files to process.");
					} else {
						logger.info("skipping arcfile " + arcFileName
								+ " because there was an entry in "
								+ this.processedArcLogfile.getName()
								+ "file was already processed before.");
					}
				}
			}

		}
		// initialize current iterator
		if (this.mAllArcFiles.size() > 0) {
			try {
				this.arcFileIterator = new ArcFileIterator(
						this.mAllArcFiles.get(this.mCurrentIndex));
				this.hasNextArcFileIterator = new ArcFileIterator(
						this.mAllArcFiles.get(this.mCurrentIndex));
			} catch (IOException e) {
				throw new ResourceInitializationException(e);
			}
		}
	}

	/**
	 * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
	 */
	public void getNext(CAS acas) throws IOException, CollectionException {
		getNext(acas, true);
	}

	/**
	 * skips a document in this reader without reading the content into a new cas
	 * @throws IOException
	 * @throws CollectionException
	 */
	public void skipCas() throws IOException, CollectionException {
		this.getNext(null, false);
	}
	/**
	 * Method to iterate through all files contained in ARC-Files
	 * Calls itself till PDF-File or end of file is found
	 */
	private void getNext(CAS acas, boolean fillCas) throws IOException, CollectionException {

		String currentArcfileName = this.mAllArcFiles.get(this.mCurrentIndex).getName();
		ArchiveRecord record = null;

		if (this.arcFileIterator.hasNext()) {
			record = this.arcFileIterator.next();

		}
		if (record != null) {
			ArchiveRecordHeader header = record.getHeader();
			
			// skip header
			record.skip(header.getContentBegin());

			// check mime type -> only interested in pdf
			String mimetype = header.getMimetype();
			if (!mimetype.toLowerCase().startsWith("application/pdf")) {
				String error = "Found non application/PDF document in Arc-File+" + currentArcfileName
						+ ". Mime-Type was: " + mimetype;
				getNext(acas, fillCas);
				return;
			} 
			
			// extract text and metadata if pdf
			if (fillCas && mimetype.toLowerCase().startsWith("application/pdf")) {
				try {

					// dump content of record in outputstream
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					record.dump(outStream);

					String documentText;
					PDDocumentInformation pdfMetaData;
					
					try{
						documentText = extractText(outStream);
						pdfMetaData = extractPDFMetadata(outStream);
					}catch(TikaException tex){
						System.out.println("PDF encrypted or not readable");
						documentText = "";
						pdfMetaData = null;
					}catch(IOException ioex){
						System.out.println("Error on parsing pdf document");
						documentText = "";
						pdfMetaData = null;
					}
					outStream.close();
					
					// only interested in documentext with a length above 0
					if(documentText.length() > 0){
						fillContentOfCAS(acas, currentArcfileName, header, mimetype, documentText, pdfMetaData);
					}else{
						getNext(acas, fillCas);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
			this.recordNumber++;
		}

		// if iterator has no next record, delete it and increase count for Arc-File to process
		if (!this.arcFileIterator.hasNext()) {
			// No more Files in ARC-File
			// log current arcfile name to "processedArcs" file for checkpointing
			this.markArcFileAsProcessed(currentArcfileName);
			this.mCurrentIndex++;
			// check if all ARC-Files were iterated
			if (this.mCurrentIndex < this.mAllArcFiles.size()) {
				this.arcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
				this.hasNextArcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
				getNext(acas, fillCas);
			}

		}
		if (!this.hasNext()) {
			this.close();
			return;
		}

	}
	/**
	 * Method to extract text of pdf-document 
	 * uses Apache Tika PDFParser for text extraction
	 * @param fileContent	ByteArrayOutputStream of pdf-document
	 * @return returns string of document text
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 */
	private static String extractText(ByteArrayOutputStream fileContent) throws IOException, SAXException, TikaException {
		// Conversion of ByteArrayOutputStream to InputStream
		InputStream is = new ByteArrayInputStream(fileContent.toByteArray());
		Parser p = new PDFParser();

		StringWriter writer = new StringWriter();

		//Parse PDF Document with Tika
		p.parse(is, new BodyContentHandler(writer), new Metadata(), new ParseContext());
		is.close();


		return writer.toString();
	}
	/**
	 * Method to extract metadata of pdf-document 
	 * uses Apache Tika PDFParser for metadata extraction
	 * @param fileContent	ByteArrayOutputStream of pdf-document
	 * @return returns metadata of pdf document
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 */
	private static PDDocumentInformation extractPDFMetadata(ByteArrayOutputStream fileContent) throws IOException, SAXException, TikaException {
		// Conversion of ByteArrayOutputStream to InputStream
		InputStream is = new ByteArrayInputStream(fileContent.toByteArray());
		
		//Load PDF Metadata with PDFBOX Library
		PDDocument doc = PDDocument.load(is);
		PDDocumentInformation metadata = doc.getDocumentInformation();

		return metadata;
	}
	
	
	
	/**
	 * Method to fill CAS with content
	 * creates view in CAS to store documenttext
	 * @param acas actual CAS
	 * @param currentArcfileName name of current ARC-File
	 * @param header headerinformation of document from ARC-File  
	 * @param mimetype Mimetype of actual document (should be application/pdf)
	 * @param documentText text of document
	 * @throws CollectionException
	 */
	private void fillContentOfCAS(CAS acas, String currentArcfileName, ArchiveRecordHeader header,
			String mimetype, String documentText, PDDocumentInformation extractedPDFMetadata) throws CollectionException {
		
		try {
			JCas jcas = acas.getJCas();

			//create view and set doc text
			jcas.createView(header.getUrl());
			jcas.setDocumentText(documentText);

			//add pdfMetadata annotation to cas
			if (extractedPDFMetadata != null) {
			
				PDFMetadata pdfMetadata = new PDFMetadata(jcas);
				pdfMetadata.setAuthor(extractedPDFMetadata.getAuthor());
				pdfMetadata.setCreator(extractedPDFMetadata.getCreator());
				pdfMetadata.setTitle(extractedPDFMetadata.getTitle());	
				pdfMetadata.setProducer(extractedPDFMetadata.getProducer());
				jcas.addFsToIndexes(pdfMetadata);
			
			}
			
			//add documentMetadata annotation to cas
			DocumentMetadata metadata = new DocumentMetadata(jcas);
			String docUrl = header.getUrl();
			String id = header.getUrl();
			metadata.setDocumentID(id);
			metadata.setDocumentURL(docUrl);
			metadata.setSource(currentArcfileName);
			metadata.setMimeType(mimetype);
			jcas.addFsToIndexes(metadata);
			
			
			java.net.URI uri;
			try {
				uri = new java.net.URI(header.getUrl());
				ViewURIUtil.setURI(acas, uri);
			} catch (URISyntaxException e) {
				logger.error("Problem setting URI for CAS");
						e.printStackTrace();
			}
			logger.info(currentArcfileName + ":" + this.recordNumber + 
					", document url: " + docUrl);

		} catch (CASException e) {
			throw new CollectionException(e);
		}
	}

	/**
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
	 */
	public void close() {
		this.isFinished = true;
	}

	/**
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
	 */
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(this.mCurrentIndex, this.mAllArcFiles.size(),
				Progress.ENTITIES) };
	}

	/**
	 * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
	 */
	public boolean hasNext() {
		if (this.isFinished) {
			return false;
		}
		boolean result = false;
		// check if we have more arc files in the input directory to process
		if (this.mCurrentIndex < this.mAllArcFiles.size()) {
			// if yes, check if there is any record waiting in the current arc file
			if (this.arcFileIterator != null) {
				while(this.hasNextArcFileIterator.hasNext()){
					ArchiveRecord record = this.hasNextArcFileIterator.next();
					record.getHeader();
					String mimetype = record.getHeader().getMimetype();
					if (mimetype.toLowerCase().startsWith("application/pdf")) {
						//System.out.println("PDF");
						return true;
						//return;
					}
				}
				if (!this.hasNextArcFileIterator.hasNext()) {
					System.out.println("Finished processing current ARC-Archive");
					// log current arcfile name to "processedArcs" file for checkpointing
					String currentArcfileName = this.mAllArcFiles.get(this.mCurrentIndex).getName();
					this.markArcFileAsProcessed(currentArcfileName);
					this.mCurrentIndex++;
					if (this.mCurrentIndex < this.mAllArcFiles.size()) {
						try {
							this.arcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
							this.hasNextArcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
						} catch (IOException e) {
							return false;
						}
						return hasNext();
					}

				}
				return false;
			}
		}
		return result;
	}

	/**
	 * @see org.apache.uima.collection.CollectionReader_ImplBase#reconfigure()
	 */
	@Override
	public void reconfigure() throws ResourceConfigurationException {
		super.reconfigure();
		this.isFinished = false;
	}

	/**
	 * This method adds the given arc file name to a file called "processedArcs" in the arc-files input
	 * folder. If no such file exists yet in the input folder, it is created there
	 * @param arcfileName the name of the processed arc-file
	 */
	private void markArcFileAsProcessed(String arcfileName) {
		// check if the parameter for reader was set
		if (this.processedArcLogfile != null) {
			boolean fileExists = this.processedArcLogfile.exists();

			// create file if it doesn't exist yet
			if (!fileExists) {
				try {
					File parentFile = this.processedArcLogfile.getParentFile();
					if (parentFile != null && (parentFile.isDirectory() || parentFile.mkdirs())) {
						this.processedArcLogfile.createNewFile();
						fileExists = true;
						System.out.println("Creating file: '" + this.processedArcLogfile.getAbsoluteFile()
								+ " in arc-file input directory.");
					} else {
						System.out.println("error creating file '" + this.processedArcLogfile.getAbsoluteFile()
								+ " in arc-file input directory.");
						return;
					}
				} catch (IOException e) {
					System.out.println("error creating file '" + this.processedArcLogfile.getAbsoluteFile()
							+ " in arc-file input directory.");
					return;
				}
			}
			if (fileExists) {
				PrintStream outputFile = null;
				try {
					outputFile = new PrintStream(new FileOutputStream(this.processedArcLogfile, true));
					outputFile.println(arcfileName);
				} catch (FileNotFoundException e) {
					logger.warn("error writing to file file '" + this.processedArcLogfile.getAbsoluteFile()
							+ " in arc-file input directory.", e);
				} finally {
					if (outputFile != null) {
						outputFile.close();
					}
				}
			}
		} else {
			// only go here if this.processedArcLogfile == null || this.processedArcLogfile.equals("")
			logger.warn("Could not write to arcfile-logfile because it wasn't set correctly for this ArcReader.");
		}
	}

	/**
	 * reads the lines in the current file and returns it to be used as list of already processed arcfiles
	 * @param processedFileLog
	 * @return a list of already processed arc files.
	 */
	private List<String> readLogOfProcessedFiles(File processedFileLog) {
		List<String> processedFileList = new ArrayList<String>();
		try {
			BufferedReader logFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(
					processedFileLog)));
			String line = null;
			for (int i = 0; (line = logFileReader.readLine()) != null; i++) {
				processedFileList.add(line);
				logger.debug("added " + line + " to list of already processed arc-files.");
			}
			logFileReader.close();
		} catch (IOException e) {
			logger.warn("Could not read logfile containing processed arc-files. Continuing with empty list."
					+ e.getMessage());
		}
		return processedFileList;
	}
	/**
	 * Method to get ArcCollectionReader
	 * @return ArcCollectionReader via CollectionReaderFactory
	 * @throws ResourceInitializationException
	 */
	public static CollectionReader getCollectionReader() throws ResourceInitializationException {
		return CollectionReaderFactory.createCollectionReader(CollectionReaderFactory.createDescription(
				ArcCollectionReader.class));

	}
	/**
	 * Method to set directory for ARC-Files 
	 * @param directory directory of ARC-Files
	 */
	public static void setARCDirectory(String directory){
		PARAM_INPUTDIR = directory;
		PARAM_PROCESSEDARCSLOG = PARAM_INPUTDIR.trim()+"/ProcessedArcsLogfile.txt";
	}

	/**
	 * Method to manually set files
	 * Used for cross validation
	 * @param files list of files to process
	 */
	public static void setARCFileList(List<File> files) {
		mAllArcFiles = new ArrayList<File>(files);
	}
}
