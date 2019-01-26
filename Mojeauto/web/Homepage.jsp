<%-- 
    Document   : Homepage
    Created on : 2019-01-26, 20:15:15
    Author     : Sobocik
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mojeauto</title>
    </head>
    <body>
        <h1>Welcome to homepage!</h1>
        <div>What would you like to do?</div>
        <hr>
        <form action="<%=request.getContextPath()%>/TEMP_PopulateDB" method="POST">
            <input type="submit" value="Populate database with test entities" />
        </form>
        <hr>
    </body>
</html>
