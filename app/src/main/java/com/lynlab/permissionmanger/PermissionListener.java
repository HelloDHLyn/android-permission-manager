package com.lynlab.permissionmanger;

/**
 * @author LYn
 * @since 2016-08-26
 */
public interface PermissionListener {

    /**
     * Method called when permission granted or denied.
     *
     * @param permission Name of permission.
     * @param granted    True if permission granted, false if not.
     */
    void onPermit(String permission, boolean granted);
}
