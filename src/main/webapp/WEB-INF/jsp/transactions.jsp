<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Fedor
  Date: 08.11.2018
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transactions</title>
    <link href=${pageContext.request.contextPath}/resources/style.css rel="stylesheet">
    <script>
        function disableEmptyInputs(form) {
            var controls = form.elements;
            for (var i=0, iLen=controls.length; i<iLen; i++) {
                controls[i].disabled = controls[i].value == '';
            }
        }
    </script>
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
        <form action="/transactions" method="get" onsubmit="disableEmptyInputs(this)">
            <table>
                <caption>Фильтр:</caption>
                <tr>
                    <td>
                        Имя
                    </td>
                    <td>
                        <input type="text" name="name" value="${name}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Номер счета
                    </td>
                    <td>
                        <input type="number" name="acc_id" value="${acc_id}">
                    </td>
                </tr>
                <tr>
                    <td>
                        Дата
                    </td>
                    <td>
                        От
                        <input type="date" name="date_from" value="${date_from}">
                        До
                        <input type="date" name="date_to" value="${date_to}">
                    </td>
                </tr>
            </table>
            <input type="submit" value="Поиск">
        </form>
    </div>

    <div>
        <table>
            <caption>Транзакции:</caption>
            <tr>
                <th>Номер счета отправителя</th>
                <th>Номер счета получателя</th>
                <th>Сумма</th>
                <th>Дата</th>
            </tr>
            <c:forEach items="${transactions}" var="transaction">
                <tr>
                    <td>${transaction.getIdAccSender()}</td>
                    <td>${transaction.getIdAccReceiver()}</td>
                    <td>${transaction.getMoney()}</td>
                    <td>${transaction.getDate()}</td>
                </tr>
            </c:forEach>
        </table>
    </div>

</body>
</html>
