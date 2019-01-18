<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<!-- EL表达式生效 -->  
<%@ page isELIgnored="false" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>Insert title here</title>  
</head>  
<body>  
    <table border="1">  
        <tr>  
            <th>姓名</th>  
        </tr> 
       <c:forEach items="${user}" var="p">  
            <tr>  
                <td>${p.name}</td>  
            </tr>  
        </c:forEach>
    </table>  
</body>  
</html>  