package de.juwo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Configuration {

	
	public static enum AnnotatorMode {TRAIN, TEST, CLASSIFY};
	
	
	public static String TRAIN_ARC_PATH 		= 	"/Users/Jules/Projects/HTW/Masterarbeit/Testlauf_Train";
	public static String TEST_ARC_PATH 			= 	"/Users/Jules/Projects/HTW/Masterarbeit/Testlauf_Test";
	public static String PRED_ARC_PATH			= 	"/Users/Jules/Projects/HTW/Masterarbeit/Testlauf_Class";
	public static String LABEL_STORAGE_PATH		=   "/Users/Jules/Projects/HTW/Masterarbeit";
	public static String MODEL_PATH 			= 	"/Users/Jules/Projects/HTW/Masterarbeit/Model";
	public static String OUTPUT_SOLR_XML_WRITER = 	"/Users/Jules/Projects/HTW/Masterarbeit";
	
	public static final String CLASS_1				=	"is.Teach";
	public static final String CLASS_2				=	"rs.no";
	
	public static final Map<String, String> CLASS_NAMES;
	static {
		Map<String,String> tmpMap = new HashMap<String, String>();
		tmpMap.put("CLASS_1", "Science");
		tmpMap.put("CLASS_2", "Politics");
		tmpMap.put("CLASS_3", "Religion");
		tmpMap.put("CLASS_4", "Communication");
		tmpMap.put("CLASS_5", "NoCategory");
		CLASS_NAMES = Collections.unmodifiableMap(tmpMap);
	}
	
	public static List<String> SVM_PARAMETERS = Arrays.asList("-t", "0");
	
	
	public static final String STEMMING_LANGUAGE 	= 	"German";
	public static final Boolean TOKEN_ANNOTATOR		=   true;
	public static final Boolean STEMMING			=	true;
	public static final Boolean SENTENCE_ANNOTATOR	= 	true;
	
	

	
	
	public static final List<String> LATEX_STRINGS = Arrays.asList("Tex", "Latex");


	public static void loadConfigFromFile(String string) {
		
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(string));
			
			System.out.println("Load configuration from file: " + string);
			
			if (prop.getProperty("TRAIN_ARC_PATH") != null)
				TRAIN_ARC_PATH = prop.getProperty("TRAIN_ARC_PATH");
			if (prop.getProperty("TEST_ARC_PATH") != null)
				TEST_ARC_PATH = prop.getProperty("TEST_ARC_PATH");
			if (prop.getProperty("PRED_ARC_PATH") != null)
				PRED_ARC_PATH = prop.getProperty("PRED_ARC_PATH");
			if (prop.getProperty("LABEL_STORAGE_PATH") != null)
				LABEL_STORAGE_PATH = prop.getProperty("LABEL_STORAGE_PATH");
			if (prop.getProperty("MODEL_PATH") != null)
				MODEL_PATH = prop.getProperty("MODEL_PATH");		
			if (prop.getProperty("OUTPUT_SOLR_XML_WRITER") != null)
				OUTPUT_SOLR_XML_WRITER = prop.getProperty("OUTPUT_SOLR_XML_WRITER");	
			if (prop.getProperty("SVM_PARAMETERS") != null) {
				SVM_PARAMETERS = Arrays.asList(prop.getProperty("SVM_PARAMETERS").split(","));
			}

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void showConfig() {
		
		System.out.println("TRAIN_ARC_PATH:" + TRAIN_ARC_PATH);
		System.out.println("TEST_ARC_PATH: "+ TEST_ARC_PATH);
		System.out.println("PRED_ARC_PATH: "+ PRED_ARC_PATH);
		System.out.println("LABEL_STORAGE_PATH: " + LABEL_STORAGE_PATH);
		System.out.println("MODEL_PATH: " + MODEL_PATH);
		System.out.println("OUTPUT_SOLR_XML_WRITER: " + OUTPUT_SOLR_XML_WRITER);
		System.out.println("SVM_PARAMETERS" + SVM_PARAMETERS);
		System.out.println("CLASS 1: " + CLASS_1);
		System.out.println("CLASS 2: " + CLASS_2);
	}
}
