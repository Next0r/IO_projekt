<%-- 
    Document   : LoginPage
    Created on : 2019-01-27, 10:56:36
    Author     : Michal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LoginPage</title>
    </head>
    <body>
        <h1>LoginPage</h1>
        <hr/>
        <p>${loginMessage}</p>
        <form action="<%=request.getContextPath()%>/LogInOut" method="post">
            <p>Login: <input type="text" name="login"/></p>
            <p>Password: <input type="password" name="password"/></p>
            <p><input type="submit" value="Login"/></p>
        </form>
        <a href="Homepage.jsp">Go to homepage</a>
        
    </body>
</html>
