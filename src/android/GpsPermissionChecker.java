package com.hotelbird.gpsPermissionChecker;

import android.content.Context;
import android.content.pm.PackageManager;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class GpsPermissionChecker extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("checkGps")) {
            this.checkGpsPermission(callbackContext);
            return true;
        }
        return false;
    }

    private void checkGpsPermission(CallbackContext callbackContext) {
        String gpsPermissionName = "android.permission.ACCESS_FINE_LOCATION";
        Context context = this.cordova.getActivity().getApplicationContext();
        int res = context.checkCallingOrSelfPermission(gpsPermissionName);
        if (res == PackageManager.PERMISSION_GRANTED){
            callbackContext.success("permission GPS given!!!");
        } else {
            callbackContext.error("permisson GPS not given!!!");
        }
    }
}
