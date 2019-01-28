<%-- 
    Document   : LoginPage
    Created on : 2019-01-27, 10:56:36
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
        <title>LoginPage</title>
        <style>
            input{
                float:right;
                width: 40%;
                margin-right: 20%;
            }
            
            td{
                vertical-align: top;
                width: 50%;
            }
        </style>
    </head>
    <body>
        <h1>LoginRegisterPage</h1>
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
        
        <p>${accountMessage}</p>
        <table style="width: 50%;">
            <tr>
                <td>
                    <p>I already have account...</p>
                    <hr/>
                    <form action="<%=request.getContextPath()%>/LogInOut" method="post">
                        <p>Login: <input type="text" name="login"/></p>
                        <p>Password: <input type="password" name="password"/></p>
                        <p><input type="submit" value="Login"/></p>
                    </form>
                </td>
                <td>
                    <p>I want to create a new account!</p>
                    <hr/>
                    <form action="<%=request.getContextPath()%>/CreateAccount" method="post">
                        <p>Login: <input type="text" name="login"/></p>
                        <p>Password: <input type="password" name="password"/></p>
                        <p>Retype password: <input type="password" name="repassword"/></p>
                        <p><input type="submit" value="Register"></p>
                    </form>
                </td>
            </tr>
        </table>

    </body>
</html>
