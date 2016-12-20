var exec = require('cordova/exec');

exports.checkGps = function(arg0, success, error) {
    exec(success, error, "GpsPermissionChecker", "checkGps", [arg0]);
};
