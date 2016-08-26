package com.lynlab.permissionmanger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * @author LYn
 * @since 2016-08-26
 */
public class PermissionManager {

    private static PermissionListener permissionListener;


    /**
     * Request permissions.
     */
    public static void requestPermissions(Context context, PermissionListener listener, String... permissions) {
        if (listener == null)
            throw new IllegalStateException("Permission listener should not be null.");

        permissionListener = listener;

        ArrayList<String> requiredPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                permissionListener.onPermit(permission, true);
            } else {
                requiredPermissions.add(permission);
            }
        }

        if (requiredPermissions.size() == 0) {
            return;
        }

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putStringArrayListExtra("permissions", requiredPermissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    static void onPermissionGranted(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            permissionListener.onPermit(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
        }
    }
}
