package com.es.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.fendo.dto.ModelDto;


/**
 * Elasticsearch的基本测试
 * @ClassName: ElasticsearchTest 
 * @author sunt  
 * @date 2018年01月22日 
 * @version V1.0
 */
public class ElasticsearchUtil {

    private Logger logger = Logger.getLogger(ElasticsearchUtil.class);
    public final static String HOST = "127.0.0.1";
    
    public final static int PORT = 9300;//http请求的端口是9200，
    
    /**
     * 测试Elasticsearch客户端连接
     * @Title: test1 
     * @author sunt  
     * @return void
     * @throws UnknownHostException 
     */
    @SuppressWarnings("resource")
    @Test
    public void test1() throws UnknownHostException {
        //创建客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                                 new TransportAddress(InetAddress.getByName(HOST),PORT));
        logger.info("Elasticsearch connect info:" + client.toString());
       // createIndexByMap(client);
        try {
        	// createIndexByMap(client);
        	//  queryText(client, "text", "孙俪",0); 	
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //关闭客户端
        client.close();
    }
    
    
    @Test
    public void test2() throws Exception {
    	ElasticsearchUtil.queryData("text", "孙俪",0);
    	}
    
    public static List<ModelDto> queryData(String field,String keyWrod,int pageNo) throws Exception{

        //创建客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                                 new TransportAddress(InetAddress.getByName(HOST),PORT));
        List<ModelDto> list = null;
        try {
        	// createIndexByMap(client);
        	list =  queryText(client, field, keyWrod,pageNo); 	
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //关闭客户端
        client.close();
    
    	return list;
    }
    
    public static   List<ModelDto> queryText(TransportClient client,String field,String keyWord,int pageNo) {
    	 // 通过某个字段查询,例如uid, country  
        SearchRequestBuilder builder = client.prepareSearch("index_pro").setTypes("t_search").setSearchType(SearchType.DEFAULT)  
                .setFrom(pageNo).setSize(5);  
        BoolQueryBuilder qb = null;
        if(keyWord != null && !"".equals(keyWord.trim())) {
    	   qb = QueryBuilders.boolQuery().should(new QueryStringQueryBuilder(keyWord).field(field));
        }else {
        	qb = QueryBuilders.boolQuery();
        }

        builder.setQuery(qb);  
        SearchResponse response = builder.execute().actionGet();  
       
        
        SearchHits hits =  response.getHits();
        List<ModelDto> list = new ArrayList<>();
        for(int t=0;t<hits.getHits().length;t++) {
        	Map<String, Object> dataMap = hits.getAt(t).getSourceAsMap();
        	String title_ = (String) dataMap.get("title");
        	String url_ = (String) dataMap.get("url");
        	String type_ = (String) dataMap.get("type");
        	String text_ = (String) dataMap.get("text");
        	String insertTime_ = (String) dataMap.get("insertTime");
        	String shortText_ = (String) dataMap.get("shortText");
        	list.add(new ModelDto(title_, insertTime_, url_, text_, type_,shortText_));
        }
        System.out.println("通过字段查询:" + response);  
        System.out.println("通过字段查询一共数据量:" +  response.getHits().getTotalHits()); 
     //   jsonObj.get(key)
        // JSONObject.toJavaObject(json, clazz);
        return  list;
    }
    
    /**
     * 创建索引库
     * @Title: addIndex1
     * @author sunt  
     * @return void
     * 需求:创建一个索引库为：msg消息队列,类型为：t_search,id为1
     * 索引库的名称必须为小写
     * @throws IOException 
     */
    public void createIndexByField(TransportClient client ) throws IOException {
    	 IndexResponse response = client.prepareIndex("index_pro", "t_search", "1").setSource(XContentFactory.jsonBuilder()
                 .startObject().field("userName", "张三")
                 .field("sendDate", new Date())
                 .field("msg", "你好李四")
                 .endObject()).get();
         
         logger.info("索引名称:" + response.getIndex() + "\n类型:" + response.getType()
                     + "\n文档ID:" + response.getId() + "\n当前实例状态:" + response.status());
    }
    
    /**
     * 创建索引-传入Map对象
     * @Title: addIndex3 
     * @return void
     */
    public void createIndexByMap(TransportClient client) {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("keyWord", "法制");
        map.put("insertTime", new Date());
        map.put("url", "http://www.baidu.com");
        map.put("text", "天下新闻唯快不破333");
        map.put("type", "娱乐");
        IndexResponse response = client.prepareIndex("index_pro", "t_search","6").setSource(map).get();
        
        logger.info("map索引名称:" + response.getIndex() + "\n map类型:" + response.getType()
        + "\n map文档ID:" + response.getId() + "\n当前实例map状态:" + response.status());
    }
    
    
    public void getIndex(TransportClient client) throws Exception {
        GetResponse response = client.prepareGet("index_pro", "t_search", "2")  
                .execute().actionGet();  
        System.out.println("response.getId():"+response.getId());  
        System.out.println("response.getSourceAsString():"+response.getSourceAsString());  
    }
    
    
    /**
     * 传递json对象
     * 需要添加依赖:gson
     * @return void
     */
  /*  public void createIndexByJson(TransportClient client) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", "那公顷");
        jsonObject.addProperty("sendDate", "2018-01-23");
        jsonObject.addProperty("msg","你好kekl");
        
        IndexResponse response = client.prepareIndex("qq", "t_search").setSource(jsonObject, XContentType.JSON).get();
        
        logger.info("jsonObject索引名称:" + response.getIndex() + "\n jsonObject类型:" + response.getType()
        + "\n jsonObject文档ID:" + response.getId() + "\n当前实例jsonObject状态:" + response.status());
    }*/
}