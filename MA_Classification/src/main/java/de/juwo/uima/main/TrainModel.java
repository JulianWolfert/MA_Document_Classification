package de.juwo.uima.main;
import java.io.File;
import java.util.Arrays;

import org.apache.uima.collection.CollectionReader;

import de.juwo.uima.core.DocumentClassificationEvaluation;
import de.juwo.util.Configuration;
import de.renehelbig.uima.arcreader.ArcCollectionReader;


/**
 * 
 * Wrapper-Class for trainingpipeline
 * Main-Method of class starts trainingpipeline
 * @author Rene Helbig
 * 
 */
public class TrainModel {
	
	
	/**
	 * Standard main-method to start trainingpipeline
	 * given params are ignored
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
  
		DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
    		new File(Configuration.MODEL_PATH),
    		//params for libSVM "-t 0" -> linear svm
    		Configuration.SVM_PARAMETERS);
		
	    /*
	     * Initialization of ArcCollectionReader with path to ARC-Files
	     */
		ArcCollectionReader.setARCDirectory(Configuration.TRAIN_ARC_PATH);
		CollectionReader collectionReader = ArcCollectionReader.getCollectionReader();
	    /*
	     * start of trainingpipeline and training of svm
	     */
	    evaluation.train(collectionReader, new File(Configuration.MODEL_PATH));
	}
}
