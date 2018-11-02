<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Fedor
  Date: 01.11.2018
  Time: 13:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Clients</title>
</head>
<body>
    <c:forEach items="${clients}" var="client">
        ${client.toString()}
        <br>
    </c:forEach>
</body>
</html>
