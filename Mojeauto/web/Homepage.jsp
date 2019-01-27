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
    <style>
        
        .menu-button-group {
            width:100%;
        }
        
        .menu-button-group button {
            background-color: #4081e8;
            border: 1px solid blue;
            color: white;
            cursor: pointer;
            padding: 10px 20px;
            width: 24.5%;
        }
        .menu-button-group button:hover {
            background-color: #215dc4;
        }   
        
        
    </style>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mojeauto</title>
    </head>
    <body>
        
        <h1>Welcome to homepage!</h1>
        
        <div class="menu-button-group">
            <button  onclick="window.location.href='Homepage.jsp';">Home</button>
            <button>View Offers</button>
            <button>Buy</button>
            <c:choose>
                <c:when test="${not empty currentUser}">
                    <button  onclick="window.location.href='MyAccountPage.jsp';">My Account</button>
                </c:when>
                <c:otherwise>
                    <button  onclick="window.location.href='LoginPage.jsp';">Login</button>
                </c:otherwise>
            </c:choose>
        </div>
        
        <hr>
        
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
