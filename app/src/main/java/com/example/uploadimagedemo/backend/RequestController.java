package com.example.uploadimagedemo.backend;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.uploadimagedemo.util.LruBitmapCache;

public class RequestController extends Application {

	private static final String TAG = RequestController.class.getSimpleName();
	private RequestQueue mRequestQueue = null;
	private ImageLoader mImageLoader;
	private static RequestController mInstance = null;

	// private String pushId;
	// private ParseInstallation installation = null;
	// private ParseACL defaultACL = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// Initailaze parse object
		// try {
		// Parse.initialize(this, Config.APPLICATION_ID, Config.CLIENT_KEY);
		// installation = ParseInstallation.getCurrentInstallation();
		// pushId = Utils.getDeviceID(this);
		// installation.put("UniqueId", pushId);
		//
		// defaultACL = new ParseACL();
		// defaultACL.setPublicReadAccess(true);
		// defaultACL.setPublicWriteAccess(true); // objects created are
		// // writable
		// ParseACL.setDefaultACL(defaultACL, true);
		// installation.saveInBackground();
		// // PushService.setDefaultPushCallback(this, SplashActivity.class);
		// ParsePush.subscribeInBackground(pushId);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// installation =null;
		// defaultACL=null;
		// System.gc();
		// }
		// Log.print("Uniqueid ::  " + pushId);
		// Log.print("installation object id :: " + installation.getObjectId());
	}

	public static synchronized RequestController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		System.out.println(":::: request controoller :: "+ tag + " :: "+ mRequestQueue != null ? "not null": "null");
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}
