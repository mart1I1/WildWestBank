<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="for" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Переводы</title>
    <link href=${pageContext.request.contextPath}/resources/style.css rel="stylesheet">
    <script>
        function setDate()
        {
            document.getElementById("currDate").value = new Date();
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

    <div class="noneLastBorder">
        <form:form modelAttribute="transaction" method="post" action="/accounts/transfer" onsubmit="setDate()">
            <table>
                <caption>Выполнить перевод:</caption>
                <tr>
                    <td>Номер счет отправителя</td>
                    <td><input type="number" name="idAccSender"/></td>
                    <td><form:errors path="idAccSender" cssClass="error"/></td>
                </tr>
                <tr>
                    <td>Номер счета получателя</td>
                    <td><input type="number" name="idAccReceiver" /></td>
                    <td><form:errors path="idAccReceiver" cssClass="error"/></td>
                </tr>
                <tr>
                    <td>Сумма</td>
                    <td><input type="number" name="money" /></td>
                    <td><form:errors path="money" cssClass="error"/></td>
                </tr>
                <tr>
                    <td class="last"></td>
                    <td class="last"><input type="submit" style="display: inline; vertical-align: bottom;"/></td>
                    <td><form:errors path="" cssClass="error"/></td>
                </tr>
                <input type="hidden" id="currDate" name="date">
            </table>
        </form:form>
    </div>
</body>
</html>
