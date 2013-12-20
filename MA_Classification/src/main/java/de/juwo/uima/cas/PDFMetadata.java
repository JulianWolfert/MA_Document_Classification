

/* First created by JCasGen Fri Dec 13 18:16:41 CET 2013 */
package de.juwo.uima.cas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;


/** 
 * Updated by JCasGen Fri Dec 13 18:16:41 CET 2013
 * XML source: /Users/Jules/Projects/HTW/Masterarbeit_Repo/MA_Classification/src/main/resources/pdfMetadata.xml
 * @generated */
public class PDFMetadata extends TOP {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PDFMetadata.class);
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
  protected PDFMetadata() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public PDFMetadata(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public PDFMetadata(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: author

  /** getter for author - gets 
   * @generated */
  public String getAuthor() {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_author == null)
      jcasType.jcas.throwFeatMissing("author", "de.juwo.uima.cas.PDFMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_author);}
    
  /** setter for author - sets  
   * @generated */
  public void setAuthor(String v) {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_author == null)
      jcasType.jcas.throwFeatMissing("author", "de.juwo.uima.cas.PDFMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_author, v);}    
   
    
  //*--------------*
  //* Feature: title

  /** getter for title - gets 
   * @generated */
  public String getTitle() {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "de.juwo.uima.cas.PDFMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_title);}
    
  /** setter for title - sets  
   * @generated */
  public void setTitle(String v) {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "de.juwo.uima.cas.PDFMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_title, v);}    
   
    
  //*--------------*
  //* Feature: subject

  /** getter for subject - gets 
   * @generated */
  public String getSubject() {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_subject == null)
      jcasType.jcas.throwFeatMissing("subject", "de.juwo.uima.cas.PDFMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_subject);}
    
  /** setter for subject - sets  
   * @generated */
  public void setSubject(String v) {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_subject == null)
      jcasType.jcas.throwFeatMissing("subject", "de.juwo.uima.cas.PDFMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_subject, v);}    
   
    
  //*--------------*
  //* Feature: creator

  /** getter for creator - gets 
   * @generated */
  public String getCreator() {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_creator == null)
      jcasType.jcas.throwFeatMissing("creator", "de.juwo.uima.cas.PDFMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_creator);}
    
  /** setter for creator - sets  
   * @generated */
  public void setCreator(String v) {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_creator == null)
      jcasType.jcas.throwFeatMissing("creator", "de.juwo.uima.cas.PDFMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_creator, v);}    
   
    
  //*--------------*
  //* Feature: producer

  /** getter for producer - gets 
   * @generated */
  public String getProducer() {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_producer == null)
      jcasType.jcas.throwFeatMissing("producer", "de.juwo.uima.cas.PDFMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_producer);}
    
  /** setter for producer - sets  
   * @generated */
  public void setProducer(String v) {
    if (PDFMetadata_Type.featOkTst && ((PDFMetadata_Type)jcasType).casFeat_producer == null)
      jcasType.jcas.throwFeatMissing("producer", "de.juwo.uima.cas.PDFMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((PDFMetadata_Type)jcasType).casFeatCode_producer, v);}    
  }

    