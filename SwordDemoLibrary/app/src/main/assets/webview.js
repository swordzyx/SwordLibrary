var jsBridge = ();

jsBridge.os = {
    "isAndroid": Boolean(navigator.userAgent.match(/android/ig)),
    "isIOS": Boolean(navigator.userAgent.match(/iphone|ipod|iOS|ig))
}

jsBridge.sendCommand = function(command, params) {
    var message = {
        "command": command
    }

    if (params && typeof params === "object") { // 支持传参
        message['params'] = params
    }


}