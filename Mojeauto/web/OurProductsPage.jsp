<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>OurProducts</title>
        <style>
            .text-box1{
                width: 100%;
                height: inherit;
                flex-wrap: wrap;
            }
            .text-box2{
                width: 100%;
            }
        </style>
    </head>
    <body>
        <%-- Header section --%>
    <center>
        <div class="header-container">
            <%-- Banner --%>
            <div class ="header-banner">
                <h1>Our products page</h1>
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
            <button  onclick="window.location.href = '<%=request.getContextPath()%>/ProductSales';">Our products</button>
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
    
            
            
    <%-- Page content --%>
    <hr>
    <c:if test="${not empty resultMessage}">
        <div style="margin-left: 10px; width: 542px;">
            <div class="text-box2" >${resultMessage}</div>
        </div>
        <%  session.setAttribute("resultMessage", ""); %>
    </c:if>
    <c:choose>
        <c:when test="${carSelection != true}">
            <c:if test="${empty currentUser}">
                <div style="width: 500px; margin-left: 10px;">
                    <div class="text-box2">You need to log in to purchase package.</div>
                </div>
            </c:if>
            <c:if test="${not empty packages}">
                <hr>
                <div class="text-box1">Packages:</div>
                <div class="table" style="width: 100%; min-width: 700px;">
                    <div class="row">
                        <c:forEach items="${packages}" var="package">
                            <div class="cell" style="width: 33%;">
                                <div class="text-box2">${package.name}</div>
                                <div class="text-box1" style="text-align: justify;">${package.description}</div>
                                <div class="text-box2">Includes services:</div>
                                <c:forEach items="${package.singleServices}" var="sService">
                                    <div class="text-box1">
                                        <div style="padding-left: 20px;">${sService.name}</div>
                                    </div> 
                                </c:forEach>
                                <div class="text-box2">Price: ${package.price}$</div>
                                <c:if test="${not empty currentUser}">
                                    <center>
                                        <form action="<%=request.getContextPath()%>/ProductSales" method="post">
                                            <input type="hidden" name="stage" value="packageSelected"/>
                                            <input type="hidden" name="packageSelected" value="${package.productTypeID}"/>
                                            <input class="form-button1" type="submit" value="Buy Package"/>
                                        </form>
                                    </center> 
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty services}">
                <hr>
                <div class="text-box1">Single services:</div>
                <div class="table" style="width: 100%">
                    <div class="row">
                        <div class="cell" style="width: 20%;">
                            <div class="text-box2">Name</div>
                        </div>
                        <div class="cell" style="width: 40%;">
                            <div class="text-box2">Description</div>
                        </div>
                        <div class="cell" style="width: 20%;">
                            <div class="text-box2">Price</div>
                        </div>
                        <div class="cell" style="width: 20%;">
                        </div>
                    </div>
                    <c:forEach items="${services}" var="service">
                        <div class="row">
                            <div class="cell" style="width: 20%;">
                                <div class="text-box1">${service.name}</div>
                            </div>
                            <div class="cell" style="width: 40%;">
                                <div class="text-box1">${service.description}</div>
                            </div>
                            <div class="cell" style="width: 20%;">
                                <div class="text-box1">${service.price}$</div>
                            </div>
                            <div class="cell" style="width: 20%;">
                                <c:if test="${not empty currentUser}">
                                    <form action="<%=request.getContextPath()%>/ProductSales" method="post">
                                        <input type="hidden" name="stage" value="serviceSelected"/>
                                        <input type="hidden" name="serviceSelected" value="${service.productTypeID}"/>
                                        <input class="form-button1" type="submit" value="Buy Service"/>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                    
                </div>
            </c:if>
        </c:when>
        <c:otherwise>
            <%  session.setAttribute("carSelection", false);  %>
            <div style="width: 542px; margin-left: 10px;">
                <div class="text-box1">Select vehicles that should be associated with the package:</div>
            </div>
            <div class="table">
                <div class="row">
                    <div class="cell" style="width: 20%;">
                        <div class="text-box2">Brand</div>
                    </div>
                    <div class="cell" style="width: 20%;">
                        <div class="text-box2">Model</div>
                    </div>
                    <div class="cell" style="width: 20%;">
                        <div class="text-box2">License Number</div>
                    </div>
                    <div class="cell" style="width: 20%;">
                        <div class="text-box2">Production Year</div>
                    </div>
                    <div class="cell" style="width: 20%;">
                    </div>
                </div>
                <c:forEach items="${clientCars}" var="car">
                    <div class="row">
                        <div class="cell" style="width: 20%;">
                            <div class="text-box1">${car.brand}</div>
                        </div>
                        <div class="cell" style="width: 20%;">
                            <div class="text-box1">${car.model}</div>
                        </div>
                        <div class="cell" style="width: 20%;">
                            <div class="text-box1">${car.licenseNumber}</div>
                        </div>
                        <div class="cell" style="width: 20%;">
                            <div class="text-box1">${car.productionYear}</div>
                        </div>
                        <div class="cell" style="width: 20%;">
                            <input form="form6" type="checkbox" name="${car.licenseNumber}"/>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="table">
                <div class="row">
                    <div class="cell">
                        <form id="form6" action="<%=request.getContextPath()%>/ProductSales" method="post">    
                            <input type="hidden" name="stage" value="carsSelected"/>
                            <input type="hidden" name="carsSelected" value="carsSelected"/>
                            <input class="form-button1" style="margin-left: 173px; width: 200px;" type="submit" value="Buy"/>
                        </form>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>    

</body>
</html>
