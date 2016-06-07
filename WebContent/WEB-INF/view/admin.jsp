<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="style.css" />


<html>
<body>
	<h1>WELCOME ADMIN</h1>
	<p>NOTICE : All usernames are unique!</p>
	<p>Usernames must be 3 to 15 characters long and may contain numbers!</p>
	<p>Passwords may be up to 13 characters long and may contain numbers and symbols however it cannot be empty!</p>

	<form action="admin" method="get">
		<table>
			<tr>
				<th>Usernames currently in use!</th>
			</tr>
			<c:forEach items="${users}" var="user">
				<tr>
					<td><c:out value="${user.username}" /></td>
				</tr>
			</c:forEach>
		</table>
	</form>

	<div id="content">
		<form action="create" method="post"
			onsubmit="create(username.value,password.value);return false;">
			<p>Enter the username and password for the new user you would
				like to create!</p>
			Username: <input id="username" type="text" name="username">
			<p>
				Password: <input id="password" type="password" name="password">
			<p>
				<input type="submit" value="Create user!">
		</form>
	</div>

	<c:if test="${message eq true}">
		<div id="alert">Username is already in use or current entry is
			invalid!</div>
	</c:if>

	<form method="POST" action="logout">
		<br />
		<input type="submit" value="Exit!" />
	</form>
</body>
</html>