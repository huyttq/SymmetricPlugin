package com.softline.symmetric.android;

import java.net.URISyntaxException;
import java.util.Properties;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class SymmetricEngine {
	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	//private String mCreateScript;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		//private String mCreateScript;

        DatabaseHelper(Context context, String databaseName, int version) {
            super(context, databaseName, null, version);
            //this.mCreateScript = createScript;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);*/
        }
    }

	public SymmetricEngine(Context ctx) {
        this.mCtx = ctx;
        //this.mCreateScript = createScript;
    }

	public String start(Uri registrationUrl, String groupId, String deviceId, String databaseName, int databaseVersion) throws SQLException {
    	mDbHelper = new DatabaseHelper(mCtx, databaseName, databaseVersion);
        final String HELPER_KEY = "SymmetricAndroidHelperKey";

	    // Register the database helper, so it can be shared with the SymmetricService
	    SQLiteOpenHelperRegistry.register(HELPER_KEY, mDbHelper);
	    Intent intent = new Intent(mCtx, SymmetricService.class);

    	// Notify the service of the database helper key
	    intent.putExtra(SymmetricService.INTENTKEY_SQLITEOPENHELPER_REGISTRY_KEY, HELPER_KEY);
	    intent.putExtra(SymmetricService.INTENTKEY_REGISTRATION_URL, registrationUrl.toString());
	    intent.putExtra(SymmetricService.INTENTKEY_EXTERNAL_ID, deviceId);
	    intent.putExtra(SymmetricService.INTENTKEY_NODE_GROUP_ID, groupId);
	    intent.putExtra(SymmetricService.INTENTKEY_START_IN_BACKGROUND, true);

	    Properties properties = new Properties();
	    // initial load existing notes from the Client to the Server
	    properties.setProperty(ParameterConstants.AUTO_RELOAD_REVERSE_ENABLED, "true");
	    intent.putExtra(SymmetricService.INTENTKEY_PROPERTIES, properties);

	    mCtx.startService(intent);
	    return intent.toUri(0);
    }

	public void stop(String intentUri) {
        try {
			mCtx.stopService(Intent.parseUri(intentUri, 0));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
