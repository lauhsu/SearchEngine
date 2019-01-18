import org.thunlp.text.classifiers.BasicTextClassifier;
import org.thunlp.text.classifiers.ClassifyResult;
import org.thunlp.text.classifiers.LinearBigramChineseTextClassifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DemoMain {
	public static String runLoadModelAndUse(String text) {
		// 新建分类器对象
		BasicTextClassifier classifier = new BasicTextClassifier();
		
		// 设置分类种类，并读取模型
		classifier.loadCategoryListFromFile("E:\\网页分类\\news_model\\category");
		classifier.setTextClassifier(new LinearBigramChineseTextClassifier(classifier.getCategorySize()));
		classifier.getTextClassifier().loadModel("E:\\网页分类\\news_model");
		
		// 之后就可以使用分类器进行分类
		String str = text;
		int topN = 3;  // 保留最有可能的3个结果
		ClassifyResult[] result = classifier.classifyText(str, topN); 
		/*for (int i = 0; i < topN; ++i) {
			// 输出分类编号，分类名称，以及概率值。
			System.out.println(result[i].label + "\t" + 
								classifier.getCategoryName(result[i].label) + "\t" + 
								result[i].prob); 
		}*/
		return classifier.getCategoryName(result[0].label);
	}
	
	
	public static void main (String args[]) {
		try {
		      Class.forName("com.mysql.jdbc.Driver");     //鍔犺浇MYSQL JDBC椹卞姩绋嬪簭   
		      System.out.println("Success loading Mysql Driver!");
		    }
		    catch (Exception e) {
		      System.out.print("Error loading Mysql Driver!");
		      e.printStackTrace();
		    }
		try {
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/news","root","123456");
		    System.out.println("Success connect Mysql server!");
		    Statement stmt1 = connect.createStatement();
		    Statement stmt2 = connect.createStatement();
		    ResultSet rs = stmt1.executeQuery("select * from data");
		    String text = null;
		    String newsType = null;
		    while (rs.next()) {
		    	text = rs.getString("title")+" "+rs.getString("content");
		        newsType = runLoadModelAndUse(text);
		        stmt2.executeUpdate("update data set type='"+newsType+"'where id="+rs.getString("id")+"");
		    }
		}
		catch (Exception e) {
			System.out.print("get data error!");
		    e.printStackTrace();
		}
	}
	
}
