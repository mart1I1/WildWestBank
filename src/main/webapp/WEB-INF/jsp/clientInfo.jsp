<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ClientInfo</title>
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
            <caption>Информация о клиенте:</caption>
            <tr>
                <th>Имя</th>
                <th>Адрес</th>
                <th>Возраст</th>
                <th>Баланс</th>
            </tr>
            <tr>
                <td>${client.getName()}</td>
                <td>${client.getAddress()}</td>
                <td>${client.getAge()}</td>
                <td>0</td>
            </tr>
        </table>
    </div>

    <div>
        <table>
            <caption>Счета:</caption>
            <tr>
                <th>Номер счета</th>
                <th>Баланс</th>
                <th>Транзакции</th>
            </tr>
            <c:forEach items="${accounts}" var="account">
                <tr>
                    <td>${account.getId()}</td>
                    <td>${account.getMoney()}</td>
                    <td>
                        <form action="/transactions" method="get">
                            <input type="hidden" value="${account.getId()}" name="acc_id">
                            <input type="submit" value="Транзакции">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <div>
        <form method="post" action="/accounts/add">
            <input id="idClient" name="idClient" type="hidden" value="${client.getId()}" >
            <input type="submit" value="Создать новый счет">
        </form>
    </div>

</body>
</html>
