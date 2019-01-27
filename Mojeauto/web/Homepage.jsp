<%-- 
    Document   : Homepage
    Created on : 2019-01-26, 20:15:15
    Author     : Sobocik
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mojeauto</title>
    </head>
    <body>
        <h1>Welcome to homepage!</h1>
        <div>What would you like to do?</div>

        <c:if test="${not empty currentUser}">
            <hr/>
            <p>Logged in as ${currentUser}</p>
            <form action="<%=request.getContextPath()%>/LogInOut" method="post">
                <input type="hidden" name="hidden" value="logout"/>
                <input type="submit" value="log out" />
            </form>
        </c:if>

        <hr>
        <form action="<%=request.getContextPath()%>/TEMP_PopulateDB" method="POST">
            <input type="submit" value="Populate database with test entities" />
        </form>
        <hr>

        <c:choose>
            <c:when test="${not empty currentUser}">
                <form action="MyAccountPage.jsp" method="get">
                    <p><input type="submit" value="My account"/></p>
                </form>
            </c:when>
            <c:otherwise>
                <form action="LoginPage.jsp" method="get">
                    <p><input type="submit" value="My account"/></p>
                </form>
            </c:otherwise>
        </c:choose>


    </body>
</html>
