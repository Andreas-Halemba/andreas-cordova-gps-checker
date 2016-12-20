package com.hotelbird.gpsPermissionChecker;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class GpsPermissionChecker extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("checkGps")) {
            this.coolMethod(callbackContext);
            return true;
        }
        return false;
    }

    private void checkGpsPermission(CallbackContext callbackContext) {

            callbackContext.success("Permission granted.");
            callbackContext.error("Expected one non-empty string argument.");
    }
}
