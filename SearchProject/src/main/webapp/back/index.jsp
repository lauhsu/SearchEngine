<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>搜索引擎</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/index.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container">
	<div id="bd">
        <div id="main">
        	<h1 class="title">
            	<div class="logo large"></div>
            </h1>
            <div class="nav ue-clear">
            	<ul class="searchList">
                    <li class="searchItem current">资讯</li>
                    <li class="searchItem">设计</li>
                    <li class="searchItem">娱乐</li>
                    <li class="searchItem">新闻</li>
                    <li class="searchItem">体育</li>
                    
                </ul>
            </div>
            <div class="inputArea">
            	<input type="text" class="searchInput" />
                <input type="button" class="searchButton" onclick="javascript:window.location='result.jsp'" />
                <a class="advanced" href="advanced.jsp">高级搜索</a>
                <ul class="dataList">
                	<li>如何学好设计</li>
                    <li>界面设计</li>
                    <li>UI设计培训要多少钱</li>
                    <li>设计师学习</li>
                    <li>哪里有好的网站</li>
                </ul>
            </div>
            
            <div class="historyArea">
            	<p class="history">
                	<label>热门搜索：</label>
                    <a href="javascript:;">UI设计</a>
                    <a href="javascript:;">界面设计</a>
                    <a href="javascript:;">手机界面</a>
                    <a href="javascript:;">交互</a>
                    <a href="javascript:;">图标</a>
                    <a href="javascript:;">UI素材</a>
                    <a href="javascript:;">教程</a>
                </p>
                <p class="history">
    
                </p>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
    
    <div class="foot">
    	<div class="wrap">
            <div class="copyright">Copyright &copy;uimaker.com 版权所有  E-mail:admin@uimaker.com</div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript">
	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	});
	
	// 联想下拉显示隐藏
	$('.searchInput').on('focus', function(){
		$('.dataList').show()
    });
	
	// 联想下拉点击
	$('.dataList').on('click', 'li', function(){
		var text = $(this).text();
		$('.searchInput').val(text);
		$('.dataList').hide()
	});
	
	hideElement($('.dataList'), $('.searchInput'));
</script>
</html>