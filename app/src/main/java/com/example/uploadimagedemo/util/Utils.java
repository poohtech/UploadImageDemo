package com.example.uploadimagedemo.util;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Utils {

    public static void systemUpgrade(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        int level = Integer.parseInt(Pref.getValue(context, "LEVEL", "0"));

        if (level == 0) {
            dbHelper.upgrade(level);

            // Create not confirmed order
            level++;

        }
        Pref.setValue(context, "LEVEL", level + "");
    }

    /**
     * Check Connectivity of network.
     */
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
            Log.error("Exception", e);
            return false;
        }

    }

    /**
     * Set the Current Language which is selected in your device.
     */
    public static String setCurrentLanguage(Context context) {

        String lang = Locale.getDefault().getLanguage();
        // Pref.setValue(context, Config.PREF_CURRENT_LANGUAGE, lang);

        if (!lang.equals("")
                && (lang.equals("en") || lang.equals("es") || lang.equals("pt")
                || lang.equals("eng") || lang.equals("spa") || lang
                .equals("por"))) {
            return lang;
        } else {
            lang = "en";
            return lang;
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Regular.ttf");
        } else if (tag == 200) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Bold.ttf");
        } else if (tag == 300) {
            return Typeface.createFromAsset(context.getAssets(),
                    "fontawesome-webfont.ttf");
        }
        return Typeface.DEFAULT;
    }

    /* Compress Image */
    public static String compressImage(String imageUri, Context context) {

        System.out.println(" -----------Utils imageUri :::: " + imageUri);

        String filePath = getRealPathFromURI(imageUri, context);

        System.out.println(" -----------Utils filePath :::: " + filePath);

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        System.out.println("actualHeight :: " + actualHeight);
        System.out.println(" actualWidth :: " + actualWidth);

        float maxWidth = 640.0f;
        float maxHeight = 800.0f;

		/*
         * if (actualWidth > actualHeight) { maxHeight = 800.0f; maxWidth =
		 * 640.0f; System.out
		 * .println(" =================== LandScape ================"); } else {
		 * maxWidth= 800.0f; maxHeight = 640.0f;
		 * System.out.println(" =================== Portrait================");
		 * }
		 */

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            System.out
                    .println("----------------Utils orientation--------------:"
                            + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                System.out
                        .println("---------------Utils orientation == 6--------------"
                                + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                System.out
                        .println("---------------Utils orientation == 3--------------"
                                + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                System.out
                        .println("---------------Utils orientation == 8--------------"
                                + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // scaledBitmap = Bitmap
        // .createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
        // scaledBitmap.getHeight(), matrix, true);

        FileOutputStream out = null;
        // String filename = getFilename();
        String filename = "";
        try {
            Storage.verifyDataPath();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        filename = Config.DIR_USERDATA + "/image_" + System.currentTimeMillis()
                + ".png";

        System.out.println("---------------Utils filename--------------"
                + filename);

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    private static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null,
                null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    /**
     * ImageFile save Function.
     */
    public static void createDirectoryAndSaveFile(Bitmap imageToSave,
                                                  String fileName) {
        File file = new File(fileName);

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        imageToSave = null;
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}
