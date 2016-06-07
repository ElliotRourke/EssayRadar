function load() {
	$.ajax({
		type: "POST",
		url: "load",
		beforeSend: function() {
			document.getElementById("content").innerHTML;
		},
		success: function(response){
			if (response != "") {
				document.getElementById("content").innerHTML = response;
			}
		}
	});
}

function login(username, password) {
	$.ajax({
		type: "POST",
		url: "login",
		data: {
			"username": username,
			"password": password
		},
		beforeSend: function() {
			document.getElementById("content").innerHTML;
		},
		success: function(response){
			if (response != "") {
				document.getElementById("content").innerHTML = response;
			}
		}
	});
}

function back() {
	$.ajax({
		type: "POST",
		url: "back",
		beforeSend: function() {
			document.getElementById("content").innerHTML;
		},
		success: function(response){
			if (response != "") {
				document.getElementById("content").innerHTML = response;
			}
		}
	});
}

function logout() {
	$.ajax({
		type: "POST",
		url: "logout",
		beforeSend: function() {
			document.getElementById("content").innerHTML;
		},
		success: function(response){
			if (response != "") {
				document.getElementById("content").innerHTML = response;
			}
		}
	});
}

function create(username, password) {
	$.ajax({
		type: "POST",
		url: "create",
		data: {
			"username": name,
			"password": pass
		},
		beforeSend: function() {
			document.getElementById("content").innerHTML;
		},
		success: function(response){
			if (response != "") {
				document.getElementById("content").innerHTML = response;
			}
		}
	});
}




