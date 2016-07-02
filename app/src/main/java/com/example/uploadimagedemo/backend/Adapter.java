package com.example.uploadimagedemo.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uploadimagedemo.util.Config;
import com.example.uploadimagedemo.util.Pref;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Adapter {

    public Context caller;
    public int httpStatusCode;

    public Adapter(Context caller) {
        this.caller = caller;
    }

    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
//			Log.error("Exception", e);
            return false;
        }
    }

    public static boolean wifiStatus(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = null;
            if (cm != null) {
                networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            }

            return networkInfo == null ? false : networkInfo.isConnected();
        } catch (Exception e) {
//			Log.error("Exception", e);
            return false;
        }
    }

    public void doPost(final String tag, String url,
                       HashMap<String, String> mParams,
                       final APIResponseListener responseListener,
                       final ErrorListener errorListener) {
        try {
            System.out.println(" PARA<S ::: " + mParams);
            ApiRequest apiReq = new ApiRequest(Method.POST, url, mParams,
                    new Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            System.out.println(tag + response);
                            System.out.println(this.getClass()
                                    + ":: Response BACKEND:: " + tag + " :: " + response);

                            responseListener.onResponse(response);
                        }
                    }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    System.out.println("Error :: " + tag + ":: "
                            + error.toString());

                    if (error.getMessage() == null) {
                        // Utils.showTost(
                        // caller,
                        // "Error",
                        // "Unable to get response from the server, please try again",
                        // R.drawable.icon_error, 7);
                    } else {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 403) {
                                // Utils.showTost(
                                // caller,
                                // "Error",
                                // "The request is for something forbidden. Authorization will not help",
                                // R.drawable.icon_error, 7);
                            } else if (error.networkResponse.statusCode == 404) {
                                // Utils.showTost(
                                // caller,
                                // "Error",
                                // "The server has not found anything matching the URI given",
                                // R.drawable.icon_error, 7);
                            } else if (error.networkResponse.statusCode == 500) {
                                // Utils.showTost(
                                // caller,
                                // "Error",
                                // "The server encountered an unexpected condition",
                                // R.drawable.icon_error, 7);
                            } else {
                                // Utils.showTost(caller, "Error",
                                // error.getMessage(),
                                // R.drawable.icon_error, 7);
                            }
                        }
                    }

                    System.out.println("Error :: " + tag + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
            });

            System.out.println("============ 111=== api req " + apiReq);
            // Adding request to request queue
            RequestController.getInstance().addToRequestQueue(apiReq, tag);
        } catch (Exception e) {
//            Log.error("Exception", e);
        }
    }

    public void doPut(final String tag, String url,
                      HashMap<String, String> mParams,
                      final APIResponseListener responseListener,
                      final ErrorListener errorListener) {
        try {
            System.out.println(" PARA<S ::: " + mParams);
            ApiRequest apiReq = new ApiRequest(Method.PUT, url, mParams,
                    new Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            System.out.println(tag + response);
                            System.out.println(tag + response);

                            responseListener.onResponse(response);
                        }
                    }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    System.out.println("Error :: " + tag + ":: "
                            + error.toString());

                    if (error.getMessage() == null) {
                        // Utils.showTost(
                        // caller,
                        // "Error",
                        // "Unable to get response from the server, please try again",
                        // R.drawable.icon_error, 7);
                    } else {
                        if (error.networkResponse.statusCode == 403) {
                            // Utils.showTost(
                            // caller,
                            // "Error",
                            // "The request is for something forbidden. Authorization will not help",
                            // R.drawable.icon_error, 7);
                        } else if (error.networkResponse.statusCode == 404) {
                            // Utils.showTost(
                            // caller,
                            // "Error",
                            // "The server has not found anything matching the URI given",
                            // R.drawable.icon_error, 7);
                        } else if (error.networkResponse.statusCode == 500) {
                            // Utils.showTost(
                            // caller,
                            // "Error",
                            // "The server encountered an unexpected condition",
                            // R.drawable.icon_error, 7);
                        } else {
                            // Utils.showTost(caller, "Error",
                            // error.getMessage(),
                            // R.drawable.icon_error, 7);
                        }
                    }

                    System.out.println("Error :: " + tag + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
            });

            System.out.println("============ 111=== api req " + apiReq);
            // Adding request to request queue
            RequestController.getInstance().addToRequestQueue(apiReq, tag);
        } catch (Exception e) {
//            Log.error("Exception", e);
        }
    }

    public void doGet(final String tag, String url,
                      HashMap<String, String> mParams,
                      final APIResponseListener responseListener,
                      final ErrorListener errorListener) {
        try {
            ApiRequest apiReq = new ApiRequest(Method.GET, url, mParams,
                    new Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            System.out.println(tag + response);
                            System.out.println(tag + response);

                            responseListener.statusCode = httpStatusCode;
                            responseListener.onResponse(response);
                        }
                    }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    System.out.println("Error :: " + tag + error.getMessage());
                    System.out.println("Error :: " + tag + error.getMessage());

                    if (error.getMessage() == null) {
                        // Utils.showTost(
                        // caller,
                        // "Error",
                        // "Unable to get response from the server, please try again",
                        // R.drawable.icon_error, 7);
                    } else {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 403) {
                                // Utils.showTost(
                                // caller,
                                // "Error",
                                // "The request is for something forbidden. Authorization will not help",
                                // R.drawable.icon_error, 7);
                            } else if (error.networkResponse.statusCode == 404) {
                                // Utils.showTost(
                                // caller,
                                // "Error",
                                // "The server has not found anything matching the URI given",
                                // R.drawable.icon_error, 7);
                            } else if (error.networkResponse.statusCode == 500) {
                                // Utils.showTost(
                                // caller,
                                // "Error",
                                // "The server encountered an unexpected condition",
                                // R.drawable.icon_error, 7);
                            } else {
                                // Utils.showTost(caller, "Error",
                                // error.getMessage(),
                                // R.drawable.icon_error, 7);
                            }
                        }
                    }

                    System.out.println("Error :: " + tag + error.getMessage());
                    errorListener.onErrorResponse(error);
                }
            });

            // Adding request to request queue
            RequestController.getInstance().addToRequestQueue(apiReq, tag);
        } catch (Exception e) {
//            Log.error("Exception", e);
        }
    }

    public void doCancel(String tag) {
        try {
            RequestController.getInstance().cancelPendingRequests(tag);
        } catch (Exception e) {
//            Log.error("Exception", e);
        }
    }

    public class ApiRequest extends StringRequest {

        private Map<String, String> mParams;

        public ApiRequest(int method, String url,
                          HashMap<String, String> mParams,
                          Listener<String> responseListener, ErrorListener errorListener) {
            super(method, url, responseListener, errorListener);

            System.out.println(this.getClass() + " :: URL :: " + url);
            System.out.println(this.getClass() + " :: URL :: " + url);

            this.mParams = mParams;

//            this.mParams.put("key", Config.API_KEY);
//            this.mParams.put("version", Config.API_VERSION);

            System.out.println(" API ::: " + mParams);
            /*
             * SET - Timeout - Number of attemptes - Default 1
			 */
            this.setRetryPolicy(new DefaultRetryPolicy(Config.TIMEOUT_SOCKET,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        @Override
        public Map<String, String> getParams() {
            return mParams;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.android.volley.toolbox.StringRequest#parseNetworkResponse(com
         * .android.volley.NetworkResponse)
         */
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            // since we don't know which of the two underlying network vehicles
            // will Volley use, we have to handle and store session cookies
            // manually
            this.checkSessionCookie(response.headers);

            httpStatusCode = response.statusCode;

            return super.parseNetworkResponse(response);
        }

        /**
         * Checks the response headers for session cookie and saves it if it
         * finds it.
         *
         * @param headers Response Headers.
         */
        public void checkSessionCookie(Map<String, String> headers) {
            if (headers.containsKey(Config.SET_COOKIE_KEY)
                    && headers.get(Config.SET_COOKIE_KEY).startsWith(
                    Config.SESSION_COOKIE)) {
                String cookie = headers.get(Config.SET_COOKIE_KEY);
                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    Pref.setValue(caller, Config.PREF_SESSION_COOKIE, cookie);
                }
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see com.android.volley.Request#getHeaders()
         */
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = super.getHeaders();

            if (headers == null || headers.equals(Collections.emptyMap())) {
                headers = new HashMap<String, String>();
            }

            headers.put("AUTH_KEY", Config.API_KEY);

            this.addSessionCookie(headers);

            return headers;
        }

        /**
         * Adds session cookie to headers if exists.
         *
         * @param headers
         */
        public final void addSessionCookie(Map<String, String> headers) {
            String sessionId = Pref.getValue(caller,
                    Config.PREF_SESSION_COOKIE, "");
            if (sessionId.length() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append(Config.PREF_SESSION_COOKIE);
                builder.append("=");
                builder.append(sessionId);
                if (headers.containsKey(Config.COOKIE_KEY)) {
                    builder.append("; ");
                    builder.append(headers.get(Config.COOKIE_KEY));
                }
                headers.put(Config.COOKIE_KEY, builder.toString());
            }
        }
    }
}
