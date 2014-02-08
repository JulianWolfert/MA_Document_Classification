package de.juwo.uima.main;

import java.io.File;

import org.apache.uima.collection.CollectionReader;
import org.cleartk.eval.AnnotationStatistics;

import de.juwo.uima.arcreader.ArcCollectionReader;
import de.juwo.uima.core.DocumentClassificationEvaluation;
import de.juwo.util.Configuration;


/**
 * 
 * Wrapper-Class for test a classification model
 * @author Julian Wolfert
 * 
 */
public class TestModel {

	/**
	 * Standard main-method to start the test of a model
	 * given params are ignored
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		//Create a new instance of DocumentClassificaitonEvaluation and set model input path
		DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
				new File(Configuration.MODEL_PATH));
		
		//Initialization of ArcCollectionReader with path to ARC-Files for testing
		ArcCollectionReader.setARCDirectory(Configuration.TEST_ARC_PATH);
		CollectionReader collectionReader = ArcCollectionReader
				.getCollectionReader();

		// Run test and print Holdout Set
		AnnotationStatistics<String> holdoutStats = evaluation.test(collectionReader, new File(Configuration.MODEL_PATH));
		System.err.println("Holdout Set Results:");
		System.err.print(holdoutStats);
		System.err.println();
		System.err.println(holdoutStats.confusions());
	}
}


