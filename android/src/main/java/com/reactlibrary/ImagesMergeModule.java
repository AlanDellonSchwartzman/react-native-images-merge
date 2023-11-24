package com.reactlibraryimagesmerge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImagesMergeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public ImagesMergeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ImagesMerge";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void mergeImages(ReadableArray imgs, String orient, final Promise promise) {
        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

        ArrayList<String> arr = new ArrayList<>();

        // Log.d("LOGCAT", "INIT MERGE -----");
        for (int i = 0; i < imgs.size(); i++) {
            final ReadableMap arg_object = imgs.getMap(i);
            final String uri = arg_object.getString("uri");
            Bitmap imgBitmap = null;

            if (uri.startsWith("file:///")) {
                String filePath = uri.substring(7);
                imgBitmap = BitmapFactory.decodeFile(filePath);
            }
            // else if (uri.startsWith("http")) {
            // imgBitmap = downloadBitmapFromUrl(uri);
            // }

            imgBitmap = rotateBitmap(imgBitmap);
            bitmapArray.add(imgBitmap);
        }

        Integer width = 0;
        Integer height = 0;

        for (int i = 0; i < bitmapArray.size(); i++) {
            if ("vertical".equals(orient)) {
                height += bitmapArray.get(i).getHeight();
                if (bitmapArray.get(i).getWidth() > width) {
                    width = bitmapArray.get(i).getWidth();
                }
            } else {
                width += bitmapArray.get(i).getWidth();
                if (bitmapArray.get(i).getHeight() > height) {
                    height = bitmapArray.get(i).getHeight();
                }
            }
        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        Integer lastPos = 0;

        for (int i = 0; i < bitmapArray.size(); i++) {
            if ("vertical".equals(orient)) {
                canvas.drawBitmap(bitmapArray.get(i), 0, lastPos, new Paint(Paint.ANTI_ALIAS_FLAG));
                lastPos += bitmapArray.get(i).getHeight();
            } else {
                canvas.drawBitmap(bitmapArray.get(i), lastPos, 0, new Paint(Paint.ANTI_ALIAS_FLAG));
                lastPos += bitmapArray.get(i).getWidth();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        result.recycle();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        promise.resolve(encoded);
    }

    // private Bitmap downloadBitmapFromUrl(String urlString) {
    // try {
    // URL url = new URL(urlString);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // connection.setDoInput(true);
    // connection.connect();
    // InputStream input = connection.getInputStream();
    // Bitmap bitmap = BitmapFactory.decodeStream(input);
    // connection.disconnect();
    // return bitmap;
    // } catch (Exception e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    private Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
