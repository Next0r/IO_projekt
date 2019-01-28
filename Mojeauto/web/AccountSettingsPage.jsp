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
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account settings page</title>
        <style>
            .field-class{
                float: right;
                width: 60%;
            }
        </style>
    </head>
    <body>
        <h1>Account settings</h1>
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
