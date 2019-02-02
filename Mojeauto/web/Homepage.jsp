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
        <%-- Header section --%>
    <center>
        <div class="header-container">
            <%-- Banner --%>
            <div class ="header-banner">
                <h1>Welcome to Mojeauto!</h1>
            </div>

            <%-- Account status --%> 
            <div class="login-status">
                <c:choose>
                    <c:when test="${not empty currentUser}">
                        <div style="margin: auto;">
                            <p>Welcome, ${currentUser}!</p>
                            <form action="<%=request.getContextPath()%>/LogInOut" method="post">
                                <input type="hidden" name="hidden" value="logout"/>
                                <input class="form-button2" type="submit" value="Log Out" />
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div style="margin: auto;">
                            <p>Looks like you are not logged in!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </center>

    <%-- Main navigation buttons --%>
    <center>
        <div class="menu-button-group">
            <button  onclick="window.location.href = 'Homepage.jsp';">Home</button>
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/ProductSales';">Our products</button>
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/LoadRequest';">Request assistance</button>
            <button  onclick="window.location.href = 'ContactPage.jsp';">Contact</button>
            <c:choose>
                <c:when test="${not empty currentUser}">
                    <button  onclick="window.location.href = 'MyAccountPage.jsp';">My Account</button>
                </c:when>
                <c:otherwise>
                    <button  onclick="window.location.href = 'LoginRegisterPage.jsp';">Login/Register</button>
                </c:otherwise>
            </c:choose>
        </div>
    </center>
    <%-- Sub navigation buttons --%>
    <%-- Page content --%>
    <hr>
    <p>What would you like to do?</p>

    <form action="<%=request.getContextPath()%>/TEMP_PopulateDB" method="POST">
        BUTTON FOR TESTING - fills database with example entities (products, product types, services, accounts etc.) <br>
        <input type="submit" value="Populate database with test entities!" />
    </form>
    <hr>

</body>
</html>
