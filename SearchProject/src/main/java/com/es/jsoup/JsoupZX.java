package com.es.jsoup;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 爬虫 并创建索引
 * Created by monster on 2018/02/11.
 */
public class JsoupZX {
    public final static String HOST = "127.0.0.1";
    
    public final static int PORT = 9300;//http请求的端口是9200，
    public static void main(String[] args){
    	createIndexFromDataSource();
    }
    
    //数据爬虫
    public static void getData() {
    	

        Map<String,String> typeMap = new HashMap<>();
        typeMap.put("军事", "http://news.baidu.com/mil");
        typeMap.put("娱乐", "http://news.baidu.com/ent");
        try {
   
        	for(Map.Entry<String, String> map:typeMap.entrySet()) {
        		
                Document doc = Jsoup.connect(map.getValue()).get();

                Elements container =    doc.select("div[id=body]>div[id=col_focus]>div[class=l-left-col]>div[class=b-left]>ul");
                
                System.out.println(container.get(0).toString());
                 Document containerDoc = Jsoup.parse(container.get(0).toString());
                 Elements el =    containerDoc.getAllElements();
                 el = el.select("ul>li");
                 
                 for(int i=0;i<el.size();i++) {
                 	Elements temp = el.get(i).select("a");	
                 String urlStr  =	temp.attr("href");
                 String title  =	temp.text();
                 
                 Document doc2 = Jsoup.connect(urlStr).get();
                 System.out.println("******************"+title+":"+urlStr+"********************");
                 System.out.println("["+doc2.text()+"]");
                 System.out.println("==================================================================================");
               
                }
               
        	}
        	



        } catch (IOException e) {
            e.printStackTrace();
        }
    
    	
    }
    
    public static void createIndexFromDataSource() {
    	
    	

        Map<String,String> typeMap = new HashMap<>();
        typeMap.put("军事", "http://news.baidu.com/mil");
        typeMap.put("娱乐", "http://news.baidu.com/ent");
        try {
   
        	for(Map.Entry<String, String> map:typeMap.entrySet()) {
        		
                Document doc = Jsoup.connect(map.getValue()).get();

                Elements container =    doc.select("div[id=body]>div[id=col_focus]>div[class=l-left-col]>div[class=b-left]>ul");
                
                System.out.println(container.get(0).toString());
                 Document containerDoc = Jsoup.parse(container.get(0).toString());
                 Elements el =    containerDoc.getAllElements();
                 el = el.select("ul>li");
                 
               
                 
                 for(int i=0;i<el.size();i++) {
                 	Elements temp = el.get(i).select("a");	
                 String urlStr  =	temp.attr("href");
                 String title  =	temp.text();
                 
                 /////////////
                 
                 //创建客户端
                 TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                                          new TransportAddress(InetAddress.getByName(HOST),PORT));
                // createIndexByMap(client);
                 try {
                 	// createIndexByMap(client);

                	  Document doc2 = Jsoup.connect(urlStr).get();
                	 
                     Map<String, Object> map1 = new HashMap<String,Object>();
                     map1.put("title", title);//标题
                     map1.put("insertTime", new Date());
                     map1.put("url",urlStr);//url
                     map1.put("text", doc2.text());//全文
                     if( doc2.text().length() < 160) {
                    	  map1.put("shortText", doc2.text());//全文
                     }else {
                    	  map1.put("shortText", doc2.text().substring(0, 160));//全文
                     }
                   
                     map1.put("type", map.getKey());//类型
                     IndexResponse response = client.prepareIndex("index_pro", "t_search",UUID.randomUUID().toString()).setSource(map1).get();
                     
                  
                 	
                 	
         		} catch (Exception e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         		}
                 //关闭客户端
                 client.close();
                 ///////////////
                 System.out.println("******************"+title+":"+urlStr+"********************");
                 System.out.println("==================================================================================");
               
                }
               
        	}
        	



        } catch (IOException e) {
            e.printStackTrace();
        }
    
    	
    }
public static void testEsSrarch() {


    
    /////////////
    
    //创建客户端
    TransportClient client = null;
	try {
		client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
		                         new TransportAddress(InetAddress.getByName(HOST),PORT));
		if(client != null) {
			System.out.println("#############【连接 es 成功】########### ~");
		}
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
	    //关闭客户端
	    client.close();
	    System.out.println("#############关闭 es 成功########### ~");
	}

    System.out.println("==================================================================================");
  
   
}
}