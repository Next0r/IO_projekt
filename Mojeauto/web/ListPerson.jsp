<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Of Persons</title>
    </head>
    <body>

    <h1>List of Persons currently in Database</h1>
    
<table id="personListTable" border="3">
<tr >
    <th bgcolor=>ID</th>
    <th bgcolor=>FirstName</th>
    <th bgcolor=>LastName</th>
</tr>
<c:forEach var="person" begin="0" items="${requestScope.personList}">
<tr>
    <td>${person.id}&nbsp;&nbsp;</td> 
    <td>${person.firstName}&nbsp;&nbsp;</td> 
    <td>${person.lastName}&nbsp;&nbsp;</td> 
</tr> 

</c:forEach>

</table>
<a href="CreatePerson.jsp"><strong>Create a Person Record</strong></a>
</body>
</html>
