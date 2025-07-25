// app/src/main/java/com/proapp/obdcodes/util/FileUtils.java

package com.eyadalalimi.car.obd2.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtils {

    /**
     * يحول Uri إلى مسار ملف حقيقي على الذاكرة
     */
    public static String getPath(Context ctx, Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = null;
        try {
            cursor = ctx.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(idx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
