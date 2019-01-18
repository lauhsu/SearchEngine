package com.qy.cn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import org.junit.Test;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;



public class TestStopWords {
	public static void main(String[] args) throws IOException {
/*		String keyWords = "12";
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(new File("data/stopword.txt")));
		IKSegmenter ikSegmenter = new IKSegmenter(isr, true);
		Lexeme lexeme = null;
		while((lexeme=ikSegmenter.next())!= null){
			System.out.println(lexeme.getLexemeText());
		}*/
		new TestStopWords().ss();
	}
	
	public  void ss() throws IOException {/*
	 String a =	this.getClass().getResource("/").getPath();
	 InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(a+"ext.dic")));
	 IKSegmenter ikSegmenter = new IKSegmenter(isr, true);
		Lexeme lexeme = null;
		while((lexeme=ikSegmenter.next())!= null){
			System.out.println(lexeme.getLexemeText());
		}
	 System.out.println(a);
	*/}
	
	
	@Test  
	// 测试分词的效果，以及停用词典是否起作用  
	public void test() throws IOException {  
	    String text = "老爹我们都爱您！";  
	    Configuration configuration = DefaultConfig.getInstance();  
	    
	    configuration.setUseSmart(true);  
	    IKSegmenter ik = new IKSegmenter(new StringReader(text), configuration);  
	    Lexeme lexeme = null;  
	    while ((lexeme = ik.next()) != null) {  
	        System.out.println(lexeme.getLexemeText());  
	    }  
	}
}	