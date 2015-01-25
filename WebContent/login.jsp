<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>MJChalkStack</title>
	<link rel="stylesheet" href="css/bootmin.css">
	<link rel="stylesheet" href="css/style.css">
 </head>
 <body>
	<header>
		<h1>Microjuris.com</h1>
	</header>
	
	<div class="container">
		<form class="form-signin" method="post" action="access_course">
		<div style="color: #FF0000;">${errorMessage}</div>
        <h2 class="form-signin-heading">Please Login Here</h2>
        <input type="email" name="email" class="form-control" placeholder="Email address" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="Password" required>
        
        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
      </form>

    </div> <!-- /container -->
</body>
</html>