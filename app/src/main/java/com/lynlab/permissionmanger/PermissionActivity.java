package com.lynlab.permissionmanger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Dummy activity to grant permissions.
 *
 * @author LYn
 * @since 2016-08-26
 */
@TargetApi(Build.VERSION_CODES.M)
public class PermissionActivity extends Activity {

    private static final int REQ_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> permissions = getIntent().getStringArrayListExtra("permissions");
        String[] permissionArray = new String[permissions.size()];
        permissionArray = permissions.toArray(permissionArray);

        requestPermissions(permissionArray, REQ_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionManager.onPermissionGranted(permissions, grantResults);
        finish();
    }
}
