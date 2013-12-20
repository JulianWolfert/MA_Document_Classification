package de.juwo.util;

import java.util.Arrays;
import java.util.List;

public class Configuration {

	
	public static enum AnnotatorMode {TRAIN, TEST, CLASSIFY};
	
	
	public static final String TRAIN_ARC_PATH 		= 	"/Users/Jules/Projects/HTW/Masterarbeit/Testlauf_Train";
	public static final String TEST_ARC_PATH 		= 	"/Users/Jules/Projects/HTW/Masterarbeit/Testlauf_Test";
	public static final String PRED_ARC_PATH		= 	"/Users/Jules/Projects/HTW/Masterarbeit/Testlauf_Class";
	public static final String LABEL_STORAGE_PATH	=   "/Users/Jules/Projects/HTW/Masterarbeit";
	public static final String MODEL_PATH 			= 	"target/document_classification/models";
	
	
	public static final String CLASS_1				=	"is.Teach";
	public static final String CLASS_2				=	"rs.no";
	
	public static final List<String> SVM_PARAMETERS = Arrays.asList("-t", "0");
	
	
	public static final String STEMMING_LANGUAGE 	= 	"German";
	public static final Boolean TOKEN_ANNOTATOR		=   true;
	public static final Boolean STEMMING			=	true;
	public static final Boolean SENTENCE_ANNOTATOR	= 	true;
	
	
	public static final String OUTPUT_SOLR_XML_WRITER = "/Users/Jules/Projects/HTW/Masterarbeit";
	
	
	public static final List<String> LATEX_STRINGS = Arrays.asList("Tex", "Latex");
}
