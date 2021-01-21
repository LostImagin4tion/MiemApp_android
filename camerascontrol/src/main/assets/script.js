var ws = new WebSocket('wss://media.auditory.ru:8443/player');
var video;
var webRtcPeer;
var state = null;
var isSeekable = false;
var currentVideoUrl;
var isWebSocketOpe

window.onload = function() {
	video = document.getElementById('video');
}

window.onbeforeunload = function() {
	ws.close();
}

ws.onopen = function(message) {
	Android.log('i' ,'Websocket ready to work. Now allowed to start translation');
	startStream(currentVideoUrl);
}

ws.onmessage = function(message) {
	var parsedMessage = JSON.parse(message.data);
	Android.log('i', 'Received message: ' + message.data);

	switch (parsedMessage.id) {
	case 'startResponse':
		startResponse(parsedMessage);
		break;
	case 'error':
		onError('Error message from server: ' + parsedMessage.message);
		break;
	case 'playEnd':
	case 'videoInfo':
	    Android.setLoading(false) // received successfully
		break;
	case 'iceCandidate':
		webRtcPeer.addIceCandidate(parsedMessage.candidate, function(error) {
			if (error)
				return Android.log('e', 'Error adding candidate: ' + error);
		});
		break;
	case 'seek':
		Android.log('i', parsedMessage.message);
		break;
	case 'position':
		break;
	case 'iceCandidate':
		break;
	default:
		onError('Unrecognized message', parsedMessage);
	}
}

function start(videoUrl) {
    Android.log('i', videoUrl);
    currentVideoUrl = videoUrl;
    Android.setLoading(true);

    // if WebSocket is already open - start instantly
    if (ws.readyState == WebSocket.OPEN) {
        startStream(currentVideoUrl);
    } else {
        Android.log('i', "Waiting for WebSocket to open");
    }
}

function startStream(videoUrl) {
	Android.log('i', 'Creating WebRtcPeer and generating local sdp offer ...');

	// Video and audio by default
	var userMediaConstraints = {
		audio : true,
		video : true
	}
	var options = {
		remoteVideo : video,
		mediaConstraints : userMediaConstraints,
		onicecandidate : onIceCandidate
	}

	Android.log('i', 'User media constraints' + userMediaConstraints);

	webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function(error) {
				if (error)
					return console.error(error);
				webRtcPeer.generateOffer(onOffer);
			});
}

function onOffer(error, offerSdp) {
	if (error)
		return Android.log('e', 'Error generating the offer');
	Android.log('i', 'Invoking SDP offer callback function ' + location.host);

	var message = {
		id : 'start',
		sdpOffer : offerSdp,
		videourl : currentVideoUrl
	}
	sendMessage(message);
}

function onError(error) {
	console.error(error);
}

function onIceCandidate(candidate) {
	Android.log('i', 'Local candidate' + JSON.stringify(candidate));

	var message = {
		id : 'onIceCandidate',
		candidate : candidate
	}
	sendMessage(message);
}

function startResponse(message) {
	Android.log('i', 'SDP answer received from server. Processing ...');

	webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
		if (error)
			return console.error(error);
	});
}

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	Android.log('i', 'Sending message: ' + jsonMessage);
	ws.send(jsonMessage);
}

function stop() {
	Android.log('i', 'Stopping video ...');
	if (webRtcPeer) {
		webRtcPeer.dispose();
		webRtcPeer = null;

		var message = {
			id : 'stop'
		}
		sendMessage(message);
	}
	Android.setLoading(false);
}