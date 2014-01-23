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

import com.google.common.base.Function;

/**
 * 
 * Wrapper-Class for cross validation of a classifier
 * @author Julian Wolfert
 * 
 */
public class CrossValidation {

	/**
	 * Standard main-method to start cross validation
	 * given params are ignored
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	
		
		//Create a new instance of DocumentClassificaitonEvaluation and set model output path
	    DocumentClassificationEvaluation evaluation = new DocumentClassificationEvaluation(
	    		new File(Configuration.MODEL_PATH));
	   
	    
		//Get all arc.gz-Files from train directory
	    List<File> trainFiles = getFilesFromDirectory(new File(Configuration.TRAIN_ARC_PATH));
	    
	    //Start the cross validation with loaded files
		List<AnnotationStatistics<String>> foldStats = evaluation.crossValidation(trainFiles, Configuration.CROSS_VALIDATION_FOLDS);
		
		//Calculate and print out results
	    AnnotationStatistics<String> crossValidationStats = AnnotationStatistics.addAll(foldStats);
	    System.err.println("Cross Validation Results:");
	    System.err.print(crossValidationStats);
	    System.err.println();
	    System.err.println(crossValidationStats.confusions());
	    System.err.println();
	    
	}
	
	/**
	 * Put all arc.gz-Files in a directoy in a list of files
	 * @param directory
	 * @throws Exception
	 */
	public static List<File> getFilesFromDirectory(File directory) {
		IOFileFilter extensionFilter = FileFilterUtils
				.suffixFileFilter("arc.gz");
		IOFileFilter dirFilter = FileFilterUtils.makeSVNAware(FileFilterUtils
				.andFileFilter(FileFilterUtils.directoryFileFilter(),
						HiddenFileFilter.VISIBLE));
		return new ArrayList<File>(FileUtils.listFiles(directory,
				extensionFilter, dirFilter));
	}
}


