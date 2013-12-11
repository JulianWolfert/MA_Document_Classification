

/* First created by JCasGen Fri Oct 25 21:55:02 CEST 2013 */
package de.christianherta.uima.cas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;


/** Metadata for a document
 * Updated by JCasGen Fri Oct 25 21:55:02 CEST 2013
 * XML source: /Users/Jules/Projects/HTW/Masterarbeit_Repo/MA_Classification/src/main/resources/DocumentMetadata.xml
 * @generated */
public class DocumentMetadata extends TOP {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DocumentMetadata.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected DocumentMetadata() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DocumentMetadata(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DocumentMetadata(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: label

  /** getter for label - gets A manually given label
   * @generated */
  public String getLabel() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_label);}
    
  /** setter for label - sets A manually given label 
   * @generated */
  public void setLabel(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_label, v);}    
   
    
  //*--------------*
  //* Feature: documentURL

  /** getter for documentURL - gets The original URL of the document
   * @generated */
  public String getDocumentURL() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_documentURL == null)
      jcasType.jcas.throwFeatMissing("documentURL", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_documentURL);}
    
  /** setter for documentURL - sets The original URL of the document 
   * @generated */
  public void setDocumentURL(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_documentURL == null)
      jcasType.jcas.throwFeatMissing("documentURL", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_documentURL, v);}    
   
    
  //*--------------*
  //* Feature: documentID

  /** getter for documentID - gets A unique document ID.
   * @generated */
  public String getDocumentID() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_documentID == null)
      jcasType.jcas.throwFeatMissing("documentID", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_documentID);}
    
  /** setter for documentID - sets A unique document ID. 
   * @generated */
  public void setDocumentID(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_documentID == null)
      jcasType.jcas.throwFeatMissing("documentID", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_documentID, v);}    
   
    
  //*--------------*
  //* Feature: mimeType

  /** getter for mimeType - gets The mime type of a document
   * @generated */
  public String getMimeType() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_mimeType == null)
      jcasType.jcas.throwFeatMissing("mimeType", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_mimeType);}
    
  /** setter for mimeType - sets The mime type of a document 
   * @generated */
  public void setMimeType(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_mimeType == null)
      jcasType.jcas.throwFeatMissing("mimeType", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_mimeType, v);}    
   
    
  //*--------------*
  //* Feature: source

  /** getter for source - gets The source of the document (e.g. the heritrix arc file the document was extracted from, or the crawl or DB it was fetched from)
   * @generated */
  public String getSource() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_source);}
    
  /** setter for source - sets The source of the document (e.g. the heritrix arc file the document was extracted from, or the crawl or DB it was fetched from) 
   * @generated */
  public void setSource(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_source, v);}    
   
    
  //*--------------*
  //* Feature: encoding

  /** getter for encoding - gets The character encoding of this document
   * @generated */
  public String getEncoding() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_encoding == null)
      jcasType.jcas.throwFeatMissing("encoding", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_encoding);}
    
  /** setter for encoding - sets The character encoding of this document 
   * @generated */
  public void setEncoding(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_encoding == null)
      jcasType.jcas.throwFeatMissing("encoding", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_encoding, v);}    
   
    
  //*--------------*
  //* Feature: host

  /** getter for host - gets 
   * @generated */
  public String getHost() {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_host == null)
      jcasType.jcas.throwFeatMissing("host", "de.christianherta.uima.cas.DocumentMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_host);}
    
  /** setter for host - sets  
   * @generated */
  public void setHost(String v) {
    if (DocumentMetadata_Type.featOkTst && ((DocumentMetadata_Type)jcasType).casFeat_host == null)
      jcasType.jcas.throwFeatMissing("host", "de.christianherta.uima.cas.DocumentMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentMetadata_Type)jcasType).casFeatCode_host, v);}    
  }

    