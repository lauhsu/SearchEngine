#coding: utf-8
# 新闻类定义
class News(object):
    """
           title:title of news  新闻的标题
           content:content of news 新闻的内容
           author:author of news 新闻发布作者
           date:published time of news 新闻发布时间
           url:from where 新闻的出处
           keywords:keywords of news 新闻的关键字
    """
    def __init__(self):
        self.title = None
        self.content = None
        self.author = None
        self.date = None
        self.url = None
        self.keywords=None



