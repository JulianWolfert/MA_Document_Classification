package de.juwo.uima.main;
import java.io.File;
import java.util.Arrays;

import org.apache.uima.collection.CollectionReader;

import de.juwo.uima.arcreader.ArcCollectionReader;
import de.juwo.uima.core.DocumentClassificationEvaluation;
import de.juwo.util.Configuration;


/**
 * 
 * Wrapper-Class for train a classification model
 * @author Julian Wolfert
 * 
 */
public class TrainModel {
	
	
	/**
	 * Standard main-method to train a classification model
	 * given params are ignored
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
  
		//Create a new instance of DocumentClassificaitonEvaluation and set model output path
		DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
    		new File(Configuration.MODEL_PATH),
    		//set parameter for libSVM "-t 0" -> linear svm
    		Configuration.SVM_PARAMETERS);
		
		//Initialization of ArcCollectionReader with path to ARC-Files to classify
		ArcCollectionReader.setARCDirectory(Configuration.TRAIN_ARC_PATH);
		CollectionReader collectionReader = ArcCollectionReader.getCollectionReader();
	  
	    //start of trainingpipeline and training of svm
	    evaluation.train(collectionReader, new File(Configuration.MODEL_PATH));
	}
}
