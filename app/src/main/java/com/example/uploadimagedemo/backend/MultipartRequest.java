package com.example.uploadimagedemo.backend;

import android.content.Context;

import com.example.uploadimagedemo.R;
import com.example.uploadimagedemo.util.Config;
import com.example.uploadimagedemo.util.Log;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;

public class MultipartRequest {

	public Context caller;
	public MultipartBuilder builder;
	private OkHttpClient client;

	public MultipartRequest(Context caller) {
		this.caller = caller;

		this.builder = new MultipartBuilder();
		this.builder.type(MultipartBuilder.FORM);

		this.client = new OkHttpClient();
	}

	public void addString(String name, String value) {
		System.out.println("::::: addStrings mParams ::: name : "+name +"::Value : "+value);
		this.builder.addFormDataPart(name, value);
	}

	public void addFile(String name, String filePath, String fileName) {
		System.out.println("::::: addFile mParams ::: name : "+name +"::File path : "+filePath+"::Value : "+fileName);
		this.builder.addFormDataPart(name, fileName, RequestBody.create(
				MediaType.parse("image/jpeg"), new File(filePath)));
	}

	public String execute(String url) {
		RequestBody requestBody = null;
		Request request = null;
		Response response = null;

		int code = 200;
		String strResponse = null;

		try {

			requestBody = this.builder.build();

			request = new Request.Builder().header("AUTH_KEY", Config.API_KEY)
					.url(url).post(requestBody).build();
			System.out.println("=====request========="+request);

			response = client.newCall(request).execute();
			
			System.out.println("=====response========="+response);

			if (!response.isSuccessful())
				throw new IOException();

			code = response.networkResponse().code();


			if (response.isSuccessful()) {
				strResponse = response.body().string();

			} else if (code == HttpStatus.SC_NOT_FOUND) {
				// ** "Invalid URL or Server not available, please try again" */

				strResponse = caller.getResources().getString(
						R.string.error_invalid_URL);
			} else if (code == HttpStatus.SC_REQUEST_TIMEOUT) {
				// * "Connection timeout, please try again", */

				strResponse = caller.getResources().getString(
						R.string.error_timeout);
			} else if (code == HttpStatus.SC_SERVICE_UNAVAILABLE) {
				// *
				// "Invalid URL or Server is not responding, please try again",
				// */

				strResponse = caller.getResources().getString(
						R.string.error_server_not_responding);
			}

		} catch (Exception e) {
			Log.error("Exception", e);
			Log.print(e);
		} finally {
			requestBody = null;
			request = null;
			response = null;
			builder = null;
			if (client != null)
				client = null;
			System.gc();

		}
		return strResponse;
	}

}