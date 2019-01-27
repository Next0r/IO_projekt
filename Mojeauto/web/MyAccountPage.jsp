<%-- 
    Document   : MyAccountPage
    Created on : 2019-01-27, 12:54:04
    Author     : Michal
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My account page</title>
    </head>
    <body>
        
        <c:if test="${not empty currentUser}">
            <hr/>
            <p>Logged in as ${currentUser}</p>
            <form action="<%=request.getContextPath()%>/LogInOut" method="post">
                <input type="hidden" name="hidden" value="logout"/>
                <input type="submit" value="log out" />
            </form>
	</c:if>
    
        <hr/>
        <p>Here you can manage account, buy stuff, contact us etc.</p>
        <a href="Homepage.jsp">Go to homepage</a>
    </body>
</html>
