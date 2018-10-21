package com.bitvault.appstore.imagelib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoaders {

    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;

    public ImageLoaders(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    private ResponseObserver mObserver;

    /**
     * method for set listener for download image
     * @param observer
     */
    public void setResponseObserver(ResponseObserver observer) {
        mObserver = observer;
    }

    /**
     * set url to imageview to load image
     * @param url
     * @param imageView
     * @return
     */
    public Bitmap DisplayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url.replaceAll(" ", "%20"));
        Bitmap bitmap = memoryCache.get(url.replaceAll(" ", "%20"));
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else {
            queuePhoto(url.replaceAll(" ", "%20"), imageView);
        }
        return bitmap;
    }

    /**
     * add to queue load image
     * @param url
     * @param imageView
     */
    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url.replaceAll(" ", "%20"), imageView);
        executorService.submit(new PhotosLoader(p));
    }

    /**
     * get bitmap from imageview
     * @param url
     * @return
     */
    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url.replaceAll(" ", "%20"));

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null) {
            if(mObserver != null) {
                mObserver.onSuccess();
            }
            return b;
        }

        // from web
        try {
            Bitmap bitmap;
            URL imageUrl = new URL(url.replaceAll(" ", "%20"));
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            if(mObserver != null) {
                mObserver.onSuccess();
            }
            return bitmap;
        } catch (Throwable ex) {
            if(mObserver != null) {
                mObserver.onError();
            }
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception ignored) {
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u.replaceAll(" ", "%20");
            imageView = i;
        }
    }

    /**
     * load image in thread
     */
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    /**
     * reused cache
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * interface for image download response
     */
    public interface ResponseObserver
    {
        public void onError();
        public void onSuccess();
    }


}