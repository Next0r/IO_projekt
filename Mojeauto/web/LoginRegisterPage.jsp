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
            
        </style>
    </head>
    <body>
        <%-- Code that should executed on page load --%>
        <%
            // reset login message to avoid displaying again in login page
            // session.setAttribute("accountMessage", "");
        %>

        <%-- Header section --%>
    <center>
        <div class="header-container">
            <%-- Banner --%>
            <div class ="header-banner">
                <h1>Login/register page</h1>
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
            <button  onclick="window.location.href = 'OurProductsPage.jsp';">Our products</button>
            <button  onclick="window.location.href = 'RequestAssistancePage.jsp';">Request assistance</button>
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
    <%--
    <hr/>
    <center>
        <div class="menu-button-group" style="margin: 10px auto; width: 80%;">
            <button  onclick="window.location.href = '';">SubPage1</button>
            <button  onclick="window.location.href = '';">SubPage2</button>
            <button  onclick="window.location.href = '';">SubPage3</button>
            <button  onclick="window.location.href = '';">SubPage4</button>
        </div>
    </center>
    --%>
    <%-- Page content --%>
    <hr>
    <c:if test="${not empty accountMessage}">
        <div class="text-box2" style="margin-left: 13px;">${accountMessage}</div>
    </c:if>
    <div class="table">
        <div class="row">
            <div class="cell">
                <div class="text-box1" style="width: 300px;">I already have account...</div>
            </div>
            <div class="cell">
                <div class="text-box1" style="width: 300px;">I want to create a new account!</div>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <form action="<%=request.getContextPath()%>/LogInOut" method="post">
                    <div class="text-box1">Login: </div>
                    <input class="input-field1" type="text" name="login"/>
                    <div class="text-box1">Password: </div>
                    <input class="input-field1" type="password" name="password"/>
                    <input class="form-button1" type="submit" value="Login"/>
                </form> 
            </div>
            <div class="cell">
                <form action="<%=request.getContextPath()%>/CreateAccount" method="post">
                    <div class="text-box1">Login: </div>
                    <input class="input-field1" type="text" name="login"/>
                    <div class="text-box1">Password: </div>
                    <input class="input-field1" type="password" name="password"/>
                    <div class="text-box1">Retype password: </div>
                    <input class="input-field1" type="password" name="repassword"/>
                    <input class="form-button1" type="submit" value="Register">
                </form>
            </div>
        </div>        
    </div>

</body>
</html>
