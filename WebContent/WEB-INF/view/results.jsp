<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/style.css" />" />
<html>
<body>
	<h1>Please enjoy your results!</h1>

	<div id="content">
		<form action="result" method="get">
			<table>
				<tr>
					<th>ID</th>
					<th>Filename</th>
					<th>Score</th>
					<th>Grade</th>
				</tr>
				<c:forEach items="${reports}" var="report">
					<tr>
						<td><c:out value="${report.id}" /></td>
						<td><c:out value="${report.filename}" /></td>
						<td><c:out value="${report.score}" /></td>
						<td><c:out value="${report.grade}" /></td>

					</tr>
				</c:forEach>
			</table>
		</form>
	</div>

	<form method="GET" action="back">
		<br />
		<input type="submit" value="Return to uploads!" />
	</form>

</body>
</html>