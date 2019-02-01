<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add vehicle page</title>
        <style>
            .cell{
                width: 50%;
            }
            .text-box1{
                width: 100%;
                margin: 0;
            }
            .input-field1{
                width: 100%;
            }
            .table{
                width: 400px;
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
                <h1>Add vehicle page</h1>
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
        <div class="text-box2" style="margin-left: 10px; width: 500px;">${resultMessage}</div>
        <%
            session.setAttribute("resultMessage", "");
        %>
    </c:if>
    <div class="table">
        <div class="row">
            <div class="cell">
                <div class="text-box1">Brand</div>
            </div>
            <div class="cell">
                <input form="form5" class="input-field1" type="text" name="brand"/>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">Model</div>
            </div>
            <div class="cell">
                <input form="form5" class="input-field1" type="text" name="model"/>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">License Number</div>
            </div>
            <div class="cell">
                <input form="form5" class="input-field1" type="text" name="lnumber"/>
            </div>
        </div>
        <div class="row">
            <div class="cell">
                <div class="text-box1">Production Year</div>
            </div>
            <div class="cell">
                <input form="form5" class="input-field1" type="text" name="pyear"/>
            </div>
        </div>
    </div>
    <div class="table" style="margin-top: -6px;">
        <div class="row">
            <div class="cell">
                <form id="form5" action="<%=request.getContextPath()%>/ManageVehicles" method="post">
                    <input type="hidden" name="hidden" value="add"/>
                    <input class="form-button1" style="width: calc(50% - 5px);" type="submit" value="Add Vehicle"/>
                </form>
            </div>
        </div>
    </div>

</body>
</html>
