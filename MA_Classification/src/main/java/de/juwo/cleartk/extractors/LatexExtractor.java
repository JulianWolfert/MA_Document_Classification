package de.juwo.cleartk.extractors;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.CleartkExtractorException;
import org.cleartk.classifier.feature.extractor.simple.SimpleFeatureExtractor;

import de.juwo.uima.cas.PDFMetadata;
import de.juwo.util.Configuration;

public class LatexExtractor<OUTCOME_T> implements SimpleFeatureExtractor{

	@Override
	public List<Feature> extract(JCas view, Annotation focusAnnotation)
			throws CleartkExtractorException {
		
		FSIterator<?> iterator = view.getJFSIndexRepository().getAllIndexedFS(
				PDFMetadata.type);
		
		List<Feature> result = new ArrayList<Feature>();
		Feature f = new Feature("Latex", 0);
		
		if (iterator.hasNext()) {
		
			PDFMetadata pdfMetadata = (PDFMetadata) iterator.next();
			
			String creator = pdfMetadata.getCreator();
			String producer = pdfMetadata.getProducer();
			String author = pdfMetadata.getAuthor();
			
			for (int i=0; i < Configuration.LATEX_STRINGS.size(); i++) {
				
				if (creator != null && creator.toLowerCase().contains(Configuration.LATEX_STRINGS.get(i))) {
					f.setValue(1);
					System.out.println("Found one with Latex");
					break;
				}
				if (producer != null && producer.toLowerCase().contains(Configuration.LATEX_STRINGS.get(i))) {
					f.setValue(1);
					System.out.println("Found one with Latex");
					break;
				}
				if (author != null && producer.toLowerCase().contains(Configuration.LATEX_STRINGS.get(i))) {
					f.setValue(1);
					System.out.println("Found one with Latex");
					break;
				}
				
			}
			
		}	
		result.add(f);
		return result;
	}

}
