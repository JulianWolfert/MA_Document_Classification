package de.juwo.uima.core;
/** 
 * Copyright (c) 2007-2012, Regents of the University of Colorado 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 * Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.classifier.CleartkAnnotator;
import org.cleartk.classifier.Instance;
import org.cleartk.classifier.feature.transform.InstanceDataWriter;
import org.cleartk.classifier.feature.transform.InstanceStream;
import org.cleartk.classifier.feature.transform.extractor.MinMaxNormalizationExtractor;
import org.cleartk.classifier.feature.transform.extractor.ZeroMeanUnitStddevExtractor;
import org.cleartk.classifier.jar.DefaultDataWriterFactory;
import org.cleartk.classifier.jar.DirectoryDataWriterFactory;
import org.cleartk.classifier.jar.GenericJarClassifierFactory;
import org.cleartk.classifier.jar.JarClassifierBuilder;
import org.cleartk.classifier.libsvm.LIBSVMStringOutcomeDataWriter;
import org.cleartk.eval.AnnotationStatistics;
import org.cleartk.eval.Evaluation_ImplBase;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.ConfigurationParameterFactory;
import org.uimafit.pipeline.JCasIterable;
import org.uimafit.pipeline.SimplePipeline;
import org.uimafit.testing.util.HideOutput;
import org.uimafit.util.JCasUtil;

import com.google.common.base.Function;

import de.christianherta.uima.core.casconsumer.SolrXMLFileWriter;
import de.juwo.cleartk.extractors.CentroidTfidfSimilarityExtractor;
import de.juwo.cleartk.extractors.TfidfExtractor;
import de.juwo.uima.cas.UsenetDocument;
import de.juwo.util.Configuration;
import de.renehelbig.uima.arcreader.ArcCollectionReader;


/**
 * <p>
 * This evaluation class is based on the class DocumentClassificationEvaluation by Lee Becker 
 * 
 * This class trains a document classificator (SVM) using the pdf's document's text 
 * which are stored in a collection of arc-files.
 * The trained classificator is used to classify pdf-documents.
 * 
 * <p>
 * 
 * 
 * @author Rene Helbig
 */
