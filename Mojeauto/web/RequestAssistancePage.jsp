<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>RequestAssistannce</title>
        <style>
            .cell{
                width: 50%;
            }
            .text-box1{
                width: 100%;
            }
            .input-field1{
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
                <h1>Request assistance</h1>
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
        <div style="margin-left: 10px; width: 810px;">
            <div class="text-box2" >${resultMessage}</div>
        </div>
        <%
            session.setAttribute("resultMessage", "");
        %>
    </c:if>
    <c:choose>
        <c:when test="${empty currentUser}">
            <div style="width: 500px; margin-left: 10px;">
                <div class="text-box2">You need to log in to request assistance.</div>
            </div>
        </c:when>
        <c:otherwise>
            
            <div class="table" style="width: 810px; margin-bottom: -20px;">
                <div class="row">
                    <div class="cell">
                        <div class="text-box1">Fill fields to submit assistance request.</div>
                    </div>
                </div>
            </div>
            <div class="table" style="margin-left: -10px;">
                <div class="row">
                    <div class="cell">
                        <div class="table" style="width: 400px; margin-bottom: -10px;">
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box2" style="width: 100%;">Your personal information:</div>
                                </div>
                            </div>
                        </div>
                        <div class="table" style="width: 400px;">
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">Name:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="name" value="${clientName}"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">Surname:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="surname" value="${clientSurname}"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">Phone Number:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="phone" value="${clientPhone}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cell">
                        <div class="table" style="width: 400px; margin-bottom: -10px; margin-top: -10px;">
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box2" style="width: 100%;">Service Type: (Not required)</div>
                                </div>
                            </div>
                            <c:forEach items="${services}" var="service">
                                <div class="text-box1">
                                    <input form="form7" type="checkbox" name="${service.productTypeID}"/>
                                    ${service.name}
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="cell">
                        <div class="table" style="width: 400px; margin-bottom: -10px; margin-top: -10px;">
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box2" style="width: 100%;">Data of your vehicle:</div>
                                </div>
                            </div>
                        </div>
                        <div class="table" style="width: 400px;">
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">Brand:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="brand"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">Model:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="model"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">License Number:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="lNumber"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box1">Production Year:</div>
                                </div>
                                <div class="cell">
                                    <input form="form7" class="input-field1" type="text" name="pYear"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cell">
                        <div class="table" style="width: 400px; margin-bottom: -10px; margin-top: -10px;">
                            <div class="row">
                                <div class="cell">
                                    <div class="text-box2" style="width: 100%;">Issue Description: (Required if no service selected)</div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="cell">
                                    <textarea form="form7" class="input-field1" style="height: auto; line-height: 16.25px;" name="description" cols="40" rows="9"></textarea>
                                </div>
                            </div>
                        </div>   
                    </div>
                </div>
                <div class="row">
                    <div class="cell">
                        <form id="form7" action="" method="post">
                            <input type="hidden" name="submit" value="submit"/>
                            <input class="form-button1" style="width: 184px; margin-left: 13px;" type="submit" value="Send"/>
                        </form>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    
</body>
</html>
