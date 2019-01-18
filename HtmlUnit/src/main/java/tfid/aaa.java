package tfid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

public class aaa {

		
		
		
		

public static Vector<String> DATA=new Vector<String>();
	public static Vector<String> divide(String s,String delim)//��һ�����ַ������ڶ����Ƿָ���
	{
		s=RemoveChar(s,',');//ȥ��������
		s=RemoveChar(s,'.');//ȥ��������
		s=s.toLowerCase();
		Vector<String> word=new Vector<String>();
		StringTokenizer st;
		if(delim.isEmpty())
		{
			st= new StringTokenizer(s);
		}
		else
		{
			st= new StringTokenizer(s,delim);
		}
		while(st.hasMoreElements())
		{
			String token=st.nextToken();//��ȡ��һ������
			token=ToSingle(token);//ת��Ϊ����
			word.add(token);
		}
		
		return word;

	}
	
	//ʹ������ʵ���зִ�
	public static Vector<String> dividewithdict(String s,String delim)
	{
		int deep=3;//��ʾ�ֵ��е�ÿһ����󵥴����Ƕ���
		
		if(DATA.size()==0)//û�м����ֵ�ʱ��ʼ����
		{
			loadDict("D:\\dic.txt");//�����ֵ��ļ�
		}
		
		Vector<String> word=divide(s,delim);
		
		String tmp="";
		Vector<String> nword=new Vector<String>();
		boolean flag=true;
		boolean iswrap=true;
		for(int i=0;i<word.size();)
		{
			
			for(int j=i;j<(i+deep>word.size()?word.size():i+deep);j++)
			{
				tmp+=" "+word.get(j);
				if(iswrap)
				{
					tmp=tmp.substring(1,tmp.length());
					iswrap=false;
				}
				if(DATA.contains(tmp))
				{
					//nword.add(tmp);
					flag=false;
					i+=j-i+1;
					break;
				}
			}
			if(flag)
			{
				nword.add(word.get(i));
				i++;
			}
			tmp="";
			iswrap=true;
			flag=true;
		}
		return nword;

	}
	//תΪСд
	public static String ChangeToLower(String s)
	{
		return s.toLowerCase();
	}
	//�Ƴ�������� 
	public static String RemoveChar(String s,char token)
	{
		return s.replace(token, ' ');
	}
	//����������תΪ��������
	public static String ChangeDoubleToOne(String token)
	{
		 if (token.equalsIgnoreCase("feet"))
		 {
			 token = "foot";
		 } 
		 else if (token.equalsIgnoreCase("geese")) 
		 {
			 token = "goose";
		 } 
		 else if (token.equalsIgnoreCase("lice")) 
		 {
			 token = "louse";
		 } 
		 else if (token.equalsIgnoreCase("mice")) 
		 {
			 token = "mouse";
		 } 
		 else if (token.equalsIgnoreCase("teeth")) 
		 {
			 token = "tooth";
		 } 
		 else if (token.equalsIgnoreCase("oxen")) 
		 {
			 token = "ox";
		 } 
		 else if (token.equalsIgnoreCase("children")) 
		 {
			 token = "child";
		 } 
		 else if (token.endsWith("men")) 
		 {
			 token = token.substring(0, token.length() - 3)+"man";
		 } 
		 else if (token.endsWith("ies")) {
		 token = token.substring(0, token.length() - 3)+"y";
		 } 
		 else if (token.endsWith("ves")) 
		 {
			 if (token.equalsIgnoreCase("knives")|| token.equalsIgnoreCase("wives")|| token.equalsIgnoreCase("lives")) 
		 {
			 token = token.substring(0, token.length() - 3)+"fe";
		 } 
		 else 
		 {
			 token = token.substring(0, token.length() - 3)+"f";
			 }
		 } 
		 else if (token.endsWith("oes") || token.endsWith("ches")|| token.endsWith("shes") || token.endsWith("ses")|| token.endsWith("xes")) 
		 {
			 token = token.substring(0, token.length() - 2);
		 } 
		 else if (token.endsWith("s")) 
		 {
			 token = token.substring(0, token.length() - 1);
		 }
		 return token;
	}
	//����һ��תΪ�����ķ���
	public static String ToSingle(String token)
	{
		return Inflector.getInstance().singularize(token);
	}
	
	
	public static void loadDict(String path)
	{
		try {
			File file=new File(path);
			if(file.isFile() && file.exists())
			{ 
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					DATA.add(lineTxt.trim());
				}

				read.close();
			}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
	}
		
		

}