public class DocumentClassificationEvaluation extends
Evaluation_ImplBase<File, AnnotationStatistics<String>> {


	public static final String 	GOLD_VIEW_NAME = "DocumentClassificationGoldView";
	public static final String 	SYSTEM_VIEW_NAME = CAS.NAME_DEFAULT_SOFA;
	
	private List<String> 		trainingArguments;
	
	
	/**
	 * Constructor used for classify
	 * @param baseDirectory
	 */
	public DocumentClassificationEvaluation(File baseDirectory) {
		super(baseDirectory);
		this.trainingArguments = Arrays.<String> asList();
	}
	/**
	 * Constructor for Training
	 * @param baseDirectory
	 * @param trainingArguments
	 */
	public DocumentClassificationEvaluation(File baseDirectory, List<String> trainingArguments) {
		super(baseDirectory);
		this.trainingArguments = trainingArguments;
	}


	/**
	 * Method to do the actual training for classificator
	 */
	@Override
	public void train(CollectionReader collectionReader, File outputDirectory) throws Exception {
		
		System.err.println();
		System.err.println("###### START TRAINING ######");
		System.err.println();
		
		/**
		 * Step 1: Extract features and serialize the raw instance objects
		 * Note: DocumentClassificationAnnotator sets the various extractor URI values to null by
		 * default. This signals to the feature extractors that they are being written out for training
		 **/
		System.err.println("*Step 1: Extracting features and writing raw instances data");

		//Build the PreprocessingPipeline
		AggregateBuilder builder = DocumentPreprocessor.createPreprocessingAggregate(Configuration.AnnotatorMode.TRAIN, GOLD_VIEW_NAME);
		
		//Create the classification annotation pipeline
		builder = createPipeline(builder, outputDirectory, Configuration.AnnotatorMode.TRAIN);
		
		//run pipeline
		SimplePipeline.runPipeline(collectionReader, builder.createAggregateDescription());


		/**
		* Step 2: Transform features and write training data
		**/
		System.err.println("*Step 2: Collection feature normalization statistics");
		// Load the serialized instance data
		Iterable<Instance<String>> instances = InstanceStream.loadFromDirectory(outputDirectory);
		featureNormalization(instances, outputDirectory);

		/**
		* Stage 3: Train and write model
		**/
		System.err.println("*Step 3: Train model and write model.jar file.");
		trainModel(outputDirectory);

		System.err.println("Training complete!");
	}
	
	public void classify (CollectionReader collectionReader, File modelDirectory) throws Exception {
		
		System.err.println();
		System.err.println("###### START CLASSIFICATION ######");
		System.err.println();
		
		//Build the PreprocessingPipeline
		AggregateBuilder builder = DocumentPreprocessor.createPreprocessingAggregate(Configuration.AnnotatorMode.CLASSIFY, GOLD_VIEW_NAME);
		
		//Create the classification annotation pipeline
		builder = createPipeline(builder, modelDirectory, Configuration.AnnotatorMode.CLASSIFY);
		
	    //run pipeline
	    SimplePipeline.runPipeline(collectionReader, builder.createAggregateDescription());
	    
	    System.err.println("Classification complete!");
		
	}
	
	public AnnotationStatistics<String> trainAndTest(CollectionReader collectionReader, File modelDirectory) throws Exception {	
		this.train(collectionReader, modelDirectory);
		AnnotationStatistics<String> holdoutStat = this.test(collectionReader, modelDirectory);
		return holdoutStat;
	}

	
	@Override
	public AnnotationStatistics<String> test(CollectionReader collectionReader,
			File directory) throws Exception {

		System.err.println();
		System.err.println("###### START TEST ######");
		System.err.println();
		
		AnnotationStatistics<String> stats = new AnnotationStatistics<String>();

		AggregateBuilder builder = DocumentPreprocessor
				.createPreprocessingAggregate(Configuration.AnnotatorMode.TEST,
						GOLD_VIEW_NAME);
		builder = createPipeline(builder, directory,
				Configuration.AnnotatorMode.TEST);

		AnalysisEngine engine = builder.createAggregate();

		// Run and evaluate
		Function<UsenetDocument, ?> getSpan = AnnotationStatistics
				.annotationToSpan();
		Function<UsenetDocument, String> getCategory = AnnotationStatistics
				.annotationToFeatureValue("category");
		JCasIterable iter = new JCasIterable(collectionReader, engine);
		while (iter.hasNext()) {
			JCas jCas = iter.next();
			JCas goldView = jCas.getView(GOLD_VIEW_NAME);
			JCas systemView = jCas
					.getView(DocumentClassificationEvaluation.SYSTEM_VIEW_NAME);

			// Get results from system and gold views, and update results accordingly
			Collection<UsenetDocument> goldCategories = JCasUtil.select(
					goldView, UsenetDocument.class);
			Collection<UsenetDocument> systemCategories = JCasUtil.select(
					systemView, UsenetDocument.class);
			stats.add(goldCategories, systemCategories, getSpan, getCategory);
		}

		return stats;
	}

	/**
	 * Creates the classification pipeline according to the annotator mode
	 **/
	public static AggregateBuilder createPipeline(
		      AggregateBuilder builder,
			  File modelDirectory,
		      Configuration.AnnotatorMode mode) throws ResourceInitializationException {
		  
	    switch (mode) {
	      
	    case TRAIN:
	        // For training we will create DocumentClassificationAnnotator that
	        // Extracts the features as is, and then writes out the data to
	        // a serialized instance file.
	        builder.add(AnalysisEngineFactory.createPrimitiveDescription(
	            DocumentClassificationAnnotator.class,
	            DefaultDataWriterFactory.PARAM_DATA_WRITER_CLASS_NAME,
	            InstanceDataWriter.class.getName(),
	            DirectoryDataWriterFactory.PARAM_OUTPUT_DIRECTORY,
	            modelDirectory.getPath()));
	        break;
	      
	      case TEST:
	      
	      case CLASSIFY:
	      default:
	        // For testing and standalone classification, we want to create a
	        // DocumentClassificationAnnotator using
	        // all of the model data computed during training. This includes feature normalization data
	        // and thei model jar file for the classifying algorithm
	        AnalysisEngineDescription documentClassificationAnnotator = AnalysisEngineFactory.createPrimitiveDescription(
	            DocumentClassificationAnnotator.class,
	            CleartkAnnotator.PARAM_IS_TRAINING,
	            false,
	            GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH,
	            JarClassifierBuilder.getModelJarFile(modelDirectory));

	        ConfigurationParameterFactory.addConfigurationParameters(
	            documentClassificationAnnotator,
	            DocumentClassificationAnnotator.PARAM_TF_IDF_URI,
	            DocumentClassificationAnnotator.createTokenTfIdfDataURI(modelDirectory),
	            DocumentClassificationAnnotator.PARAM_TF_IDF_CENTROID_SIMILARITY_URI,
	            DocumentClassificationAnnotator.createIdfCentroidSimilarityDataURI(modelDirectory),
	            DocumentClassificationAnnotator.PARAM_MINMAX_URI,
	            DocumentClassificationAnnotator.createMinMaxDataURI(modelDirectory),
	            DocumentClassificationAnnotator.PARAM_ZMUS_URI,
	            DocumentClassificationAnnotator.createZmusDataURI(modelDirectory));
	        builder.add(documentClassificationAnnotator);
	        
			AnalysisEngineDescription solr = AnalysisEngineFactory.createPrimitiveDescription(SolrXMLFileWriter.class);
			builder.add(solr);
			
	        break;
	    }
	    return builder;

		  
	}
	
	
	private void trainModel(File outputDirectory) throws Exception{
		
		/**
		 * Now that the features have been extracted and normalized, we can proceed
		 * in running machine learning to train and package a model
		 **/
		HideOutput hider = new HideOutput();
		JarClassifierBuilder.trainAndPackage(
				outputDirectory,
				this.trainingArguments.toArray(new String[this.trainingArguments.size()]));
		hider.restoreOutput();
		hider.close();
		
	}
	
	
	private void featureNormalization(Iterable<Instance<String>> instances, File outputDirectory) throws Exception {
		
		/**
		 * In this phase, the normalization statistics are computed and the raw
		 * features are transformed into normalized features.
		 * Then the adjusted values are written with a DataWriter 
		 * for training (libsvm in this case)
		 * Normalization by example of Lee Becker
		 **/

		
		// Collect TF*IDF stats for computing tf*idf values on extracted
		// tokens
		URI tfIdfDataURI = DocumentClassificationAnnotator.createTokenTfIdfDataURI(outputDirectory);
		TfidfExtractor<String> extractor = new TfidfExtractor<String>(DocumentClassificationAnnotator.TFIDF_EXTRACTOR_KEY);
		extractor.train(instances);
		extractor.save(tfIdfDataURI);

		
		// Collect TF*IDF Centroid stats for computing similarity to corpus centroid
		URI tfIdfCentroidSimDataURI = DocumentClassificationAnnotator.createIdfCentroidSimilarityDataURI(outputDirectory);
		CentroidTfidfSimilarityExtractor<String> simExtractor = new CentroidTfidfSimilarityExtractor<String>(
				DocumentClassificationAnnotator.CENTROID_TFIDF_SIM_EXTRACTOR_KEY);
		simExtractor.train(instances);
		simExtractor.save(tfIdfCentroidSimDataURI);

		// Collect ZMUS stats for feature normalization
		URI zmusDataURI = DocumentClassificationAnnotator.createZmusDataURI(outputDirectory);
		ZeroMeanUnitStddevExtractor<String> zmusExtractor = new ZeroMeanUnitStddevExtractor<String>(
				DocumentClassificationAnnotator.ZMUS_EXTRACTOR_KEY);
		zmusExtractor.train(instances);
		zmusExtractor.save(zmusDataURI);

		// Collect MinMax stats for feature normalization
		URI minmaxDataURI = DocumentClassificationAnnotator.createMinMaxDataURI(outputDirectory);
		MinMaxNormalizationExtractor<String> minmaxExtractor = new MinMaxNormalizationExtractor<String>(
				DocumentClassificationAnnotator.MINMAX_EXTRACTOR_KEY);
		minmaxExtractor.train(instances);
		minmaxExtractor.save(minmaxDataURI);
		
		// Rerun training data writer pipeline, to transform the extracted instances -- an alternative,
		// more costly approach would be to reinitialize the DocumentClassificationAnnotator above with
		// the URIs for the feature
		// extractor.
		//
		System.out.println("Write out model training data (LibSVM-Format)");
		LIBSVMStringOutcomeDataWriter dataWriter = new LIBSVMStringOutcomeDataWriter(outputDirectory);
		int i = 0;
		for (Instance<String> instance : instances) {
			i ++;
			instance = extractor.transform(instance);
			instance = simExtractor.transform(instance);
			instance = zmusExtractor.transform(instance);
			instance = minmaxExtractor.transform(instance);
			dataWriter.write(instance);
		}
		System.out.println("Number of documents for training: "+i);
		dataWriter.finish();
		
	}
	
	
	/**
	 * Method to get ARCCollectionReader for training and classify
	 * @param dir directory with arc-files
	 * @return
	 * @throws Exception
	 */
	protected CollectionReader getCollectionReader(String dir) throws Exception {
		//get collection reader with working directory
		ArcCollectionReader.setARCDirectory(dir);
		return ArcCollectionReader.getCollectionReader();
	}

	/**
	 * Method to get ARCCollectionReader for cross validation
	 * @param files List of files to be processed by the collection reader
	 * @return
	 * @throws Exception
	 */
	@Override
	protected CollectionReader getCollectionReader(List<File> files)
			throws Exception {
		ArcCollectionReader.setARCFileList(files);
		return ArcCollectionReader.getCollectionReader();
	}
}