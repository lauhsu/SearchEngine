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
public class GetKeyWordUtil {
	/** 测试文章 */
	static String keyWord = "Tesco faces record ?4bn equal pay claim By Kamal Ahmed Economics editor 7 February 2018 Share this with Facebook Share this with Twitter Share this with Messenger Share this with Messenger Share this with Email Share Share this with These are external links and will open in a new window Email Share this with Email Facebook Share this with Facebook Messenger Share this with Messenger Messenger Share this with Messenger Twitter Share this with Twitter Pinterest Share this with Pinterest WhatsApp Share this with WhatsApp LinkedIn Share this with LinkedIn Copy this link http://www.bbc.com/news/business-42968342 Read more about sharing. These are external links and will open in a new window Close share panel Media playback is unsupported on your device Media captionPam Jenkins says the men's and women's jobs are of \"equal value\" Tesco is facing Britain's largest ever equal pay claim and a possible bill running to ?4bn. Thousands of women who work in Tesco stores could receive back pay totalling ?20,000 if the legal challenge demanding parity with men who work in the company's warehouses is successful. Lawyers say hourly-paid female store staff earn less than men even though the value of the work is comparable. Tesco said it worked hard to ensure all staff were paid \"fairly and equally\". What you need to know about the gender pay gap Pay gap 'widens as women get older' Paula Lee, of Leigh Day solicitors told the BBC it was time for Tesco to tackle the problem of equal pay for work of equal worth. Her firm has been contacted by more than 1,000 Tesco staff and will this week take the initial legal steps for 100 of them. The most common rate for women is ?8 an hour whereas for men the hourly rate can be as high as ?11 an hour, she added. Cleaners vs Binmen - What is \"work of comparable value\"? Since 1984 workers doing jobs that require comparable skills, have similar levels of responsibility and are of comparable worth to the employer, should also be rewarded equally, according to the law. Thus if you are a cleaner, lugging mops and buckets up and down staircases, you may have a case for being paid the same as co-workers collecting rubbish bins. It doesn't matter whether the cleaner or the shop floor worker is male or female, they may still have a case to see their pay upped to match colleagues doing other jobs. But in practice many of the poorer paid jobs have been done by women. Key moments in the fight for equal pay Ms Lee said it was a problem that had been \"hiding in plain sight\" for years and that while there was no suggestion that Tesco had intentionally been underpaying women, historical divisions between male and female roles had led to the pay differential. \"We believe an inherent bias has allowed store workers to be underpaid over many years,\" she said. \"In terms of equal worth to the company there really should be no argument that workers in stores, compared to those working in the depots, contribute at least equal value to the vast profits made by Tesco. \"The law has been there since 1984 - you can compare with a different job. \"That's 34 years to put your house in order; that's 34 years of having the advantage of paying unequally, 34 years of you making pay decisions and making financial decisions and 34 years hiding what is in open sight.\" Significant bill? Leigh Day said that up to 200,000 supermarket workers could be affected, the majority of them women. Initial claims have been lodged with the conciliation service, ACAS - the first stage in what is likely to be a lengthy legal process through the employment tribunal system which could last several years. If even a small proportion of the women are successful, the bill for Tesco would be significant. Image copyright Reuters Birmingham City Council is now liable for over ?1bn pounds in payments after settling an equal pay claim from women employed as cleaners, cooks and carers. Their pay was below men in comparable jobs such as bin collectors and road workers. Leigh Day is also fighting similar actions against rival supermarket groups Asda and Sainsbury's on behalf of shop floor workers. In 2016, an employment tribunal ruled that 9,500 women who work at Asda on check-outs or stacking shelves, could compare themselves to higher paid men who work at warehouses. Asda is currently appealing the ruling. Tesco said that all their staff could progress equally and were paid fairly, whatever their gender or background. \"We are unable to comment on a claim that we have not received,\" a spokeswoman said. \"Tesco has always been a place for people to get on in their career, regardless of their gender, background or education, and we work hard to make sure all our colleagues are paid fairly and equally for the jobs they do.\" 'Discrepancies' Two workers for Tesco told the BBC they wanted fair treatment, arguing that their jobs in the stores were as demanding as warehouse jobs. Pam Jenkins has been working for Tesco for 26 years. \"I think that we should be brought up to their [the men's] level,\" she told me. \"Obviously the jobs are slightly different but to put it bluntly they are of equal value. \"We deal with customers, they [the men] don't have to. We load, we take the stock and we load the stock, they take it off the lorry and we load it onto the shelves. \"Women have been fighting for equal rights and their voice to be heard for 100 years, we are not just doing it for us, there are many people out there. \"We are just trying to put things right and it's a shame we are still having to fight in this day and age.\" Kim Element has been working for Tesco for 23 years. \"Although we think we have equal rights, there are times where there are discrepancies and you can't explain them,\" she said. \"And I think Tesco's are one of many companies that aren't addressing the fact that women seem to still be paid less.\" Ms Lee said that Tesco was a good employer, signing up to a number of gender equality projects over a number of years. But she said the company - along with many others - was still failing to reward people equally. Image copyright Getty Images How do you compare jobs? The criteria that the courts use to compare jobs in these cases aren't precisely pre-defined. But the starting point is to send in an independent expert to do a job evaluation study. That expert scores each of the roles under consideration including considerations such as how demanding they are physically, mentally and emotionally. If they involve working in the cold, or facing customer complaints, or if they require technical know-how, qualifications, responsibility for other staff or enhanced communication skills, that will push up their value. If the roles are comparable the case can then go to an employment tribunal, but there's still scope for the employer to argue that there's another reason the pay differs: if workers in those jobs have been there longer, or have more professional experience for example. While individuals can bring cases, It is much easier to bring such claims if you're part of a group. \"You need momentum behind the claim. That opens the doors,\" says Leigh Day's spokesperson. \"It forces them to be transparent and give information to a tribunal which you may not get on a one to one basis.\"\r\n" + 
			"";	
	
	
	/** 获取关键字个数 */
	private final static Integer NUM = 6;
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