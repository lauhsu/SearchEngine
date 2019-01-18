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
 * ��ȡ���¹ؼ���
 * 
 * @author anwj
 * 
 */
public class WordUtil {
	/** �������� */
	static String keyWord = "Ц���˳������ϡ���ý�š��ˣ�����ؿɲ�����СƷ���������Ա��Ե����ݵĶ���ϲ�硶��ý�š�����1��13���ڱ�\" + \r\n" + 
			"   \"�������ײ������У��˳��������Ϊ��˵������ŵ��ȳ��ġ���ʱ����ý�š����������롰̨���һ������Ů2����ͥ������һ������˿��Ϯ��\" + \r\n" + 
			"   \"������׷���ǡ�������������������Զ�����γ�ΰ���������������Ҳ�����������조Ц�ϡ�����ý�š�Χ���Ŷ�������������\" + \r\n" + 
			"   \"��ġ�ȫ�������������չ�����˵�����Ķ������ۿ�������ӿ�ġ������󳱡��̻����ϣ���ƾ��һ�����쿪����ҵ������\" + \r\n" + 
			"   \"���ڶ���������������Ŵ󼪣��˿�ӯ�ţ������Ҫ��ȴҲǧ��ٹ֣��ݽ�Ů��լ�С�С�ϰ��ī�ǳ���չ����һϵ����������\" + \r\n" + 
			"   \"��Ц�ֲ�ʧ����ľ��ʹ��¡����е�һ�󿴵�����Ů���䡰���С��Ĵ��䣬��ͥ���˳�������һ�Ի�ϲԩ�ҡ���ͥ��ʾ���������ˡ�\" + \r\n" + 
			"   \"����в�ࡢ�����о��룬��ò���Եȡ������˳���̸�����������趨ʱ��Ϊ������ͥ�����Ľ�ɫ���ر�������ɰ���������Ů��'�͡�\" + \r\n" + 
			"   \"С����'��������������µ��趨��������ͥ�Ƿǳ����ʵ���ѡ�������⣬�þ�Ҳ���˳����̡����˷���󡷡����������ˮ��ӨӨ���졷\" + \r\n" + 
			"   \"��һ���ڶ���֮����Ĵ��Ե����ݵ�ϲ����Ʒ���˳�����ʾ��ȫ��ͨ����ý�š��������ְҵ���ӽǣ�չʾ�ŵ���������ֻ�����ֵ�ۣ�������\" + \r\n" + 
			"   \"�ƻ������ݽ�Ů������е�������������ʱ�����⡣(���� ������)�������������Ľ��������߸��˹۵㣬�뻷�����޹ء���ԭ�����Լ�����\" + \r\n" + 
			"   \"�������ֺ�����δ����վ֤ʵ���Ա����Լ�����ȫ�����߲������ݡ����ֵ���ʵ�ԡ������ԡ���ʱ�Ա�վ�����κα�֤���ŵ������߽����ο���\" + \r\n" + 
			"   \"�������к�ʵ�������";


	
	/** ��ȡ�ؼ��ָ��� */
	private final static Integer NUM = 5;
	/** ��ȡ�ؼ����ڼ����������ϵ����� */
	private final static Integer QUANTITY = 1;

	/**
	 * ����String���͵����£�������ȡ���ʷ���list��
	 * 
	 * @param article
	 * @param a
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static List<String> extract(String article, Integer a) throws IOException {
		List<String> list = new ArrayList<String>(); // ����һ��list�����ս�Ҫ��ȡ��������
		IKAnalyzer analyzer = new IKAnalyzer(); // ��ʼ��IKAnalyzer
		analyzer.setUseSmart(true); // ��IKAnalyzer���ó����ܽ�ȡ
		TokenStream tokenStream = // ����tokenStream����(��ȡ���µ��ַ���)
				analyzer.tokenStream("", new StringReader(article));
		 tokenStream.reset();
		while (tokenStream.incrementToken()) { // ѭ����ý�ȡ�����ĵ���
			CharTermAttribute charTermAttribute = // ת��Ϊchar����
					tokenStream.getAttribute(CharTermAttribute.class);
			String keWord = charTermAttribute.toString(); // ת��ΪString����
			if (keWord.length() > a) { // �жϽ�ȡ�ؼ����ڼ����������ϵ�����(Ĭ��Ϊ2����������)
				list.add(keWord); // �����ջ�õĵ��ʷ���list������
			}
		}
		return list;
	}

	/**
	 * ��list�еļ���ת����Map�е�key��valueΪ����Ĭ��Ϊ1
	 * 
	 * @param list
	 * @return
	 */
	private static Map<String, Integer> list2Map(List<String> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : list) { // ѭ����õ�List����
			if (list.contains(key)) { // �ж�����������Ƿ���ڸ��ַ���
				map.put(key, map.get(key) == null ? 1 : map.get(key) + 1);
			} // �����л�õ��ַ�������map��key����
		} // ��������value�Ƿ���ֵ��������+1����
		return map;
	}

	/**
	 * ��ȡ�ؼ��ַ���
	 * 
	 * @param article
	 * @param a
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public static String[] getKeyWords(String article, Integer a, Integer n) throws IOException {
		List<String> keyWordsList = extract(article, a); // ������ȡ���ʷ���
		Map<String, Integer> map = list2Map(keyWordsList); // listתmap���ƴ���
		// ʹ��Collections�ıȽϷ������ж�map��value������
		ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		if (list.size() < n)
			n = list.size(); // �����ĳ��ȣ������õ�null���ַ�
		String[] keyWords = new String[n]; // ���ý�Ҫ����Ĺؼ�������ռ�
		for (int i = 0; i < list.size(); i++) { // ѭ������������
			if (i < n) { // �жϸ���
				keyWords[i] = list.get(i).getKey(); // ���ùؼ��ֽ�������
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