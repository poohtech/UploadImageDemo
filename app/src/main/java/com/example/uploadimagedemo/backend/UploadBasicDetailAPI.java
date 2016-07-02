package com.example.uploadimagedemo.backend;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.example.uploadimagedemo.bean.PropertyBean;
import com.example.uploadimagedemo.util.Config;
import com.example.uploadimagedemo.util.Log;
import com.example.uploadimagedemo.util.Utils;

import org.json.JSONObject;

import java.io.File;


public class UploadBasicDetailAPI extends AsyncTask<Void, Void, Integer> {
    public Context mCaller;
    public ResponseListener handler;
    public String mesg;
    public MultipartRequest multipartReq;
    public File file = null;
    private PropertyBean propertyBean;

    public UploadBasicDetailAPI(Context caller, PropertyBean propertyBean,
                                ResponseListener handler) {
        this.mCaller = caller;
        this.handler = handler;
        this.propertyBean = propertyBean;

    }

    @Override
    protected Integer doInBackground(Void... params) {
        int result = -1;

        try {
            multipartReq = new MultipartRequest(mCaller);

            if (this.propertyBean.cover_image != "") {
                System.out.println("-----API propertyBean.cover_image----"
                        + this.propertyBean.cover_image);
                file = new File(this.propertyBean.cover_image);
                if (file.exists()) {
                    multipartReq.addFile("cover_image", file.toString(),
                            file.getName());
                    System.out.println("-----API file.toString()-----"
                            + file.toString());
                    System.out.println("-----API file.getName()-----"
                            + file.getName());
                }
            }

            System.out.println("******API image********"
                    + propertyBean.cover_image);

            multipartReq.addString("client_id", String.valueOf(7432));
            multipartReq.addString("sub_cat_id",
                    String.valueOf(3409));
            multipartReq.addString("sub_sub_cat_id",
                    String.valueOf(3412));
            multipartReq.addString("sub_sub_sub_cat_id",
                    String.valueOf(3419));
            multipartReq.addString("title", "ASDFGHJJK");
            multipartReq.addString("description",
                    "dbfsdbfjhdsbgjfbgdsjf");
            multipartReq.addString("price", String.valueOf(12121312));
            multipartReq.addString("bed", "1");
            multipartReq.addString("bath", "1");
            multipartReq.addString("sqm", "10000");
            multipartReq.addString("garage", "1");
            multipartReq.addString("product_id",
                    String.valueOf(0));

            Config.API_ADD_PROPERTY_BASIC_DETAIL = Config.HOST
                    + "/properties/saveproperty/";
            Config.API_ADD_PROPERTY_BASIC_DETAIL = Config.API_ADD_PROPERTY_BASIC_DETAIL
                    + "?lang=" + Utils.setCurrentLanguage(mCaller);
            result = parse(multipartReq
                    .execute(Config.API_ADD_PROPERTY_BASIC_DETAIL));
        } catch (Exception e) {
            result = -1;
            mesg = "Unable to upload please try again.";

            Log.print(e);
            Log.error(this.getClass() + "", e);
        }
        return result;
    }

    protected void onPostExecute(Integer result) {

        if (result == 0) {// successful
            this.handler.onResponce(Config.API_ADD_PROPERTY_BASIC_DETAIL,
                    Config.API_SUCCESS, propertyBean);
        } else if (result > 0) {
            // WebInterface.showAPIErrorAlert(mCaller, "Alert", this.mesg);
//            AlertDailogView.showAlert(mCaller,
//                    mCaller.getResources().getString(R.string.Alert), mesg,
//                    mCaller.getResources().getString(R.string.tryAgain), true,
//                    null).show();
            this.handler.onResponce(Config.API_ADD_PROPERTY_BASIC_DETAIL,
                    Config.API_FAIL, null);
        } else {
            if (this.mCaller instanceof Activity) {
                if (result == -1 || result == -2 || result == -3) {
                    WebInterface.showAPIErrorAlert(mCaller,
                            Config.ERROR_NETWORK, this.mesg);
                } else if (result == -4) {
                    WebInterface.showAPIErrorAlert(mCaller,
                            Config.ERROR_UNKNOWN, this.mesg);
                }
                this.handler.onResponce(Config.API_ADD_PROPERTY_BASIC_DETAIL,
                        Config.API_FAIL, this.mesg);
            }
        }
    }

    public int parse(String response) {
        int code = 0;
        // String mesg = null;
        JSONObject jsonDoc = null;
        try {
            System.out.println("======response========" + response);
            jsonDoc = new JSONObject(response);
            code = jsonDoc.getInt("code");
            mesg = jsonDoc.getString("message");
            if (code == 0) {

                if (jsonDoc.has("product_id")) {
//                    propertyBean.id = jsonDoc.getInt("product_id");
                }
            }
        } catch (Exception e) {
            code = -4;
            Log.error(this.getClass() + " :: parse()", e);
            e.printStackTrace();
        } finally {
            response = null;
            /** release variables */
            jsonDoc = null;
        }
        return code;
    }
}