<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>myTitle</title>
</head>
<body>
<h2>历史年化收益率${historyAverageRate}</h2>
<h2>平台注册总人数${allUserCount}</h2>
<h2>平台累计投资总金额${allBidMoney}</h2>

</body>
</html>