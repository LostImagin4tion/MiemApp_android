var pc = null;

function start(server_link) {

    Android.setLoading("true");
    var config = {
        sdpSemantics: 'unified-plan'
    };

    config.iceServers = [{urls: ['stun:stun.l.google.com:19302']}];
    pc = new RTCPeerConnection(config);
    // connect audio / video
    let video = document.getElementById('video');
    video.addEventListener("loadedmetadata", function() {
        let maxZoom = document.body.offsetHeight / document.getElementById('video').offsetHeight;
        Android.setMaxZoom(maxZoom);
        Android.setLoading("false")
    });
    pc.addEventListener('track', function(evt) {
        if (evt.track.kind == 'video') {
            video.srcObject = evt.streams[0];
        } else {
            document.getElementById('audio').srcObject = evt.streams[0];
        }

    });
    negotiate(server_link);
}

function negotiate(server_link) {
    Android.log("i", "negotiate " + server_link);
    pc.addTransceiver('video', {direction: 'recvonly'});
    pc.addTransceiver('audio', {direction: 'recvonly'});
    return pc.createOffer().then(function(offer) {
        return pc.setLocalDescription(offer);
    }).then(function() {
        return new Promise(function(resolve) {
            if (pc.iceGatheringState === 'complete') {
                resolve();
            } else {
                function checkState() {
                    if (pc.iceGatheringState === 'complete') {
                        pc.removeEventListener('icegatheringstatechange', checkState);
                        resolve();
                    }
                }
                pc.addEventListener('icegatheringstatechange', checkState);
            }
        });
    }).then(function() {
        var offer = pc.localDescription;
        return fetch(server_link, {
            body: JSON.stringify({
                sdp: offer.sdp,
                type: offer.type,
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST'
        });
    }).then(function(response) {
        return response.json();
    }).then(function(answer) {
        return pc.setRemoteDescription(answer);
    }).catch(function(e) {
        Android.setLoading("false");
        Android.log("e", "catch" + e);
    });
}

function stop() {
    pc.close();
}