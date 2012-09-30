<%
	String broadcasterId = (String) session
			.getAttribute("net.javaforge.blog.atmosphere.broadcasterId");
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="js/jquery.atmosphere.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						var detectedTransport = null;
						var socket = $.atmosphere;
						var subSocket;

						function getKeyCode(ev) {
							if (window.event)
								return window.event.keyCode;
							return ev.keyCode;
						}

						function getElementById() {
							return document.getElementById(arguments[0]);
						}

						function getTransport(t) {
							transport = t.options[t.selectedIndex].value;
							if (transport == 'autodetect') {
								transport = 'websocket';
							}
							return false;
						}

						function getElementByIdValue() {
							detectedTransport = null;
							return document.getElementById(arguments[0]).value;
						}

						function subscribe() {
							var request = {
								url : document.location.toString() + 'atmosphere/<%=broadcasterId%>',
								transport : getElementByIdValue('transport')
							};

							request.onMessage = function(response) {
								detectedTransport = response.transport;
								if (response.status == 200) {
									var data = response.responseBody;
									if (data.length > 0) {
										$('ul')
												.prepend(
														$('<li></li>$tag$tag$tag')
																.html(
																	"&gt;&nbsp;"
																	+ data
																	+ " [detected transport: "
																	+ detectedTransport
																	+ "]"));
									}
								}
							};

							subSocket = socket.subscribe(request);
						}

						function unsubscribe() {
							callbackAdded = false;
							socket.unsubscribe();
						}

						function connect() {
							unsubscribe();
							getElementById('phrase').value = '';
							getElementById('sendMessage').className = '';
							getElementById('phrase').focus();
							subscribe();
							getElementById('connect').value = "Switch transport";
						}
						
						function sendMessage(){
							var msg = getElementByIdValue('phrase');
							subSocket.push({ data : msg ? msg : "&lt;null&gt;" });
							getElementById('phrase').value = '';
						}

						getElementById('connect').onclick = function(event) {
							connect();
						}

						getElementById('phrase').setAttribute('autocomplete', 'OFF');
						getElementById('phrase').onkeyup = function(event) {
							var keyc = getKeyCode(event);
							if (keyc == 13 || keyc == 10) {
								sendMessage();
								return false;
							}
							return true;
						};
						getElementById('send_message').onclick = function(event) {
							sendMessage();
							return false;
						};

					});
</script>
<style type='text/css'>
body {
	font-family: sans-serif;
	font-size: 11px;
}
div.hidden {
	display: none;
}
</style>
</head>
<body>
	<div id='pubsub'>
		Broadcaster associated with the user session: <strong><%=broadcasterId%></strong>
	</div>
	<h2>Select transport to use for subscribing</h2>
	<h3>You can change the transport any time.</h3>
	<div id='select_transport'>
		<select id="transport">
			<option id="autodetect" value="websocket">autodetect</option>
			<option id="jsonp" value="jsonp">jsonp</option>
			<option id="long-polling" value="long-polling">long-polling</option>
			<option id="streaming" value="streaming">http streaming</option>
			<option id="websocket" value="websocket">websocket</option>
		</select> <input id='connect' class='button' type='submit' name='connect'
			value='Connect' />
	</div>
	<br />

	<div id='sendMessage' class="hidden">
		<h2 id="s_h">Publish message</h2>
		<p>You can also publish message to any connected broadcaster using following syntax: <strong><code>@/broadcaster/broadcasterId message</code></strong></p>
		<p>e.g.: <strong><code>@<%=broadcasterId%> hello world</code></strong></p>
		<input id='phrase' type='text' size='60' /> 
		<input id='send_message' type='submit' name='Publish' value='Publish' />
		<br/>
		<h2>Real-time Broadcaster Response:</h2>
		<ul></ul>
	</div>

	
</body>
</html>