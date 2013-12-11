package de.juwo.uima.core;

import java.io.File;

import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.syntax.opennlp.SentenceAnnotator;
import org.cleartk.token.stem.snowball.DefaultSnowballStemmer;
import org.cleartk.token.tokenizer.TokenAnnotator;
import org.uimafit.component.ViewTextCopierAnnotator;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;

import de.juwo.util.Configuration;
import de.juwo.util.Configuration.AnnotatorMode;

public class DocumentPreprocessor {

	public DocumentPreprocessor() {
		
	}
	
	/**
	 * Creates the preprocessing pipeline needed for document classification. Specifically this
	 * consists of:
	 * <ul>
	 * <li>Populating the default view with the document text
	 * <li>Sentence segmentation
	 * <li>Tokenization
	 * <li>Stemming
	 * <li>labeling the document with gold-standard document categories if mode is TRAIN
	 * </ul>
	 */
	public static AggregateBuilder createPreprocessingAggregateTraining() throws ResourceInitializationException {

		AggregateBuilder builder = new AggregateBuilder();

		// NLP pre-processing components
		if (Configuration.SENTENCE_ANNOTATOR)
			builder.add(SentenceAnnotator.getDescription());
		if (Configuration.TOKEN_ANNOTATOR)
			builder.add(TokenAnnotator.getDescription());
		if (Configuration.STEMMING)
			builder.add(DefaultSnowballStemmer.getDescription(Configuration.STEMMING_LANGUAGE));
		// If this is training, put the label categories directly into the default view
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(GoldDocumentCategoryAnnotator.class));

		return builder;
	}
	/**
	 * Creates the preprocessing pipeline needed for document classification. Specifically this
	 * consists of:
	 * <ul>
	 * <li>Populating the default view with the document text
	 * <li>Sentence segmentation
	 * <li>Tokenization
	 * <li>Stemming
	 * <li>labeling the document with gold-standard document categories if mode is TRAIN
	 * </ul>
	 */
	public static AggregateBuilder createPreprocessingAggregateClassification() throws ResourceInitializationException {

		AggregateBuilder builder = new AggregateBuilder();

		// NLP pre-processing components
		if (Configuration.SENTENCE_ANNOTATOR)
			builder.add(SentenceAnnotator.getDescription());
		if (Configuration.TOKEN_ANNOTATOR)
			builder.add(TokenAnnotator.getDescription());
		if (Configuration.STEMMING)
			builder.add(DefaultSnowballStemmer.getDescription(Configuration.STEMMING_LANGUAGE));

		return builder;
	}
	
	
	public static AggregateBuilder createPreprocessingAggregate(
			AnnotatorMode mode, String GOLD_VIEW_NAME) throws ResourceInitializationException {
		
		AggregateBuilder builder = new AggregateBuilder();
		
		if (Configuration.SENTENCE_ANNOTATOR)
			builder.add(SentenceAnnotator.getDescription());
		if (Configuration.TOKEN_ANNOTATOR)
			builder.add(TokenAnnotator.getDescription());
		if (Configuration.STEMMING)
			builder.add(DefaultSnowballStemmer.getDescription(Configuration.STEMMING_LANGUAGE));
		
	    // Now annotate documents with gold standard labels
	    switch (mode) {
	      case TRAIN:
	        // If this is training, put the label categories directly into the default view
	        builder.add(AnalysisEngineFactory.createPrimitiveDescription(GoldDocumentCategoryAnnotator.class));
	        break;

	      case TEST:
	        // Copies the text from the default view to a separate gold view
	        builder.add(AnalysisEngineFactory.createPrimitiveDescription(
	            ViewTextCopierAnnotator.class,
	            ViewTextCopierAnnotator.PARAM_SOURCE_VIEW_NAME,
	            CAS.NAME_DEFAULT_SOFA,
	            ViewTextCopierAnnotator.PARAM_DESTINATION_VIEW_NAME,
	            GOLD_VIEW_NAME));

	        // If this is testing, put the document categories in the gold view
	        // The extra parameters to add() map the default view to the gold view.
	        builder.add(
	            AnalysisEngineFactory.createPrimitiveDescription(GoldDocumentCategoryAnnotator.class),
	            CAS.NAME_DEFAULT_SOFA,
	            GOLD_VIEW_NAME);
	        break;

	      case CLASSIFY:
	      default:
	        // In normal mode don't deal with gold labels
	        break;
	    }
		
		return builder;
	}
	
}
