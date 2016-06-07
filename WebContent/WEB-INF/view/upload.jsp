<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/style.css" />" />

<p>Welcome to the essay comparison program! Please upload an essay
	to begin comparison! Your results will arrive shortly after!</p>
<p>Please note that the program currently only supports '.txt' and
	'.docx' files!</p>

<form method="POST" action="compare" enctype="multipart/form-data">
	<input type="hidden" name="hiddenfield1" value="ok"> Select
	file to upload: <input type="file" name="file" /><br /> <br />
	<input type="submit" value="upload" />
</form>

<c:if test="${upmessage eq true}">
	<div id="alert">Invalid file!</div>
</c:if>

<form method="POST" action="logout">
	<br />
	<input type="submit" value="Exit!" />
</form>