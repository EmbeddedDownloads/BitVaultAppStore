package com.bitvault.appstore.imagelib;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;


public class FileCache {

    private Context mContext = null;
    public FileCache(Context ctx) {
        mContext = ctx;
        getCacheDir();
    }

    /**
     * Get the application cache root.
     *
     * @return The application cache root.
     */
    public File getCacheDir() {
        return getCacheDir(null);
    }

    /**
     * Get a cache directory for a specific task.
     *
     * @param subdir
     *            The name of the task, will result as a subdirectory of the
     *            cache root.
     * @return The cache directory for this task.
     */
    public File getCacheDir(String subdir) {
        if (subdir == null) {
            subdir = "";
        }

        // Default root is the application context cache.
        File result = new File(mContext.getCacheDir(), subdir);

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // If an external storage is available, use it as it will prevent
            // from overloading the internal memory.
            result = new File(Environment.getExternalStorageDirectory(),
                    "Android/data/com.bitvault/.cache/" + subdir);
        }

        // Physically create the directory (and its parents) if it does not
        // exist.
        if (!result.exists()) {
            result.mkdirs();
        }

        File noMedia = new File(result, ".nomedia");
        try {
            noMedia.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
                   }
        return result;
    }
    // Clear the whole application cache.
    public void clearCache() {
        clearCache(null);
    }

    // Clear only the cache of a specific task.
    public void clearCache(String subdir) {
        File cacheDir = getCacheDir(subdir);
        if (cacheDir != null) {
            String[] files = cacheDir.list();
            if (files != null && files.length > 0) {
                for (String cachedFile : getCacheDir(subdir).list()) {
                    deleteDirectory(new File(getCacheDir(subdir), cachedFile));
                }
            }
        }
    }


    /**
     * delete cache directory
     * @param path
     * @return
     */
    static public int deleteDirectory(File path) {
        int nbDeleted = 0;
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    nbDeleted += deleteDirectory(file);
                } else {
                    if (file.delete()) {
                        nbDeleted++;
                    }

                }
            }
        } else if (path.exists()) {
            if (path.delete()) {
                nbDeleted++;
            }
        }
        return nbDeleted;
    }

    /**
     * get cache file directory path
     * @param url
     * @return
     */
    public File getFile(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        String filename = String.valueOf(url.hashCode());
        return new File(getCacheDir(), filename);

    }



}
