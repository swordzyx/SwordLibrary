package com.black.app.scanknow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

public class PermissionControl {
    protected static final String TAG = "PermissionControl";

    public static final int RC_ESAU_PERM = 7100;

    public static final String[] PERM_ESAU = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,	//android.permission-group.STORAGE
            android.Manifest.permission.CAMERA,
    };

    private static SparseArray<String[]> permissionsArray;
    private static SparseArray<Callback> callbackArray;

    public static void requestPermissions(Activity act, int requestCode, String[] requestPermissions, Callback callback){
        if(permissionsArray == null){
            permissionsArray = new SparseArray<String[]>();
        }
        if(callbackArray == null){
            callbackArray = new SparseArray<Callback>();
        }
        if(act == null){
            callback.onError();
            return;
        }

        if(requestPermissions == null){
            callback.onError();
            return;
        }

        callbackArray.put(requestCode, callback);
        permissionsArray.put(requestCode, requestPermissions);
        requestTask(act, requestCode, requestPermissions, callback);
    }

    private static void requestTask(Activity act, int requestCode, String[] requestPermissions, Callback callback) {

        List<String> permsList = new ArrayList<String>();
        if(requestPermissions != null){
            for (String perm : requestPermissions) {
                permsList.add(perm);
            }
        }
        if (hasPermissions(act, requestPermissions)) {
            onPermissionsGranted(act, requestCode, permsList);
            return;
        }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                act.requestPermissions(requestPermissions, requestCode);
            }else{
                onPermissionsGranted(act, requestCode, permsList);
            }
    }

    private static void onCallbackSuccess(int requestCode) {
        if(callbackArray != null){
            Callback sdkCallback = callbackArray.get(requestCode);
            if(sdkCallback != null){
                callbackArray.remove(requestCode);
                sdkCallback.onSuccess();
            }
        }
    }
    private static void onCallbackError(Activity act, int requestCode) {
        Callback sdkCallback = null;
        if(callbackArray != null){
            sdkCallback = callbackArray.get(requestCode);
        }
        if(sdkCallback != null){
            callbackArray.remove(requestCode);
            sdkCallback.onError();
        }
    }

    /**
     *
     * 第一次打开App时	false
     * 上次弹出权限点击了禁止（但没有勾选“下次不在询问”）	true
     * 上次选择禁止并勾选：下次不在询问	false
     *
     * @param act
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity act, @NonNull List<String> perms) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(perms != null){
                for (String deniedPermission : perms) {
                    if (act.shouldShowRequestPermissionRationale(deniedPermission)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static boolean hasPermissions(@NonNull Context context,
                                         @Size(min = 1) @NonNull String... perms) {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // DANGER ZONE!!! Changing this will break the library.
            return true;
        }

        // Null context may be passed if we have detected Low API (less than M) so getting
        // to this point with a null context should not be possible.
        if (context == null) {
            throw new IllegalArgumentException("Can't check permissions for null context");
        }

        if(perms != null){
            for (String perm : perms) {
                if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public static final int APP_SETTINGS_RC = 7534;
    public static void onActivityResult(Activity act,int requestCode, int resultCode, Intent data) {
        if (requestCode == APP_SETTINGS_RC) {
            // TODO Do something after user returned from app settings screen, like showing a Toast.
        }
    }

    public static void onRequestPermissionsResult(Activity act, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        if (!granted.isEmpty()) {
            onPermissionsGranted(act, requestCode, granted);
        }

        // Report denied permissions, if any.
        if (!denied.isEmpty()) {
            onPermissionsDenied(act, requestCode, denied);
        }
    }

    public static void onPermissionsGranted(Activity act, int requestCode, @NonNull List<String> perms) {
        //TODO 回调成功
        if(permissionsArray != null){
            String[] allPerms = permissionsArray.get(requestCode);
            if(hasPermissions(act, allPerms)){
                onCallbackSuccess(requestCode);
            }
        }
    }

    public static void onPermissionsDenied(Activity act, int requestCode, @NonNull List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.

        //TODO 回调失败
        if(permissionsArray != null){
            String[] allPerms = permissionsArray.get(requestCode);
            if(!hasPermissions(act, allPerms)){
                onCallbackError(act, requestCode);
            }
        }

    }

    public static interface Callback {
        void onSuccess();
        void onError();
    }
}
