package de.juwo.uima.main;

import java.net.URL;
import java.net.URLClassLoader;

import de.juwo.util.Configuration;


/**
 * 
 * Suite for command line usage of document classification
 * Offers different modes to run
 * @author Julian Wolfert
 * 
 */
public class Suite {

	public static void main(String[] args) throws Exception {
	    
		if (args.length != 0) {
			Configuration.loadConfigFromFile(args[0]);
		}	
		
		System.out.println("Heap Size:" + Runtime.getRuntime().maxMemory() / 1000000);
		
		// Local variable
	    int swValue = 0;

	    while (swValue != 7) {
	    
	    // Display menu graphics
	    System.out.println();
	    System.out.println("================================");
	    System.out.println("|       ClASSIFICATION MENU     |");
	    System.out.println("================================");
	    System.out.println("|1. Train Model                 |");
	    System.out.println("|2. Test Model                  |");
	    System.out.println("|3. Cross Validation            |");
	    System.out.println("|4. Run Classification          |");
	    System.out.println("|5. Run Label Software          |");
	    System.out.println("|6. Show Configuration          |");
	    System.out.println("|7. Exit                        |");
	    System.out.println("================================");
	    swValue = Keyin.inInt("Select option: ");
	    System.out.println();

	    // Switch construct
	    switch (swValue) {
	    case 1:
	      TrainModel.main(args);
	      break;
	    case 2:
	      TestModel.main(args);
	      break;
	    case 3:
		  CrossValidation.main(args);
		  break;
	    case 4:
	      RunModel.main(args);
	      break;
	    case 6:
			Configuration.showConfig();
			break;
	    default:
	      System.out.println("Invalid selection");
	      break; // This break is not really necessary
	    }
	  }
	}
}



/**
 * Util class for check input
 */
class Keyin {

	  //*******************************
	  //   support methods
	  //*******************************
	  //Method to display the user's prompt string
	  public static void printPrompt(String prompt) {
	    System.out.print(prompt + " ");
	    System.out.flush();
	  }

	  //Method to make sure no data is available in the
	  //input stream
	  public static void inputFlush() {
	    int dummy;
	    int bAvail;

	    try {
	      while ((System.in.available()) != 0)
	        dummy = System.in.read();
	    } catch (java.io.IOException e) {
	      System.out.println("Input error");
	    }
	  }

	  //********************************
	  //  data input methods for
	  //string, int, char, and double
	  //********************************
	  public static String inString(String prompt) {
	    inputFlush();
	    printPrompt(prompt);
	    return inString();
	  }

	  public static String inString() {
	    int aChar;
	    String s = "";
	    boolean finished = false;

	    while (!finished) {
	      try {
	        aChar = System.in.read();
	        if (aChar < 0 || (char) aChar == '\n')
	          finished = true;
	        else if ((char) aChar != '\r')
	          s = s + (char) aChar; // Enter into string
	      }

	      catch (java.io.IOException e) {
	        System.out.println("Input error");
	        finished = true;
	      }
	    }
	    return s;
	  }

	  public static int inInt(String prompt) {
	    while (true) {
	      inputFlush();
	      printPrompt(prompt);
	      try {
	        return Integer.valueOf(inString().trim()).intValue();
	      }

	      catch (NumberFormatException e) {
	        System.out.println("Invalid input. Not an integer");
	      }
	    }
	  }

	  public static char inChar(String prompt) {
	    int aChar = 0;

	    inputFlush();
	    printPrompt(prompt);

	    try {
	      aChar = System.in.read();
	    }

	    catch (java.io.IOException e) {
	      System.out.println("Input error");
	    }
	    inputFlush();
	    return (char) aChar;
	  }

	  public static double inDouble(String prompt) {
	    while (true) {
	      inputFlush();
	      printPrompt(prompt);
	      try {
	        return Double.valueOf(inString().trim()).doubleValue();
	      }

	      catch (NumberFormatException e) {
	        System.out
	            .println("Invalid input. Not a floating point number");
	      }
	    }
	  }
}
