package de.juwo.uima.casconsumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
 

import org.cleartk.util.ViewURIUtil;
import org.uimafit.component.JCasConsumer_ImplBase;
import org.uimafit.util.JCasUtil;

import de.juwo.uima.cas.UsenetDocument;
import de.juwo.util.Configuration;

/**
 * 
 * Write the classification result to XML file (Apache Solr format)
 * Changes by Julian Wolfert previous version by Prof. Dr. Christian Herta (HTW-Berlin)
 * @author Julian Wolfert
 * 
 */
public class SolrXMLFileWriter extends JCasConsumer_ImplBase {
	/**
	 * Name of configuration parameter that must be set to the path of a
	 * directory into which the output files will be written.
	 */
	public static final String PARAM_OUTPUTDIR = "OutputDirectory";

	private File mOutputDir = new File (Configuration.OUTPUT_SOLR_XML_WRITER);

	private int mDocNum;
	private int nbFiles;
	
	private XMLStreamWriter out = null;
	private String rawContent = "rawContent";
	private String detagContent = "detagContent";

	private static Logger logger = Logger.getLogger(SolrXMLFileWriter.class);

	@Override
	public void destroy(){
		closeXMLFile();
	}
	
	private void closeXMLFile() {	
		if(out != null){
			try {
				out.writeEndElement();
				out.writeEndDocument();
				out.close();
			}  
			catch (XMLStreamException e) {
				logger.error("Error while closing xmlStream : " + e.getStackTrace().toString());
				e.printStackTrace();
			}
		}
	}
	
	private void openXMLFile() throws IOException, XMLStreamException, FactoryConfigurationError {
			
			String docName = "Class_1_Documents_Solr.xml";
			logger.debug("Create XMLFile " + docName + " in Dir " + mOutputDir);
			File file = new File(mOutputDir, docName);
			
			OutputStream outputStream = new FileOutputStream(file);
			out = XMLOutputFactory.newInstance().createXMLStreamWriter(
			                new OutputStreamWriter(outputStream, "utf-8"));
			
			out.writeStartDocument();
			out.writeStartElement("add");
			nbFiles++;
	}
	
	
	public void initialize() throws ResourceInitializationException {
		mDocNum = 0;
		nbFiles = 0;
		//mOutputDir = new File((String) getConfigParameterValue(PARAM_OUTPUTDIR));
		if (!mOutputDir.exists()) {
			mOutputDir.mkdirs();
		}
	}

	/**
	 * Processes the CAS which was populated by the TextAnalysisEngines. <br>
	 * Writes out only documents which are classified with Class_1.
	 * 
	 * @param aCAS
	 *            a CAS which has been populated by the TAEs
	 * 
	 * @throws ResourceProcessException
	 *             if there is an error in processing the Resource
	 * 
	 */
	public void process(JCas aCAS) throws  AnalysisEngineProcessException {
	
		mDocNum++;
		UsenetDocument document = JCasUtil.selectSingle(aCAS, UsenetDocument.class);
		String documentURL = ViewURIUtil.getURI(aCAS).toString();		
		String documentText = aCAS.getDocumentText();
		String documentCategory = document.getCategory();
			
		//Write out only Class_1 documents
		if (documentCategory.equals(Configuration.CLASS_1)) {
			
			try {
				if(out == null) {
					openXMLFile();
				}
				
				out.writeStartElement("doc");
			
				writeField("url", documentURL);
				writeField("content", documentText);

				out.writeEndElement();
			} catch (Exception e) {
				logger.error("Error while writing to xml file: " + e.getStackTrace().toString()); 
			} 
		}
	}
	
	private void writeField(String fieldName, String content) throws XMLStreamException{
		 out.writeStartElement("field");
	     out.writeAttribute("name", fieldName);
	     out.writeCData(content);
	     out.writeEndElement();
		
	}

		
}