<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My vehicles page</title>
        <style>
            .cell{
                width: 20%;
            }
            .text-box1{
                width: 100%;
                margin: 0;
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
                <h1>My vehicles page</h1>
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
        <div class="text-box2" style="margin-left: 10px; margin-bottom: 0px; width: 500px;">${resultMessage}</div>
        <%
            session.setAttribute("resultMessage", "");
        %>
    </c:if>
    <c:choose>
        <c:when test="${not empty clientCars}">
            <div class="table">
                <div class="row">
                    <div class="cell">
                        <div class="text-box2" style="margin: 0px; width: 100%;">Brand</div>
                    </div>
                    <div class="cell">
                        <div class="text-box2" style="margin: 0px; width: 100%;">Model</div>
                    </div>
                    <div class="cell">
                        <div class="text-box2" style="margin: 0px; width: 100%;">License Number</div>
                    </div>
                    <div class="cell">
                        <div class="text-box2" style="margin: 0px; width: 100%;">Production Year</div>
                    </div>
                    <div class="cell">
                    </div>
                </div>
                <c:forEach items="${clientCars}" var="car">
                    <div class="row">
                        <div class="cell">
                            <div class="text-box1">${car.brand}</div>
                        </div>
                        <div class="cell">
                            <div class="text-box1">${car.model}</div>
                        </div>
                        <div class="cell">
                            <div class="text-box1">${car.licenseNumber}</div>
                        </div>
                        <div class="cell">
                            <div class="text-box1">${car.productionYear}</div>
                        </div>
                        <div class="cell">
                            <form action="<%=request.getContextPath()%>/ManageVehicles" method="post">
                                <input class="form-button1" type="submit" value="Remove"/>
                                <input type="hidden" name="hidden" value="${car.clientCarID}"/>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-box2" style="margin-left: 10px; margin-top: 6px; width: 500px;">There are no vehicles related to your account.</div>
        </c:otherwise>
    </c:choose>

</body>
</html>
