#coding:utf-8
import requests
from bs4 import BeautifulSoup
import pymysql
import News
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
url_set = set()  # url集合
url_old = set()  # 爬过的url集合
try:
    conn = pymysql.connect(host='localhost', user='root', passwd='', db='web_data', port=3306, charset='utf8')
    cur = conn.cursor()
    cur.execute('select * from url')
    urls_data = cur.fetchall()
    for i in urls_data:
        url = i[1]
        url_set.add(url)
    while len(url_set) != 0:
            try:
                # 获取链接
                url = url_set.pop()
                if url not in url_old:
                    # 请求新闻的URL，获取其text文本
                    wbdata = requests.get(url)
                    wbdata.encoding = 'utf-8'
                    #print(wbdata.text)
                    # 对获取到的文本进行解析
                    soup = BeautifulSoup(wbdata.text, 'lxml')
                    #print(soup.text)
                    # 从解析文件中通过select选择器定位指定的元素，返回一个列表
                    news_titles = soup.select('.news-item')
                    # 对返回的列表进行遍历
                    for n in news_titles:
                       # 获取信息
                       h2 = n.select('h2')
                       if len(h2) > 0:
                        article = News.News()
                        title = h2[0].text  # 标题信息
                        article.title=title.encode("utf-8")
                        #print("title:" + article.title)
                        article.url =h2[0].select('a')[0]['href']  # URL信息
                        #print("url:" + article.url)
                        date = n.select('.time')[0].text # 日期信息
                        article.date=date.encode("utf-8")
                        #print("date:" + article.date)
                        wbdata0 = requests.get(article.url)
                        wbdata0.encoding = 'utf-8'
                        soup0 = BeautifulSoup(wbdata0.text, 'lxml')
                        content = soup0.select('.article')
                        article.content=content[0].text
                        # print("content:" + article.content)
                        keywords=soup0.select('.keywords')
                        article.keywords=keywords[0].text
                        # print("keywords:" + article.keywords)
                        article.author = soup0.select('.show_author')[0].text # 作者信息
                        #print("author:"+article.author)
                        url_set.add(article.url)
                        #sql = 'insert into data (title,content,author,date,url) values(%s,%s,%s,%s,%s)'
                        #param = (article.title,article.content,article.author,article.date,article.url )
                        #cur.execute(sql,param)
                        cur.execute('insert into data(title,content, author,date,url,keywords)  values(%s,%s,%s,%s,%s,%s)',
                                    (article.title,article.content,article.author,article.date,article.url,article.keywords))
                url_old.add(url)
            except Exception as e:
                print(e)
            continue;
    cur.execute('select * from data ')
    news_data= cur.fetchall()
    for j in news_data:
        print("title:" +str(j[1]) + '\n' + 'content:' + j[2] + '\n' + "author:" + str(j[3]) + '\n' + "date:" + j[
          4] + '\n' + "url:" + j[5] + 'keywords:' + str(j[6]))
        print ("*****************************************************************")
    conn.commit()
    cur.close()
    conn.close()
except Exception:
    print('error')