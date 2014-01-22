package de.juwo.uima.main;
import java.io.File;

import org.apache.uima.collection.CollectionReader;

import de.juwo.uima.core.DocumentClassificationEvaluation;
import de.juwo.util.Configuration;
import de.renehelbig.uima.arcreader.ArcCollectionReader;

/**
 * 
 * Wrapper-Class for run a classification on unlabeled documents
 * @author Julian Wolfert
 * 
 */
public class RunModel {

	/**
	 * Standard main-method to run a new classification given params are ignored
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		//Create a new instance of DocumentClassificaitonEvaluation and set model input path
		DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
				new File(Configuration.MODEL_PATH));


		//Initialization of ArcCollectionReader with path to ARC-Files to classify
		ArcCollectionReader.setARCDirectory(Configuration.PRED_ARC_PATH);
		CollectionReader collectionReader = ArcCollectionReader.getCollectionReader();

		//start of classification pipeline
		evaluation.classify(collectionReader,new File(Configuration.MODEL_PATH));
	}
}
