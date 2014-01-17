package de.juwo.uima.core;
/** 
 * Copyright (c) 2012, Regents of the University of Colorado 
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


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.cleartk.util.ViewURIUtil;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.renehelbig.uima.arcreader.LabelStorage;
import de.juwo.uima.cas.UsenetDocument;
import de.juwo.util.*;


/**
 * 
 * 
 * This class is based on GoldDocumentCategoryAnnotator by Lee Becker
 * This class will assign the gold-standard document categories for the documents
 * by using the stored manuall classlabel information
 * 
 * @author Rene Helbig
 * 
 */
public class GoldDocumentCategoryAnnotator extends JCasAnnotator_ImplBase {
	private HashMap<String, String> uriIsClass = null;
	private int class_1_counter = 0;
	private int class_2_counter = 0;

	public GoldDocumentCategoryAnnotator(){
		LabelStorage storage = new LabelStorage();
		//create Hashmap of all documenturls with classname 
		this.uriIsClass =  storage.getAllClassLabels();
	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		try {
			JCas uriView = jCas.getView(ViewURIUtil.URI);
			//get URI of document
			URI uri = new URI(uriView.getSofaDataURI());

			if(jCas.getDocumentText() != null && this.uriIsClass.get(uri.toString().trim()) != null){
				
				UsenetDocument document = new UsenetDocument(jCas, 0, jCas.getDocumentText().length());
				if(this.uriIsClass.get(uri.toString().trim()).equals(Configuration.CLASS_1) ){
					document.setCategory(Configuration.CLASS_1);
					class_1_counter++;
				}else{
					document.setCategory(Configuration.CLASS_2);
					class_2_counter++;
				}
				document.addToIndexes();
			}
			else{
				// if document was never labeled manually make it part of class 2
				UsenetDocument document = new UsenetDocument(jCas, 0, 0);
				document.setCategory(Configuration.CLASS_2);
				System.out.println("Document isn't labeled");
				class_2_counter++;
				document.addToIndexes();
			}

		} catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		} catch (URISyntaxException e) {
			throw new AnalysisEngineProcessException(e);
		}
		//System.out.println("Teach: "+teachStuffCounter);
		//System.out.println("Other: "+otherStuffCounter);
	}

}
