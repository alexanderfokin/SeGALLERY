package ru.symdeveloper.SeGALLERY;

import android.util.Log;

import java.util.ArrayList;

public class SegalEntity {

    private int height;
    private int width;

    public SegalEntity(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String toString() {
        return "width: " + width + " | height: " + height;
    }

    public String getImageUrl() {
        return Constants.IMAGE_URL_BASE + "/" + String.valueOf(width) + "/" + String.valueOf(height);
    }

    public static class SegalEntitiesList extends ArrayList<SegalEntity> {

        public void dump(String logTag, String text) {
            Log.d(logTag, text);
            for (SegalEntity item : this) {
                Log.d(logTag, item.toString());
            }
        }
    }
}
