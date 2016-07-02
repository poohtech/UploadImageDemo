package com.example.uploadimagedemo.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.uploadimagedemo.util.Config;
import com.example.uploadimagedemo.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class WebInterface {

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
            return false;
        }
    }

    public static ApiResponse doPost(String url,
                                     ArrayList<BasicNameValuePair> params) throws Exception {
        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpClient httpClient = null;
        HttpParams httpParameters = null;
        HttpPost httppost = null;

        Log.print("WebInterface :: dopost() :; URL", url);
        Log.debug("WebInterface :: dopost() :; URL", url);

        // set timeout
        httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                Config.TIMEOUT_CONNECTION);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams
                .setSoTimeout(httpParameters, Config.TIMEOUT_SOCKET);

        httpClient = new DefaultHttpClient(httpParameters);
        httppost = new HttpPost(url);

        httppost.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse httpResponse = httpClient.execute(httppost);

        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();

        if (apiResponse.code == HttpStatus.SC_OK) {
            httpentity = httpResponse.getEntity();
            apiResponse.response = new String(EntityUtils.toString(httpentity));
        }

        Log.print("WebInterface :: dopost() :: ", " Code ::: "
                + apiResponse.code + " <BR/> Response :: "
                + apiResponse.response);
        Log.debug("WebInterface :: dopost() :: ", " Code ::: "
                + apiResponse.code + " <BR/> Response :: "
                + apiResponse.response);

        // release
        httpentity = null;
        httpResponse = null;
        httppost = null;
        httpClient = null;

        return apiResponse;
    }

    public static ApiResponse doGet(String url) throws Exception {
        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpGet httpGet = null;

        Log.print("WebInterface :: doGet() :; URL", url);
        Log.debug("WebInterface :: doGet() :; URL", url);

        HttpClient httpclient = new DefaultHttpClient();

        url = url.replace(" ", "%20");

        Log.print(WebInterface.class + "::doGet()",
                "URL : " + url.replace(" ", "%20"));

        httpGet = new HttpGet(url);

        HttpResponse httpResponse = httpclient.execute(httpGet);

        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();

        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            httpentity = httpResponse.getEntity();
            apiResponse.response = new String(EntityUtils.toString(httpentity));
        }

        Log.print("WebInterface :: doGet() :: ", " Code ::: "
                + apiResponse.code + " <BR/> Response :: "
                + apiResponse.response);
        Log.debug("WebInterface :: doGet() :: ", " Code ::: "
                + apiResponse.code + " <BR/> Response :: "
                + apiResponse.response);

        // Release
        httpResponse = null;
        httpclient = null;
        httpGet = null;
        httpentity = null;

        return apiResponse;
    }

    public static ApiResponse doWS(String url, String strRequest)
            throws Exception {

        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        StringEntity input = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        Log.print(WebInterface.class + "::doWS()", "URL : " + url);
        Log.print(WebInterface.class + "::doWS()", "Request : " + strRequest);

        input = new StringEntity(strRequest);
        input.setContentType("application/json");
        httppost.setEntity(input);
        HttpResponse response = httpclient.execute(httppost);

        apiResponse = new ApiResponse();

        apiResponse.code = response.getStatusLine().getStatusCode();

        if (apiResponse.code == HttpStatus.SC_OK) {
            apiResponse.code = 200;
            httpentity = response.getEntity();
            apiResponse.response = new String(EntityUtils.toString(httpentity));
        }

        Log.print(WebInterface.class + "::doWS()", "API Response :: Code "
                + apiResponse.code + " :: Response: " + apiResponse.response);

        // release
        httpentity = null;
        response = null;
        httppost = null;
        httpclient = null;

        return apiResponse;
    }

    public static String readStream(InputStream is) throws IOException {
        int ch = 0;
        String str = new String();

        while ((ch = is.read()) != -1) {
            str += (char) ch;
        }

        is.close();

        return str;
    }

    public static InputStream download(String paraURL) throws IOException {
        URL url = null;
        HttpURLConnection conn = null;

        url = new URL(paraURL);
        conn = (HttpURLConnection) url.openConnection();

        if ((conn).getResponseCode() == HttpURLConnection.HTTP_OK)
            return conn.getInputStream();
        else
            return null;
    }

    // Show the Custom Toast message when success is false.
    public static void showAPIErrorAlert(Context context, String errorType,
                                         String errorMessage) {
        if (Config.ERROR_NETWORK.equals(errorType)) {
//            AlertDailogView.showAlert(context, "No Internet Available", "OK",
//                    true).show();
            // Utils.showTost(context, "No Internet", "No Internet Available",
            // R.drawable.icon_network_error, 3);
        } else if (Config.ERROR_API.equals(errorType)) {
//            AlertDailogView.showAlert(context, "API Error", "OK", true).show();
            // Utils.showTost(context, "API Error", errorMessage,
            // R.drawable.icon_warning, 3);
        } else {
//            AlertDailogView.showAlert(context, "Unexpected Error", "OK", true)
//                    .show();
            // Utils.showTost(context, "Unexpected Error", "Unexpected Error",
            // R.drawable.icon_warning, 3);
        }
    }
}