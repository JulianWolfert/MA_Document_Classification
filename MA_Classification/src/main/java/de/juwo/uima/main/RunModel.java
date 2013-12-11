package de.juwo.uima.main;
import java.io.File;

import org.apache.uima.collection.CollectionReader;

import de.juwo.uima.core.DocumentClassificationEvaluation;
import de.juwo.util.Configuration;
import de.renehelbig.uima.arcreader.ArcCollectionReader;

/**
 * 
 * Wrapper-Class for classificationpipeline
 * Main-Method of class starts classificationpipeline
 * @author Rene Helbig
 * 
 */
public class RunModel {


	/**
	 * Standard main-method to start classificationpipeline
	 * given params are ignored
	 * @param args
	 * @throws Exception
	 */
  public static void main(String[] args) throws Exception {
	  
    DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
    		new File(Configuration.MODEL_PATH));
   
    /*
     * Initialization of ArcCollectionReader with path to ARC-Files
     */
	ArcCollectionReader.setARCDirectory(Configuration.PRED_ARC_PATH);
	CollectionReader collectionReader = ArcCollectionReader.getCollectionReader();
    
     /*
	  * start of trainingpipeline and training of svm
	  */
	 evaluation.classify(collectionReader, new File(Configuration.MODEL_PATH));
  }
}
