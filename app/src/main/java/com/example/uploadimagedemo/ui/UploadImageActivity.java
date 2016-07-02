package com.example.uploadimagedemo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uploadimagedemo.R;
import com.example.uploadimagedemo.backend.UploadBasicDetailAPI;
import com.example.uploadimagedemo.backend.ResponseListener;
import com.example.uploadimagedemo.bean.PropertyBean;
import com.example.uploadimagedemo.util.Config;
import com.example.uploadimagedemo.util.Log;
import com.example.uploadimagedemo.util.Storage;
import com.example.uploadimagedemo.util.Utils;

import java.io.File;
import java.io.IOException;

public class UploadImageActivity extends Activity implements ResponseListener,
        OnClickListener {

    private Button btnEditPhoto, btnSubmit;
    private ProgressDialog mProgressDialog;
    private RelativeLayout relLayoutImageDetail;
    private ImageView imgThumbnail;
    private TextView txtImgName;

    private UploadBasicDetailAPI uploadBasicDetailAPI = null;
    private PropertyBean propertyBean;

    private Intent intent;
    private InputMethodManager imm;

    private boolean isClick = false;

    /* Upload Pic */
    private static String oldFileName;
    private static Uri imgUri;
    private int CAMERA_CAPTURE = 1000;
    private int GET_FROM_GALLERY = 2000;
    private static String newImgName;
    private String newFileName = "";
    private Bitmap photo;
    String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_category);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        findViewById();

        propertyBean = new PropertyBean();

        btnEditPhoto.setOnClickListener(this);
        relLayoutImageDetail.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

    }

    private void findViewById() {

        btnEditPhoto = (Button) findViewById(R.id.btnEditPhoto);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        relLayoutImageDetail = (RelativeLayout) findViewById(R.id.relLayoutImageDetail);
        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        txtImgName = (TextView) findViewById(R.id.imgName);
    }


    private void callAPI() {
        if (Utils.isOnline(UploadImageActivity.this)) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            System.out
                    .println(" =============callAPI Before: propertyBean.image =========="
                            + propertyBean.cover_image);

            propertyBean.cover_image = filename;

            System.out
                    .println(" =============callAPI After: propertyBean.image =========="
                            + propertyBean.cover_image);

            uploadBasicDetailAPI = new UploadBasicDetailAPI(UploadImageActivity.this,
                    propertyBean, UploadImageActivity.this);
            uploadBasicDetailAPI.execute();

        } else {

            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onResponce(String tag, int result, Object obj) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        if (tag.equals(Config.API_ADD_PROPERTY_BASIC_DETAIL)) {
            if (result == Config.API_SUCCESS) {

                propertyBean = (PropertyBean) obj;


                Toast.makeText(this, "Updated successfully", Toast.LENGTH_LONG).show();

                /*
                 * when get different filename from server than rename that
				 * filename
				 */
//                System.out
//                        .println(" =============onResponce Before: propertyBean.image =========="
//                                + propertyBean.cover_image);
//                System.out
//                        .println(" =============onResponce Before: filename =========="
//                                + filename);
//                new File(filename).renameTo(new File(Config.DIR_USERDATA + "/"
//                        + new File(propertyBean.cover_image).getName()));
//                System.out
//                        .println(" =============onResponce After: propertyBean.image =========="
//                                + propertyBean.cover_image);
//                System.out
//                        .println(" =============onResponce After: filename =========="
//                                + filename);
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnEditPhoto:
                try {
                    Storage.verifyDataPath();
                    showImgAlert(UploadImageActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.relLayoutImageDetail:
                showImgAlert(UploadImageActivity.this);
                break;


            case R.id.btnSubmit:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            /* CallAPI for AddCategory */

                String result = validate();
                if (result != null) {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                } else {
                    callAPI();
                }
                break;

        }

    }

    @SuppressWarnings("unused")
    private String validate() {

        String valid = null;
        if (btnEditPhoto.getVisibility() == View.GONE
                && (txtImgName.getText().toString().trim() == null || txtImgName
                .getText().toString().trim().equals(""))) {
            valid = getResources()
                    .getString(R.string.validation_exp_upload_pic);
            txtImgName.requestFocus();
        } else if (btnEditPhoto.getVisibility() == View.VISIBLE
                && (btnEditPhoto.getText().toString().trim() == null || btnEditPhoto
                .getText().toString().trim().equals(""))) {
            valid = getResources()
                    .getString(R.string.validation_exp_upload_pic);
            btnEditPhoto.requestFocus();
        } else if (checkFileSize(filename) == false) {
            valid = getResources().getString(R.string.share_exp_img_validation);
        }
        return valid;

    }

    private boolean checkFileSize(String path) {
        File file = new File(path);
        long fileSizeInMB = 0; // Convert the KB to MegaBytes (1 MB =
        // 1024 KBytes)
        if (file.exists())
            fileSizeInMB = (file.length() / 1024) / 1024;

        if (fileSizeInMB < 1) {
            return true;
        } else {
            return false;
        }
    }

    private void showImgAlert(Context context) {

        final Dialog dialog = new Dialog(UploadImageActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.popup_dialog_upload_photo);
        dialog.setCancelable(false);

        final LinearLayout btn_gallerylft = (LinearLayout) dialog
                .findViewById(R.id.lay_1);
        final LinearLayout btn_camerargt = (LinearLayout) dialog
                .findViewById(R.id.lay_2);
        final Button cancel = (Button) dialog.findViewById(R.id.btn_cancle);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        btn_camerargt.setOnClickListener(new OnClickListener() {
            // Camera
            @Override
            public void onClick(View v) {
                try {

                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getPath();
                    // oldFileName = System.currentTimeMillis() + ".png";
                    oldFileName = Config.DIR_USERDATA + "/image_"
                            + System.currentTimeMillis() + ".png";

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, oldFileName);
                    imgUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);

                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent, CAMERA_CAPTURE);

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        });

        btn_gallerylft.setOnClickListener(new OnClickListener() {

            // Galley
            @Override
            public void onClick(View v) {
                intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, GET_FROM_GALLERY);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_CAPTURE) {
                // for Camera

                try {

                    System.out
                            .println(" -----------UploadImageActivity img uri :::: "
                                    + imgUri);

                    filename = Utils.compressImage(String.valueOf(imgUri),
                            UploadImageActivity.this);

                    btnEditPhoto.setVisibility(View.GONE);
                    relLayoutImageDetail.setVisibility(View.VISIBLE);
                    imgThumbnail.setImageBitmap(BitmapFactory
                            .decodeFile(filename));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == GET_FROM_GALLERY) {
                // Pick from Gallery

                try {

                    if (data != null) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver()
                                .query(selectedImage, filePathColumn, null,
                                        null, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            int columnIndex = cursor
                                    .getColumnIndex(filePathColumn[0]);
                            newImgName = cursor.getString(columnIndex);
                            cursor.close();
                            Log.print("-------------filePath :: " + newImgName);
                            if (newImgName != null
                                    && new File(newImgName).exists()) {

                                filename = Utils.compressImage(
                                        String.valueOf(newImgName),
                                        UploadImageActivity.this);

                            }
                        }

                        btnEditPhoto.setVisibility(View.GONE);
                        relLayoutImageDetail.setVisibility(View.VISIBLE);
                        imgThumbnail.setImageBitmap(BitmapFactory
                                .decodeFile(filename));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
