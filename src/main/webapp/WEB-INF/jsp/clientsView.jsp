<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Клиенты</title>
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

    <div>
        <table>
            <tr>
                <th>Имя клиента</th>
                <th>Баланс</th>
            </tr>
            <c:forEach items="${clientAndBalance}" var="pair">
                <tr>
                    <td>
                        <a href="/clients/${pair.getKey().getId()}">${pair.getKey().getName()}</a>
                    </td>
                    <td>${pair.getValue()}</td>
                </tr>
            </c:forEach>

        </table>
    </div>

    <div>
        <form action="/clients/add">
            <input type="submit" value="Добавить клиента">
        </form>
    </div>

</body>
</html>
