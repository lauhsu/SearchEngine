package com.qy.cn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;



public class TestStopWordss {
	public static void main(String[] args) throws IOException {
/*		String keyWords = "12";
		URL aa = TestStopWords.class.getResource("");
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(new File("data/stopword.txt")));
		IKSegmenter ikSegmenter = new IKSegmenter(isr, true);
		Lexeme lexeme = null;
		while((lexeme=ikSegmenter.next())!= null){
			System.out.println(lexeme.getLexemeText());
		}*/
		new TestStopWordss().ss();
	}
	
	public  void ss() {
	 String a =	this.getClass().getResource("/").getPath();
	 System.out.println(a);
	}
}	