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
            .cell{
                width: 33%;
            }
            .text-box1{
                width: 100%;
            }
            .input-field1{
                width: 100%;
            }
            .form-button1{
                width: 100%;
            }
        </style>
    </head>
    <body>
        <%-- Code that should executed on page load --%>
        <%

        %>


        <%-- Header section --%>
    <center>
        <div class="header-container">
            <%-- Banner --%>
            <div class ="header-banner">
                <h1>Account settings</h1>
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
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/PackageSales';">Our products</button>
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
    <hr/>
    <center>
        <div class="menu-button-group" style="margin: 10px auto; width: 80%;">
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/AccountSettings';">Account Settings</button>
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/ManageVehicles';">My Vehicles</button>
            <button  onclick="window.location.href = 'AddVehiclePage.jsp';">Add Vehicle</button>
            <button  onclick="window.location.href = '';">SubPage4</button>
        </div>
    </center>
    <%-- Page content --%>
    <hr>
    <c:if test="${not empty resultMessage}">
        <div class="text-box2" style="margin-left: 10px; margin-bottom: 6px;">${resultMessage}</div>
        <%
            session.setAttribute("resultMessage", "");
        %>
    </c:if>

    <c:set var="clnName" value="${clientName}"></c:set>
    <c:set var="clnSurname" value="${clientSurname}"></c:set>

    <c:if test="${empty clientName || empty clientSurname}">
        <div class="text-box2" style="margin-left: 10px;">You still have to set up your personal details.</div>
    </c:if>

    <div class="table" style="margin-left: -3px; margin-top: -3px">
        <div class="row">
            <div class="cell">
                <div class="text-box1">Your name:</div> 
            </div>
            <div class="cell">
                <form id="form1" action="<%=request.getContextPath()%>/AccountSettings" method="post">
                    <input class="input-field1" type="text" name="name" value="${clnName}"/>
                    <input type="hidden" name="hidden" value="name"/>
                </form>
            </div>
            <div class="cell">
                <input form="form1" class="form-button1" type="submit" value="Change Name"/>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">Your surname:</div>  
            </div>
            <div class="cell">
                <form id="form2" action="<%=request.getContextPath()%>/AccountSettings" method="post">
                    <input class="input-field1" type="text" name="surname" value="${clnSurname}"/>
                    <input type="hidden" name="hidden" value="surname"/>

                </form>
            </div>
            <div class="cell">
                <input form="form2" class="form-button1" type="submit" value="Change Surname"/>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">Your login:</div> 
            </div>
            <div class="cell">
                <form id="form3" action="<%=request.getContextPath()%>/AccountSettings" method="post">
                    <input class="input-field1" type="text" name="login" value="${currentUser}"/>
                    <input type="hidden" name="hidden" value="login"/>

                </form>
            </div>
            <div class="cell">
                <input form="form3" class="form-button1" type="submit" value="Change Login"/>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">Password:</div> 
            </div>
            <div class="cell">
                <form id="form4" action="<%=request.getContextPath()%>/AccountSettings" method="post">
                    <input class="input-field1" type="password" name="password" value="" placeholder="password"/>
                    <input type="hidden" name="hidden" value="password"/>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">Password retype:</div> 
            </div>
            <div class="cell">
                <input class="input-field1" form="form4" type="password" name="repassword" value="" placeholder="password"/>
            </div>
            <div class="cell">
                <input class="form-button1" form="form4" type="submit" value="Change Password"/>
            </div>
        </div>
    </div>

</body>
</html>
