#coding=utf-8
import re  # 正则表达式
import bs4  # Beautiful Soup 4 解析模块
import urllib  # 网络访问模块
import News   #自己定义的新闻结构
import pymysql
import sys   #1解决不同页面编码问题
reload(sys)                         # 2
sys.setdefaultencoding('utf-8')     # 3

# 从首页获取所有链接
def GetAllUrl(home):
    html = urllib.urlopen(home).read()
    soup = bs4.BeautifulSoup(html, 'html.parser')
    pattern ='http://(.*?)/(.*?)/(.*?)/(.*?)'
    links = soup.find_all('a', href=re.compile(pattern))
    for link in links:
        url_set.add(link['href'])

def GetNews():
    while len(url_set) != 0:
        try:
            # 获取链接
            url = url_set.pop()
            url_old.add(url)
            # 获取信息
            article = News.News()
            article.url = url  # URL信息
            html = urllib.urlopen(article.url).read()
            soup = bs4.BeautifulSoup(html, 'html.parser')
            article.title = soup.find('title').get_text()  # 标题信息
            keywords='keywords'
            res0 = re.compile(keywords)
            if soup.find('meta', {'name': res0}).__getitem__('name') == "keywords":
                article.keywords = soup.find('meta', {'name': res0}).__getitem__('content')  # 作者
            else:
                article.keywords = ""
            author = 'author'
            res = re.compile(author)
            if soup.find('meta', {'name': res}).__getitem__('name') == "author":
                article.author = soup.find('meta', {'name': res}).__getitem__('content')  # 作者
            else:
                article.author = ""
            published_time= 'publishdate'
            res1 = re.compile(published_time)
            if soup.find('meta', {'name': res1}).__getitem__('name') == "publishdate":
                article.date = soup.find('meta', {'name': res1}).__getitem__('content')  # 作者
            else:
                article.date = ""
            article.content = soup.find('div', {'class': 'article'}).get_text()
            SaveNews(article)
        except Exception as e:
            print(e)
            continue

def SaveNews(object):
    try:
        conn = pymysql.connect(host='localhost', user='root', passwd='', db='web_data', port=3306, charset='utf8')
        cur = conn.cursor()
        cur.execute('insert into data(title,content, author,date,url,keywords)  values(%s,%s,%s,%s,%s,%s)',
                (object.title, object.content, object.author, object.date, object.url, object.keywords))
        conn.commit()
        cur.close()
        conn.close()
    except Exception:
        print('error')

def showNews():
    try:
        conn = pymysql.connect(host='localhost', user='root', passwd='', db='web_data', port=3306, charset='utf8')
        cur = conn.cursor()
        cur.execute('select * from data ')
        news_data = cur.fetchall()
        for j in news_data:
            print("title:" + str(j[1]) + '\n' + 'content:' + j[2] + '\n' + "author:" + str(j[3]) + '\n' + "date:" + j[
                4] + '\n' + "url:" + j[5] + 'keywords:' + str(j[6]))
            print ("*****************************************************************")
    except Exception:
        print('error')


url_set = set()  # url集合
url_old = set()  # 爬过的url集合
home ='http://jschina.com.cn/' # 起始位置
GetAllUrl(home)
print(url_set)
GetNews()
#showNews()

