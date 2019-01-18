package com.qy.cn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class GetHtmlDoc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final WebClient webClient=new WebClient();
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page = null;
		try {
			page = webClient.getPage("https://search.jd.com/Search?keyword=ÊÖ»ú&enc=utf-8");
			List<DomElement>  doc = page.getElementsByIdAndOrName("J_goodsList");
			System.out.println(doc.toString());
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		webClient.close();
	}

}
