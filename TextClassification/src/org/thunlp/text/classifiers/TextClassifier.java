package org.thunlp.text.classifiers;

import org.thunlp.text.Lexicon;

/**
 * 文本分类器接口，可以作为一个黑盒子来使用。首先准备适当的训练文本，不断调用addTrainingText来把
 * 训练文本加入到训练集合中。加入所有训练文本后，调用train来训练模型，然后对新的文本调用classify来
 * 得到对应的类别编号。
 * 
 * saveModel和loadModel可以把训练好的模型和特征保存到一个文件中以供反复使用
 * 
 * @author adam
 */
public interface TextClassifier {
  
  /**
   * 把带有标签的文本加入到训练集合中
   * @param text 训练文本
   * @param label 标签，可以是任意整数值
   * @return 加入是否成功
   */
  public boolean addTrainingText ( String text, int label );
  
  /**
   * 利用已经给出的训练集合训练分类器
   * @return 训练是否成功
   */
  public boolean train();
  
  /**
   * 把训练好的分类模型保存到文件中
   * @param filename 模型文件
   */
  public boolean saveModel( String filename );
  
  /**
   * 从文件中载入之前训练好的模型
   * @param filename 模型文件
   */
  public boolean loadModel( String filename );
  
  /**
   * 对新文本进行分类
   * @param text 待分类文本
   * @return 文本的类别和分类概率
   */
  public ClassifyResult classify( String text );
  
  /**
   * 对新文本进行分类
   * @param text 待分类文本
   * @param topN 前N个结果候选
   * @return 文本的类别和分类概率
   */
  public ClassifyResult[] classify( String text, int topN );
  
  /**
   * 将模型序列化为一个字符串.
   * @return
   */
  public String saveToString();
  
  /**
   *将一个字符串还原为一个模型
   */
  public void loadFromString(String model);
  
  /**
   * 设置保留特征值的最大数量
   * @param MaxFeatures 保留特征值的数量
   */
  public void setMaxFeatures(int MaxFeatures);
  
  /**
   * 返回训练字典
   * @return 字典
   */
  public Lexicon getLexicon();
}
