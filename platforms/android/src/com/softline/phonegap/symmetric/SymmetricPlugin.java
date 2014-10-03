package com.softline.phonegap.symmetric;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.text.Html;
import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

import com.softline.symmetric.android.SymmetricEngine;

public class SymmetricPlugin extends CordovaPlugin {
	private static final String START_ENGINE = "startEngine";
	private static final String STOP_ENGINE = "stopEngine";
	private static final String REGISTRATION_URL = "registration_url";
	private static final String GROUP_ID = "group_id";
	private static final String DEVICE_ID = "device_id";
	private static final String DATABASE_NAME = "database_name";
	private static final String INTENT_URI = "intent_uri";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		try {
			Context cordovaCtx = this.cordova.getActivity();

			if (action.equals(START_ENGINE)) {
				// Parse the arguments
				JSONObject obj = args.getJSONObject(0);
				Uri registrationUrl = obj.has(REGISTRATION_URL) ? Uri.parse(obj.getString(REGISTRATION_URL)) : null;
				String groupId = obj.has(GROUP_ID) ? obj.getString(GROUP_ID) : null;
				String deviceId = obj.has(DEVICE_ID) ? obj.getString(DEVICE_ID) : null;
				String databaseName = obj.has(DATABASE_NAME) ? obj.getString(DATABASE_NAME) : null;

				this.cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						SymmetricEngine symEngine = new SymmetricEngine(cordovaCtx);
						String intentUri = symEngine.start(registrationUrl, groupId, deviceId, databaseName, 1);
						callbackContext.success(intentUri);
					}
				});

				return true;
			}
			else if (action.equals(STOP_ENGINE)) {
				JSONObject obj = args.getJSONObject(0);
				String intentUri = obj.has(INTENT_URI) ? obj.getString(INTENT_URI) : null;

				this.cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						SymmetricEngine symEngine = new SymmetricEngine(cordovaCtx);
						symEngine.stop(intentUri);
					}
				});

				return true;
			}

			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
}