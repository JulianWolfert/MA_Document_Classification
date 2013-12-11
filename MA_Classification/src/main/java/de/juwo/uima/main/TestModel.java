package de.juwo.uima.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.uima.collection.CollectionReader;
import org.cleartk.eval.AnnotationStatistics;

import de.juwo.uima.core.DocumentClassificationEvaluation;
import de.juwo.util.Configuration;
import de.renehelbig.uima.arcreader.ArcCollectionReader;



public class TestModel {

	
	public static void main(String[] args) throws Exception {
	
	    DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
	    		new File(Configuration.MODEL_PATH));
	   
	    /*
	     * Initialization of ArcCollectionReader with path to ARC-Files
	     */
		ArcCollectionReader.setARCDirectory(Configuration.TEST_ARC_PATH);
		CollectionReader collectionReader = ArcCollectionReader.getCollectionReader();
	    
	     /*
		  * start of trainingpipeline and training of svm
		  */
		 
		 
		    // Run Holdout Set
		    AnnotationStatistics<String> holdoutStats = evaluation.test(collectionReader, new File(Configuration.MODEL_PATH));
		    System.err.println("Holdout Set Results:");
		    System.err.print(holdoutStats);
		    System.err.println();
		    System.err.println(holdoutStats.confusions());
	}
}


