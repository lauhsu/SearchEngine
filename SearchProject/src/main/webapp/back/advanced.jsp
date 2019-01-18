<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>检索系统</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/advanced.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container">
	<div id="hd" class="ue-clear">
    	<div class="logo"></div>
        <div class="inputArea">
        	<input type="text" class="searchInput" />
            <input type="button" class="searchButton" />
            <a class="back" href="index.jsp">返回主页</a>
        </div>
    </div>
    <div class="divsion"></div>
	<div id="bd">
        <div id="main">
        	<div class="subfield">高级搜索</div>
            <div class="subfieldContent">
            	<!--搜索范围-->
                <dl class="ue-clear advanceItem">
                	<dd>
                    	<label>搜索范围</label>
                        <span>选择要搜索的范围</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="scope" type="checkbox" checked="checked">
                            <span class="text">标题</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="scope">
                            <span class="text">正文</span>
                        </span>
                    </dt>
                </dl>
                
                <!--搜索关键字-->
                <dl class="ue-clear advanceItem keyWords">
                	<dd>
                    	<label>搜索关键字</label>
                        <div class="tips">
                        	<p class="tip">包含以下<span class="impInfo">全部</span>的关键</p>
                            
                        </div>
                    </dd>
                    <dt class="fillInArea">
                    	<p><input type="text" /></p>
               
                    </dt>
                </dl>
                
                <!--文章类型-->
                <dl class="ue-clear advanceItem">
                	<dd>
                    	<label>文章类型</label>
                        <span>指定的文章类型</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="type" type="checkbox" checked="checked">
                            <span class="text">资讯</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="type">
                            <span class="text">设计</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">界面设计</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">设计师</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">网页</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">图标库</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">人才库</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">自定义</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">其他</span>
                        </span>
                    </dt>
                </dl>
                
               
                
                
                <div class="button">
                	<input type="button" class="search" value="立刻搜索" onclick="javascript:window.location='result.jsp'" />
                </div>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
    
    
</div>
<div id="foot">Copyright &copy;qy.com 版权所有  E-mail:admin@qy.com</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript">
	$('.defineRadio input[type=radio]').click(function(e) {
        if($(this).prop('checked')){
			$('.define').show();
		}
    });
	
	$('.time input[type=radio]').click(function(){
		if(!$(this).parent().hasClass('defineRadio')){
			$('.define').hide();
		}	
	});
	
	$('.part input[type=checkbox]:gt(3)').parent().hide();
	$('.part .more').toggle(function(e) {
		$(this).addClass('show').find('.text').text('收起');
        $('.part input[type=checkbox]:gt(3)').parent().show();
    },function(){
		$(this).removeClass('show').find('.text').text('更多');
		$('.part input[type=checkbox]:gt(3)').parent().hide();	
	});
	
	setHeight();
	$(window).resize(function(){
		setHeight();	
	});
	
	function setHeight(){
		if($('#container').outerHeight() < $(window).height()){
			$('#container').height($(window).height()-33);
		}	
	}
</script>
</html>