package ru.symdeveloper.SeGALLERY;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.IOException;

public class App extends Application {

    private static final String LOG_TAG = "App";
    private static App instance;

	private DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.resetViewBeforeLoading(true)
		.bitmapConfig(Config.RGB_565)
		.build();

	public static App instance() {
		return instance;
	}

    private SimpleDiskCache mDiskCache;

    public SimpleDiskCache getDiskCache() { return mDiskCache; }

    @Override
	public void onCreate() {
		super.onCreate();
        instance = this;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                //.writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);

        try {
            mDiskCache = SimpleDiskCache.open(SimpleDiskCache.getDiskCacheDir(this, Constants.CACHE_DIR), 1, Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if(info != null) {
            for(int i = 0; i < info.length; i++)
                if(info[i].getState() == NetworkInfo.State.CONNECTED)
                    return true;
        }
        return false;
    }
}