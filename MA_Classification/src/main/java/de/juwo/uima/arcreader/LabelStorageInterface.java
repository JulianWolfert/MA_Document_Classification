package de.juwo.uima.arcreader;
import java.util.HashMap;

/**
 * Interface for storage of document class labels
 * Copyright by rene helbig
 * @author rene helbig
 *
 */
public interface LabelStorageInterface {
	/**
	 * method to store class label coresponding to document url
	 * @param url url of document
	 * @param classLabel label of documents class
	 */
	public void writeClassLabel(String url, String classLabel);
	/**
	 * Method to get all classes of labeled documents
	 * @return a hashmap of class and document <uri, classname>
	 */
	public HashMap<String, String> getAllClassLabels();
}
