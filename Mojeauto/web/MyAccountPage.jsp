<%-- 
    Document   : MyAccountPage
    Created on : 2019-01-27, 12:54:04
    Author     : Michal
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My account page</title>
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
    </head>
    <body>
        <h1>My account page</h1>
        <div class="menu-button-group">
            <button onclick="window.location.href = 'Homepage.jsp';">Home</button>
            <button onclick="window.location.href = '<%=request.getContextPath()%>/AccountSettings';">Account settings</button>
            <button>My products</button>
            <button>My cars</button>
        </div>

        <c:if test="${not empty currentUser}">
            <hr/>
            <p>Logged in as ${currentUser}</p>
            <form action="<%=request.getContextPath()%>/LogInOut" method="post">
                <input type="hidden" name="hidden" value="logout"/>
                <input type="submit" value="log out" />
            </form>
            <hr/>
        </c:if>
    </body>
</html>
