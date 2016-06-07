<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div id="content">
	<form action="login"
		onsubmit="login(username.value,password.value);return false;">
		Username: <input id="username" type="text" name="username">
		<p>
			Password: <input id="password" type="password" name="password">
		<p>
			<input type="submit" value="Login">
	</form>
</div>
<c:if test="${incorrect eq true}">
	<div id="alert">Incorrect username or password</div>
</c:if>
