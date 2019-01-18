package com.qy.cn;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 获取文章关键字
 * 
 * @author anwj
 * 
 */
public class WordUtil {
	/** 测试文章 */
	static String keyWord = "笑星潘长江当上“男媒婆”了，但这回可不是演小品——由他自编自导自演的都市喜剧《男媒婆》将于1月13日在北\" + \r\n" + 
			"   \"京卫视首播。剧中，潘长江变身成为能说会道、古道热肠的“新时代男媒婆”丁二春，与“台湾第一酒窝美女2”张庭上演了一出“屌丝逆袭”\" + \r\n" + 
			"   \"的浪漫追爱记。李明启、李文启、冯远征、任程伟、马丽、徐等明星也齐齐上阵制造“笑料”《男媒婆》围绕着丁二春和他所开\" + \r\n" + 
			"   \"办的“全成热恋”婚介所展开。人到中年的丁二春眼看来势汹涌的“婚恋大潮”商机不断，想凭借一张巧嘴开创事业和人生\" + \r\n" + 
			"   \"“第二春”。婚介所开张大吉，顾客盈门，提出的要求却也千奇百怪，拜金女、宅男、小老板粉墨登场，展开了一系列令人捧腹\" + \r\n" + 
			"   \"大笑又不失温情的精彩故事。剧中的一大看点是美女搭配“丑男”的搭配，张庭与潘长江成了一对欢喜冤家。张庭表示，剧中两人“\" + \r\n" + 
			"   \"身高有差距、年龄有距离，相貌不对等”。而潘长江谈到这种主角设定时认为：“张庭以往的角色都特别独立、可爱，而‘大女人'和‘\" + \r\n" + 
			"   \"小男人'正是我俩这对情侣的设定，所以张庭是非常合适的人选。”此外，该剧也是潘长江继《能人冯天贵》、《清凌凌的水蓝莹莹的天》\" + \r\n" + 
			"   \"第一、第二部之后第四次自导自演的喜剧作品。潘长江表示，全剧通过“媒婆”这个特殊职业的视角，展示着当代社会种种婚恋价值观，涵盖了\" + \r\n" + 
			"   \"黄昏恋、拜金女、凤凰男等诸多引发热议的时代话题。(记者 尹春芳)免责声明：本文仅代表作者个人观点，与环球网无关。其原创性以及文中\" + \r\n" + 
			"   \"陈述文字和内容未经本站证实，对本文以及其中全部或者部分内容、文字的真实性、完整性、及时性本站不作任何保证或承诺，请读者仅作参考，\" + \r\n" + 
			"   \"并请自行核实相关内容";


	
	/** 获取关键字个数 */
	private final static Integer NUM = 5;
	/** 截取关键字在几个单词以上的数量 */
	private final static Integer QUANTITY = 1;

	/**
	 * 传入String类型的文章，智能提取单词放入list中
	 * 
	 * @param article
	 * @param a
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static List<String> extract(String article, Integer a) throws IOException {
		List<String> list = new ArrayList<String>(); // 定义一个list来接收将要截取出来单词
		IKAnalyzer analyzer = new IKAnalyzer(); // 初始化IKAnalyzer
		analyzer.setUseSmart(true); // 将IKAnalyzer设置成智能截取
		TokenStream tokenStream = // 调用tokenStream方法(读取文章的字符流)
				analyzer.tokenStream("", new StringReader(article));
		 tokenStream.reset();
		while (tokenStream.incrementToken()) { // 循环获得截取出来的单词
			CharTermAttribute charTermAttribute = // 转换为char类型
					tokenStream.getAttribute(CharTermAttribute.class);
			String keWord = charTermAttribute.toString(); // 转换为String类型
			if (keWord.length() > a) { // 判断截取关键字在几个单词以上的数量(默认为2个单词以上)
				list.add(keWord); // 将最终获得的单词放入list集合中
			}
		}
		return list;
	}

	/**
	 * 将list中的集合转换成Map中的key，value为数量默认为1
	 * 
	 * @param list
	 * @return
	 */
	private static Map<String, Integer> list2Map(List<String> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : list) { // 循环获得的List集合
			if (list.contains(key)) { // 判断这个集合中是否存在该字符串
				map.put(key, map.get(key) == null ? 1 : map.get(key) + 1);
			} // 将集中获得的字符串放在map的key键上
		} // 并计算其value是否有值，如有则+1操作
		return map;
	}

	/**
	 * 提取关键字方法
	 * 
	 * @param article
	 * @param a
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public static String[] getKeyWords(String article, Integer a, Integer n) throws IOException {
		List<String> keyWordsList = extract(article, a); // 调用提取单词方法
		Map<String, Integer> map = list2Map(keyWordsList); // list转map并计次数
		// 使用Collections的比较方法进行对map中value的排序
		ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		if (list.size() < n)
			n = list.size(); // 排序后的长度，以免获得到null的字符
		String[] keyWords = new String[n]; // 设置将要输出的关键字数组空间
		for (int i = 0; i < list.size(); i++) { // 循环排序后的数组
			if (i < n) { // 判断个数
				keyWords[i] = list.get(i).getKey(); // 设置关键字进入数组
			}
		}
		return keyWords;
	}

	/**
	 * 
	 * @param article
	 * @return
	 * @throws IOException
	 */
	public static String[] getKeyWords(String article) throws IOException {
		return getKeyWords(article, QUANTITY, NUM);
	}

	public static void main(String[] args) {
		try {
			String[] keywords = getKeyWords(keyWord);
			for (int i = 0; i < keywords.length; i++) {
				System.out.println(keywords[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}