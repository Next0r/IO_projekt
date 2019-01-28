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
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mojeauto</title>
    </head>
    <body>
        <%
            // reset login message to avoid displaying again in login page
            session.setAttribute("accountMessage", "");
        %>
        <h1>Welcome to Mojeauto!</h1>
        
        <div class="menu-button-group">
            <button  onclick="window.location.href='Homepage.jsp';">Home</button>
            <button  onclick="window.location.href='OurProductsPage.jsp';">Our products</button>
            <button  onclick="window.location.href='RequestAssistancePage.jsp';">Request assistance</button>
            <button  onclick="window.location.href='ContactPage.jsp';">Contact</button>
            <c:choose>
                <c:when test="${not empty currentUser}">
                    <button  onclick="window.location.href='MyAccountPage.jsp';">My Account</button>
                </c:when>
                <c:otherwise>
                    <button  onclick="window.location.href='LoginRegisterPage.jsp';">Login/Register</button>
                </c:otherwise>
            </c:choose>
            <hr>
        </div>
        
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
            <input type="submit" value="Populate database with test entities!" />
        </form>
        <hr>

    </body>
</html>
