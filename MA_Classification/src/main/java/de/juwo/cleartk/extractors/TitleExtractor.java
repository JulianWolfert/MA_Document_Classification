package de.juwo.cleartk.extractors;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.CleartkExtractorException;
import org.cleartk.classifier.feature.extractor.simple.SimpleFeatureExtractor;

import de.juwo.uima.cas.DocumentMetadata;
import de.juwo.uima.cas.PDFMetadata;
import de.juwo.util.Configuration;

public class TitleExtractor<OUTCOME_T> implements SimpleFeatureExtractor{

	@Override
	public List<Feature> extract(JCas view, Annotation focusAnnotation)
			throws CleartkExtractorException {
		
		FSIterator<?> iterator = view.getJFSIndexRepository().getAllIndexedFS(
				PDFMetadata.type);
		
		List<Feature> result = new ArrayList<Feature>();

		
		if (iterator.hasNext()) {
		
			PDFMetadata pdfMetadata = (PDFMetadata) iterator.next();
			
			String title = pdfMetadata.getTitle();

			if (title != null) {
				String[] title_parts = title.split(" ");
				
				for (int i=0; i < title_parts.length; i++) {
					Feature f = new Feature("Title_" + title_parts[i], 1);
					result.add(f);
				}
			}
		}	
		
		return result;
	}

}
