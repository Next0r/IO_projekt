<%-- 
    Document   : AccountSettingsPage
    Created on : 2019-01-27, 17:45:30
    Author     : Michal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account settings page</title>
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
            .field-class{
                float: right;
                width: 60%;
            }
        </style>
    </head>
    <body>
        <h1>Account settings</h1>
        <div class="menu-button-group">
            <button onclick="window.location.href = 'Homepage.jsp';">Home</button>
            <button onclick="window.location.href = '<%=request.getContextPath()%>/AccountSettings';">Account settings</button>
            <button>My products</button>
            <button>My cars</button>
        </div>

        <c:choose>
            <c:when test="${not empty clientName}">
                <c:set var="clnName" value="${clientName}"></c:set>
                <c:set var="clnSurname" value="${clientSurname}"></c:set>
            </c:when>
            <c:otherwise>
                <c:set var="clnName" value=""></c:set>
                <c:set var="clnSurname" value=""></c:set>
                <p>You still need to set up your name and surname to commit any transactions!</p>
            </c:otherwise>
        </c:choose>

        <div style="width:20%;">
            <form action="" method="post">
                <p>
                    Your name: <input class="field-class" type="text" name="name" value="${clnName}"/>
                    <input type="hidden" name="hidden" value="name"/>
                <p><input type="submit" value="change name"/></p>
                </p>
            </form>
            <form action="" method="post">
                <p>
                    Your surname: <input class="field-class" type="text" name="surname" value="${clnSurname}"/>
                    <input type="hidden" name="hidden" value="surname"/>
                <p><input type="submit" value="change surname"/></p>
                </p>
            </form>


            <form action="" method="post">
                <p>
                    Your login: <input class="field-class" type="text" name="login" value="${currentUser}"/>
                    <input type="hidden" name="hidden" value="login"/>
                <p><input type="submit" value="change login"/></p>
                </p>
            </form>
            <form action="" method="post">
                <p>
                    Password: <input class="field-class" type="password" name="password" value="password"/>
                </p>
                <p>
                    Password retype: <input class="field-class" type="password" name="repassword" value="password"/>
                    <input type="hidden" name="hidden" value="password"/>
                <p><input type="submit" value="change password"/></p>
                </p>
            </form>
        </div>


    </body>
</html>
