package com.qy.test;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Text {
    private static final String url = "http://www.bbc.com/news/business-42968342";
    
    public static void main(String[] args) {
        try {
            //��ȡ������վ�ĸ��ڵ㣬Ҳ����html��ͷ����һֱ������
            Document document = Jsoup.connect(url).post();
            Element a = document.getElementById("page");
            Elements aa = a.getElementsByAttributeValue("role", "main");
            Elements ele = aa.select(".story-body");
            System.out.println(ele.text());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}