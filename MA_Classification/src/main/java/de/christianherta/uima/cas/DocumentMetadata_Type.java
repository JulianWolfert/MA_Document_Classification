
/* First created by JCasGen Fri Oct 25 21:55:02 CEST 2013 */
package de.christianherta.uima.cas;

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

/** Metadata for a document
 * Updated by JCasGen Fri Oct 25 21:55:02 CEST 2013
 * @generated */
public class DocumentMetadata_Type extends TOP_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DocumentMetadata_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DocumentMetadata_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DocumentMetadata(addr, DocumentMetadata_Type.this);
  			   DocumentMetadata_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DocumentMetadata(addr, DocumentMetadata_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DocumentMetadata.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.christianherta.uima.cas.DocumentMetadata");
 
  /** @generated */
  final Feature casFeat_label;
  /** @generated */
  final int     casFeatCode_label;
  /** @generated */ 
  public String getLabel(int addr) {
        if (featOkTst && casFeat_label == null)
      jcas.throwFeatMissing("label", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_label);
  }
  /** @generated */    
  public void setLabel(int addr, String v) {
        if (featOkTst && casFeat_label == null)
      jcas.throwFeatMissing("label", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_label, v);}
    
  
 
  /** @generated */
  final Feature casFeat_documentURL;
  /** @generated */
  final int     casFeatCode_documentURL;
  /** @generated */ 
  public String getDocumentURL(int addr) {
        if (featOkTst && casFeat_documentURL == null)
      jcas.throwFeatMissing("documentURL", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_documentURL);
  }
  /** @generated */    
  public void setDocumentURL(int addr, String v) {
        if (featOkTst && casFeat_documentURL == null)
      jcas.throwFeatMissing("documentURL", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_documentURL, v);}
    
  
 
  /** @generated */
  final Feature casFeat_documentID;
  /** @generated */
  final int     casFeatCode_documentID;
  /** @generated */ 
  public String getDocumentID(int addr) {
        if (featOkTst && casFeat_documentID == null)
      jcas.throwFeatMissing("documentID", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_documentID);
  }
  /** @generated */    
  public void setDocumentID(int addr, String v) {
        if (featOkTst && casFeat_documentID == null)
      jcas.throwFeatMissing("documentID", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_documentID, v);}
    
  
 
  /** @generated */
  final Feature casFeat_mimeType;
  /** @generated */
  final int     casFeatCode_mimeType;
  /** @generated */ 
  public String getMimeType(int addr) {
        if (featOkTst && casFeat_mimeType == null)
      jcas.throwFeatMissing("mimeType", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_mimeType);
  }
  /** @generated */    
  public void setMimeType(int addr, String v) {
        if (featOkTst && casFeat_mimeType == null)
      jcas.throwFeatMissing("mimeType", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_mimeType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_source;
  /** @generated */
  final int     casFeatCode_source;
  /** @generated */ 
  public String getSource(int addr) {
        if (featOkTst && casFeat_source == null)
      jcas.throwFeatMissing("source", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_source);
  }
  /** @generated */    
  public void setSource(int addr, String v) {
        if (featOkTst && casFeat_source == null)
      jcas.throwFeatMissing("source", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_encoding;
  /** @generated */
  final int     casFeatCode_encoding;
  /** @generated */ 
  public String getEncoding(int addr) {
        if (featOkTst && casFeat_encoding == null)
      jcas.throwFeatMissing("encoding", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_encoding);
  }
  /** @generated */    
  public void setEncoding(int addr, String v) {
        if (featOkTst && casFeat_encoding == null)
      jcas.throwFeatMissing("encoding", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_encoding, v);}
    
  
 
  /** @generated */
  final Feature casFeat_host;
  /** @generated */
  final int     casFeatCode_host;
  /** @generated */ 
  public String getHost(int addr) {
        if (featOkTst && casFeat_host == null)
      jcas.throwFeatMissing("host", "de.christianherta.uima.cas.DocumentMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_host);
  }
  /** @generated */    
  public void setHost(int addr, String v) {
        if (featOkTst && casFeat_host == null)
      jcas.throwFeatMissing("host", "de.christianherta.uima.cas.DocumentMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_host, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DocumentMetadata_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_label = jcas.getRequiredFeatureDE(casType, "label", "uima.cas.String", featOkTst);
    casFeatCode_label  = (null == casFeat_label) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_label).getCode();

 
    casFeat_documentURL = jcas.getRequiredFeatureDE(casType, "documentURL", "uima.cas.String", featOkTst);
    casFeatCode_documentURL  = (null == casFeat_documentURL) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_documentURL).getCode();

 
    casFeat_documentID = jcas.getRequiredFeatureDE(casType, "documentID", "uima.cas.String", featOkTst);
    casFeatCode_documentID  = (null == casFeat_documentID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_documentID).getCode();

 
    casFeat_mimeType = jcas.getRequiredFeatureDE(casType, "mimeType", "uima.cas.String", featOkTst);
    casFeatCode_mimeType  = (null == casFeat_mimeType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_mimeType).getCode();

 
    casFeat_source = jcas.getRequiredFeatureDE(casType, "source", "uima.cas.String", featOkTst);
    casFeatCode_source  = (null == casFeat_source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_source).getCode();

 
    casFeat_encoding = jcas.getRequiredFeatureDE(casType, "encoding", "uima.cas.String", featOkTst);
    casFeatCode_encoding  = (null == casFeat_encoding) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_encoding).getCode();

 
    casFeat_host = jcas.getRequiredFeatureDE(casType, "host", "uima.cas.String", featOkTst);
    casFeatCode_host  = (null == casFeat_host) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_host).getCode();

  }
}



    