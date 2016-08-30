package com.lynlab.permissionmanger;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

/**
 * @author LYn
 * @since 2016-08-26
 */
public class PermissionManager {

    private static PermissionListener permissionListener;
    private static Context context;

    /**
     * Request permissions.
     */
    public static void requestPermissions(Context context, PermissionListener listener, String... permissions) {
        if (listener == null)
            throw new IllegalStateException("Permission listener should not be null.");

        PermissionManager.permissionListener = listener;
        PermissionManager.context = context;

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

    static void onPermissionGranted(final String[] permissions, final int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                permissionListener.onPermit(permissions[i], true);
            } else {
                getSettingDialog(permissions[i]).show();
            }
        }
    }

    /**
     * Get AlertDialog to explain user why such permission is required, and redirect to setting.
     *
     * @param permission Permission to show and redirect.
     */
    private static AlertDialog getSettingDialog(final String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.warning_message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.parse("package:" + context.getPackageName()));
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permissionListener.onPermit(permission, false);
                    }
                });

        return builder.create();
    }
}
