package com.qy.test;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
public class apper {
private static String testString1 = "The quick brown fox jumped over the lazy dogs";
    private static String testString2 = "xy&z mail is - xyz@sohu.com";
    
    public static void testWhitespace(String testString) throws Exception{
    
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);      
        Reader r = new StringReader(testString);      
        // Tokenizer ts = (Tokenizer) analyzer.tokenStream("", r);   
    TokenStream ts =analyzer.tokenStream("", r);
CharTermAttribute cab = ts.addAttribute(CharTermAttribute.class);
        System.err.println("=====Whitespace analyzer====");
        System.err.println("分析方法：空格分割");
             
        while (ts.incrementToken()) {      
          System.out.print(cab.toString()+"  ;");      
       } 
System.out.println();    
    }
    public static void testSimple(String testString) throws Exception{
        Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_36);      
        Reader r = new StringReader(testString);      
       // Tokenizer ts = (Tokenizer) analyzer.tokenStream("", r); 
        TokenStream ts =analyzer.tokenStream("", r);
CharTermAttribute cab = ts.addAttribute(CharTermAttribute.class);
        
        System.err.println("=====Simple analyzer====");
        System.err.println("分析方法：空格及各种符号分割");
        while (ts.incrementToken()) {      
          System.out.print(cab.toString()+"  ;");      
       } 
System.out.println();     
    }
    public static void testStop(String testString) throws Exception{
        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_36);      
        Reader r = new StringReader(testString);      
        StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
        
        System.err.println("=====stop analyzer====");  
        System.err.println("分析方法：空格及各种符号分割,去掉停止词，停止词包括 is,are,in,on,the等无实际意义的词");
        //停止词
       
CharTermAttribute cab = sf.addAttribute(CharTermAttribute.class);     
        
while (sf.incrementToken()) {      
          System.out.print(cab.toString()+"  ;");      
       } 
System.out.println();     
    }
    public static void testStandard(String testString) throws Exception{
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);      
        Reader r = new StringReader(testString);      
        StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
        System.err.println("=====standard analyzer====");
        System.err.println("分析方法：混合分割,包括了去掉停止词，支持汉语");
      
CharTermAttribute cab = sf.addAttribute(CharTermAttribute.class);     
while (sf.incrementToken()) {      
          System.out.print(cab.toString()+"  ;");      
       } 
System.out.println();
    }

	public static void main(String[] args) throws Exception {
		testWhitespace("i am lihan, i am a boy, i come from Beijing，我是来自北京的李晗");
		testSimple("i am lihan, i am a boy, i come from Beijing，我是来自北京的李晗");
		testStop("i am lihan, i am a boy, i come from Beijing，我是来自北京的李晗");
		testStandard("i am lihan, i am a boy, i come from Beijing，我是来自北京的李晗");
	}
}