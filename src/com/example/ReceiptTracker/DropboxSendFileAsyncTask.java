package com.example.ReceiptTracker;
import java.io.File;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client.DropboxAPI;
import com.dropbox.client.DropboxAPI.Config;


public class DropboxSendFileAsyncTask extends AsyncTask<Void, Void, Integer> {
    private static final String TAG = "DropboxSendFileAsyncTask";
    
    final static private byte[] OBS_CREDS= {
    	0x77, 0x33, 0x78, 0x31, 0x63, 0x39, 0x6c, 0x37, 0x6a, 0x75, 
    	0x75, 0x68, 0x7a, 0x6c, 0x75, 0x67, 0x34, 0x33, 0x30, 0x6e, 
    	0x72, 0x37, 0x66, 0x63, 0x63, 0x65, 0x63, 0x68, 0x76, 0x30
    };

    private String mUser;
    private String mPassword;
    private File mFile;
    private DropboxAPI api;
    
    /**
     * AsyncTask that logs into Dropbox, sends the file, and logs out
     * @param user user's account
     * @param password user's email
     * @param file file to send
     */
    public DropboxSendFileAsyncTask(String user, String password, File file) {
        super();
        api = new DropboxAPI();
        mUser = user;
        mPassword = password;
        mFile = file;
        
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
        	int success = DropboxAPI.STATUS_NONE;
        	if (!api.isAuthenticated()) {
        		DropboxAPI.Config config = api.authenticate(buildConfig(), mUser, mPassword);
	            success = config.authStatus;

	            if (success != DropboxAPI.STATUS_SUCCESS) {
	            	return success;
	            }
        	}
        	DropboxAPI.Account account = api.accountInfo();

        	if (!account.isError()) {
        		api.putFile("dropbox", "/", mFile);
        		api.deauthenticate();
        		return DropboxAPI.STATUS_SUCCESS;
        	} else {
        		Log.e(TAG, "Account info error: " + account.httpCode + " " + account.httpReason);
        		return DropboxAPI.STATUS_FAILURE;
        	}
        	
        } catch (Exception e) {
            Log.e(TAG, "Error in logging in.", e);
            return DropboxAPI.STATUS_NETWORK_ERROR;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == DropboxAPI.STATUS_SUCCESS) {
        	
        } else {
        	if (result == DropboxAPI.STATUS_NETWORK_ERROR) {
        		// login failed w/ error. mDropboxSample.showToast("Network error: " + mConfig.authDetail);
        	} else {
        		// login failed because bad login.
        	}
        }
    }

    private Config buildConfig() {
    	String secret = new String(OBS_CREDS);
    	DropboxAPI.Config config = api.getConfig(null, false);
    	config.consumerKey=secret.substring(0, 15);
    	config.consumerSecret=secret.substring(15);
    	config.server="api.dropbox.com";
    	config.contentServer="api-content.dropbox.com";
    	config.port=80;
    	return config;
    }
    
}
