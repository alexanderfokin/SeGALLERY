package ru.symdeveloper.SeGALLERY;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Random;

public class SegalEntitiesStorage {
    private static final String LOG_TAG = "SegalEntitiesStorage";

    private static final String CACHE_ENTRY = "SegalEntities";

    private SegalEntity.SegalEntitiesList mEntitiesList;
    private SimpleDiskCache mDiskCache;

    public SegalEntitiesStorage(SimpleDiskCache diskCache) {
        mDiskCache = diskCache;
        mEntitiesList = new SegalEntity.SegalEntitiesList();
        loadFromCache();
    }

    public SegalEntity.SegalEntitiesList getEntitiesList() { return mEntitiesList; }

    private void loadFromCache() {
        try {
            Log.d(LOG_TAG, "load images information from cache");
            SimpleDiskCache.StringEntry entry = mDiskCache.getString(CACHE_ENTRY);
            if (entry != null) {
                String value = entry.getString();
                Log.d(LOG_TAG, "loadFromCache | value: " + value);
                if (!TextUtils.isEmpty(value)) {
                    mEntitiesList = new Gson().fromJson(value, SegalEntity.SegalEntitiesList.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToCache() {
        try {
            Gson gson = new Gson();
            String value = gson.toJson(mEntitiesList);
            Log.d(LOG_TAG, "saveToCache | value: " + value);
            mDiskCache.put(CACHE_ENTRY, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dump(String logTag, String text) {
        Log.d(logTag, text);
        for (SegalEntity item : mEntitiesList) {
            Log.d(logTag, item.toString());
        }
    }

    public void reset(int screenWidth, int screenHeight) {
        Log.d(LOG_TAG, "screenWidth: " + screenWidth + " | screenHeight: " + screenHeight);
        mEntitiesList.clear();

        int maxWidth = screenWidth * 2 / 3;
        int maxHeight = screenHeight * 2 / 3;

        Random random = new Random();

        int left_image_width = random.nextInt(maxWidth - Constants.MIN_IMAGE_DIMENSION) + Constants.MIN_IMAGE_DIMENSION;
        int right_image_width = screenWidth - left_image_width;

        Log.d(LOG_TAG, "left_image_width: " + left_image_width + " | right_image_width: " + right_image_width);

        for (int i = 0; i < Constants.MAX_IMAGES_ON_PAGE / 2; i++) {

            int left_image_height = random.nextInt(maxHeight - Constants.MIN_IMAGE_DIMENSION) + Constants.MIN_IMAGE_DIMENSION;
            SegalEntity left_item = new SegalEntity(left_image_width, left_image_height);
            mEntitiesList.add(left_item);
            Log.d(LOG_TAG, "left item: " + left_item.toString());

            int right_image_height = random.nextInt(maxHeight - Constants.MIN_IMAGE_DIMENSION) + Constants.MIN_IMAGE_DIMENSION;
            SegalEntity right_item = new SegalEntity(right_image_width, right_image_height);
            mEntitiesList.add(right_item);
            Log.d(LOG_TAG, "right item: " + right_item.toString());
        }
        saveToCache();
    }
}
