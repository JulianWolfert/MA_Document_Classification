

/* First created by JCasGen Thu Jan 16 17:41:37 CET 2014 */
package de.juwo.uima.cas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jan 16 17:41:37 CET 2014
 * XML source: /Users/Jules/Projects/HTW/Masterarbeit_Repo/MA_Classification/src/main/resources/UsenetDocument.xml
 * @generated */
public class UsenetDocument extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(UsenetDocument.class);
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
  protected UsenetDocument() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public UsenetDocument(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public UsenetDocument(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public UsenetDocument(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: category

  /** getter for category - gets The category label for the document (e.g. sports, entertainment, politics ...)
   * @generated */
  public String getCategory() {
    if (UsenetDocument_Type.featOkTst && ((UsenetDocument_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "de.juwo.uima.cas.UsenetDocument");
    return jcasType.ll_cas.ll_getStringValue(addr, ((UsenetDocument_Type)jcasType).casFeatCode_category);}
    
  /** setter for category - sets The category label for the document (e.g. sports, entertainment, politics ...) 
   * @generated */
  public void setCategory(String v) {
    if (UsenetDocument_Type.featOkTst && ((UsenetDocument_Type)jcasType).casFeat_category == null)
      jcasType.jcas.throwFeatMissing("category", "de.juwo.uima.cas.UsenetDocument");
    jcasType.ll_cas.ll_setStringValue(addr, ((UsenetDocument_Type)jcasType).casFeatCode_category, v);}    
  }

    