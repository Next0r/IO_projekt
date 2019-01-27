<%-- 
    Document   : LoginPage
    Created on : 2019-01-27, 10:56:36
    Author     : Michal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
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
        <hr/>
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
                    <p>I want to create new account!</p>
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
        <a href="Homepage.jsp">Go to homepage</a>

    </body>
</html>
