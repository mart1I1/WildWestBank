<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Fedor
  Date: 11.11.2018
  Time: 15:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить клиента</title>
    <link href=${pageContext.request.contextPath}/resources/style.css rel="stylesheet">
</head>
<body>

    <div class="link">
        <ul class="hr">
            <li><a href="/clients">Клиенты</a></li>
            <li><a href="/accounts/transfer">Переводы</a></li>
            <li><a href="/transactions">Транзакции</a></li>
        </ul>
    </div>

    <div class="noneLastBorder">
        <form:form modelAttribute="clientModel" method="post" action="/clients/add">
            <table >
                <caption>Добавить нового клиента:</caption>
                <tr>
                    <td>Имя</td>
                    <td><form:input path="name" /></td>
                    <td><form:errors path="name" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Адрес</td>
                    <td><form:input path="address" /></td>
                    <td><form:errors path="address" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Возраст</td>
                    <td><input type="number" name="age"></td>
                    <td><form:errors path="age" cssClass="error"/></td>
                </tr>
                <tr>
                    <td class="last"></td>
                    <td class="last"><input type="submit" value="Добавить"/></td>
                    <td><form:errors path="" cssClass="error"/></td>
                </tr>
            </table>
        </form:form>
    </div>

</body>
</html>
