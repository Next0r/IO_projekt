<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My services page</title>
        <style>
            .cell{
                width: auto;
            }
            .text-box2{
                width: 100%;
                margin: 0;
                
            }
            .text-box1{
                width: 100%;
                margin: 0;
                flex-wrap: wrap;
                height: auto;
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
                <h1>My services page</h1>
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
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/FetchServices';">My Services</button>
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
    <c:if test="${not empty clientProducts}">
        <div class="table">
            <div class="row">
                <div class="cell">
                    <div class="text-box2">Expiration Date</div>
                </div>
                <div class="cell">
                    <div class="text-box2">Product Type</div>
                </div>
                <div class="cell">
                    <div class="text-box2">Product Name</div>
                </div>
                <div class="cell">
                    <div class="text-box2">Related Car</div>
                </div>
            </div>
            <c:forEach items="${clientProducts}" var="product">
                <div class="row">
                    <div class="cell">
                        <c:choose>
                            <c:when test="${ not empty product.expirationDate}">
                                <div class="text-box1">${product.expirationDate}</div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-box1">None</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="cell">
                        <c:choose>
                            <c:when test="${ not empty product.expirationDate}">
                                <div class="text-box1">Package</div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-box1">Single Service</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="cell">
                        <div class="text-box1">${product.productType.name}</div>
                    </div>
                    <div class="cell">
                        <div class="text-box1">${product.clientCar.licenseNumber}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

</body>
</html>
