<%--
  Created by IntelliJ IDEA.
  User: CJM
  Date: 24. 7. 31.
  Time: 오후 9:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType = "text/html;charset=UTF-8" language = "java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<H1>List Page</H1>

<c:forEach var = "dto"  items = "${list}">
    <li>${dto}</li>
</c:forEach>

</body>
</html>
