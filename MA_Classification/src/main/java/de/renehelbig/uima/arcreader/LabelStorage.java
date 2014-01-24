package de.renehelbig.uima.arcreader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.StringTokenizer;

import de.juwo.util.Configuration;


public class LabelStorage implements LabelStorageInterface {
	
	/**
	 * method to store class label coresponding to document url
	 * implements method of interface LabelStorageInterface
	 * @param url url of document
	 * @param classLabel label of documents class
	 */
	public void writeClassLabel(String url, String classLabel) {
		File labelLogForArc = new File(Configuration.TRAIN_ARC_PATH+"/TrainingLabels.txt");
        if (labelLogForArc != null) {
            boolean fileExists = labelLogForArc.exists();

            
            if (!fileExists) {
            	// create file if it doesn't exist yet
                try {
                    File parentFile = labelLogForArc.getParentFile();
                    if (parentFile != null && (parentFile.isDirectory() || parentFile.mkdirs())) {
                        labelLogForArc.createNewFile();
                        fileExists = true;
                    } else {
                        return;
                    }
                } catch (IOException e) {
                    System.out.println("error creating file '" + labelLogForArc.getAbsoluteFile()+ " in arc-file input directory.");
                    return;
                }
            }
            if (fileExists) {
            	// file exists -> write to file
                PrintStream outputFile = null;
                try {
                    outputFile = new PrintStream(new FileOutputStream(labelLogForArc, true));
                    outputFile.println(url+" "+classLabel);
                } catch (FileNotFoundException e) {
                	System.out.println("error writing to file '" + labelLogForArc.getAbsoluteFile()+ " in arc-file input directory.");
                } finally {
                    if (outputFile != null) {
                        outputFile.close();
                    }
                }
            }
        } 
	}
	/**
	 * Method to write the classification results to a file
	 * works similar to writeClassLabel but works with different textfile
	 * @param url url of document
	 * @param classLabel classlabel of document
	 */
	public void writeClassifiedClassLabel(String url, String classLabel) {
		File labelLogForArc = new File(Configuration.PRED_ARC_PATH+"/ClassificationLabels.txt");
        if (labelLogForArc != null) {
            boolean fileExists = labelLogForArc.exists();
            if (!fileExists) {
            	// create file if it doesn't exist yet
                try {
                    File parentFile = labelLogForArc.getParentFile();
                    if (parentFile != null && (parentFile.isDirectory() || parentFile.mkdirs())) {
                        labelLogForArc.createNewFile();
                        fileExists = true;
                    } else {
                        return;
                    }
                } catch (IOException e) {
                    System.out.println("error creating file '" + labelLogForArc.getAbsoluteFile()+ " in arc-file input directory.");
                    return;
                }
            }
            if (fileExists) {
            	// file exists -> write to file
                PrintStream outputFile = null;
                try {
                    outputFile = new PrintStream(new FileOutputStream(labelLogForArc, true));
                    outputFile.println(url+" "+classLabel);
                } catch (FileNotFoundException e) {
                	System.out.println("error writing to file file '" + labelLogForArc.getAbsoluteFile()+ " in arc-file input directory.");
                } finally {
                    if (outputFile != null) {
                        outputFile.close();
                    }
                }
            }
        } 
	}
	/**
	 * see LabelStorageInterface
	 */
	public HashMap<String, String> getAllClassLabels() {
		HashMap<String, String> uriIsClass =  new HashMap<String, String>();
		File dir = new File(Configuration.TRAIN_ARC_PATH);
		//Classlabels stored in different textfiles take all
		File[] fileList = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File d, String name) {
			       return (name.equals("URL_Class_Label_Dokumentenkorpus.txt"));
			    }
			});
		for(File f : fileList){
			System.out.println("Read class labels from file: " + f.getName());
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String row = null;
				//read information of every line
				while((row = in.readLine()) != null){
					StringTokenizer tokens = new StringTokenizer(row);
					if(tokens.countTokens() >= 2){
						String url = tokens.nextToken();
						String classname = tokens.nextToken();
						uriIsClass.put(url.trim(), classname);
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
				e.printStackTrace();
			} catch (IOException e){
				
			}
		}
		return uriIsClass;
	}

}
