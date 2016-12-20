package com.hotelbird.gpsPermissionChecker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.webkit.PermissionRequest;

import com.assaabloy.mobilekeys.shaded.bouncycastle.util.Pack;
import com.hotelbird.hbapp.MainActivity;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class GpsPermissionChecker extends CordovaPlugin {

    private CallbackContext callbackContext;
    private Activity activity;
    private final String TAG = GpsPermissionChecker.class.getName();
    private Boolean comesFromSetting = false;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.activity = cordova.getActivity();
        this.comesFromSetting = false;
        if (action.equals("checkGps")) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                this.checkGpsPermission(this.activity, callbackContext);
                return true;
            } else {
                this.callbackContext.success("No need for GPS.");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        for (int r:grantResults){
            if (r == PackageManager.PERMISSION_DENIED) {
                callbackContext.error("permission not granted");
            } else {
                this.checkGpsActive(callbackContext);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {

        } else if (resultCode == Activity.RESULT_CANCELED){
            this.comesFromSetting = true;
            this.checkGpsActive(callbackContext);
        } else {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "GPS got not Activated."));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkGpsPermission(final Activity activity, CallbackContext callbackContext) {
        Context context = activity.getApplicationContext();
        int res = context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        if ( res == PackageManager.PERMISSION_GRANTED){
            this.checkGpsActive(callbackContext);
        } else {
            cordova.requestPermission(this, 1, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkGpsActive(CallbackContext callbackContext) {

        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Gps is enabled and ready to go.") );
        } else if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && this.comesFromSetting == false) {
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            cordova.startActivityForResult(this, myIntent, 1);
        } else {
            this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Gps not enabled after settings screen."));
        }
    }
}
