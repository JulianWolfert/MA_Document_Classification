
/* First created by JCasGen Fri Dec 13 18:16:41 CET 2013 */
package de.juwo.uima.cas;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Fri Dec 13 18:16:41 CET 2013
 * @generated */
public class PDFMetadata_Type extends TOP_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (PDFMetadata_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = PDFMetadata_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new PDFMetadata(addr, PDFMetadata_Type.this);
  			   PDFMetadata_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new PDFMetadata(addr, PDFMetadata_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = PDFMetadata.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.juwo.uima.cas.PDFMetadata");
 
  /** @generated */
  final Feature casFeat_author;
  /** @generated */
  final int     casFeatCode_author;
  /** @generated */ 
  public String getAuthor(int addr) {
        if (featOkTst && casFeat_author == null)
      jcas.throwFeatMissing("author", "de.juwo.uima.cas.PDFMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_author);
  }
  /** @generated */    
  public void setAuthor(int addr, String v) {
        if (featOkTst && casFeat_author == null)
      jcas.throwFeatMissing("author", "de.juwo.uima.cas.PDFMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_author, v);}
    
  
 
  /** @generated */
  final Feature casFeat_title;
  /** @generated */
  final int     casFeatCode_title;
  /** @generated */ 
  public String getTitle(int addr) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "de.juwo.uima.cas.PDFMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_title);
  }
  /** @generated */    
  public void setTitle(int addr, String v) {
        if (featOkTst && casFeat_title == null)
      jcas.throwFeatMissing("title", "de.juwo.uima.cas.PDFMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_title, v);}
    
  
 
  /** @generated */
  final Feature casFeat_subject;
  /** @generated */
  final int     casFeatCode_subject;
  /** @generated */ 
  public String getSubject(int addr) {
        if (featOkTst && casFeat_subject == null)
      jcas.throwFeatMissing("subject", "de.juwo.uima.cas.PDFMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_subject);
  }
  /** @generated */    
  public void setSubject(int addr, String v) {
        if (featOkTst && casFeat_subject == null)
      jcas.throwFeatMissing("subject", "de.juwo.uima.cas.PDFMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_subject, v);}
    
  
 
  /** @generated */
  final Feature casFeat_creator;
  /** @generated */
  final int     casFeatCode_creator;
  /** @generated */ 
  public String getCreator(int addr) {
        if (featOkTst && casFeat_creator == null)
      jcas.throwFeatMissing("creator", "de.juwo.uima.cas.PDFMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_creator);
  }
  /** @generated */    
  public void setCreator(int addr, String v) {
        if (featOkTst && casFeat_creator == null)
      jcas.throwFeatMissing("creator", "de.juwo.uima.cas.PDFMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_creator, v);}
    
  
 
  /** @generated */
  final Feature casFeat_producer;
  /** @generated */
  final int     casFeatCode_producer;
  /** @generated */ 
  public String getProducer(int addr) {
        if (featOkTst && casFeat_producer == null)
      jcas.throwFeatMissing("producer", "de.juwo.uima.cas.PDFMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_producer);
  }
  /** @generated */    
  public void setProducer(int addr, String v) {
        if (featOkTst && casFeat_producer == null)
      jcas.throwFeatMissing("producer", "de.juwo.uima.cas.PDFMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_producer, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public PDFMetadata_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_author = jcas.getRequiredFeatureDE(casType, "author", "uima.cas.String", featOkTst);
    casFeatCode_author  = (null == casFeat_author) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_author).getCode();

 
    casFeat_title = jcas.getRequiredFeatureDE(casType, "title", "uima.cas.String", featOkTst);
    casFeatCode_title  = (null == casFeat_title) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_title).getCode();

 
    casFeat_subject = jcas.getRequiredFeatureDE(casType, "subject", "uima.cas.String", featOkTst);
    casFeatCode_subject  = (null == casFeat_subject) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_subject).getCode();

 
    casFeat_creator = jcas.getRequiredFeatureDE(casType, "creator", "uima.cas.String", featOkTst);
    casFeatCode_creator  = (null == casFeat_creator) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_creator).getCode();

 
    casFeat_producer = jcas.getRequiredFeatureDE(casType, "producer", "uima.cas.String", featOkTst);
    casFeatCode_producer  = (null == casFeat_producer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_producer).getCode();

  }
}



    