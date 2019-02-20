<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <style>
      .red {
        color: #f00
      }
      .green {
        color: #0f0
      }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
<tr><td>date</td><td>time</td><td>description</td><td>calories</td><td>excess</td>
<c:forEach items="${mealtolist}" var="mealTo">
  <c:choose>
    <c:when test="${mealTo.excess}">
      <tr class="red">
    </c:when>
    <c:otherwise>
      <tr class="green">
    </c:otherwise>
  </c:choose>
        <td>${mealTo.date}</td><td>${mealTo.time}</td><td>${mealTo.description}</td><td>${mealTo.calories}</td><td>${mealTo.excess}</td>
      </tr>
</c:forEach>
</table>

</body>
</html>