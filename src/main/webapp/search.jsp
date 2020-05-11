<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/12
  Time: 1:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>search</title>
</head>
<body>
    <h1>获取solr数据展示页面</h1>

    <table border="1">
        <tr>
            <th>id</th>
            <th>title</th>
            <th>sellPoint</th>
            <th>price</th>
            <th>image</th>
            <th>catName</th>
            <th>itemDesc</th>
        </tr>
        <c:forEach var="data" items="${datas}">
            <tr>
                <td>${data.id}</td>
                <td>${data.title}</td>
                <td>${data.sellPoint}</td>
                <td>${data.price}</td>
                <td>${data.image}</td>
                <td>${data.catName}</td>
                <td>${data.itemDesc}</td>
            </tr>
        </c:forEach>
        
    </table>
</body>
</html>
