package sword.photo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import sword.logger.SwordLog;

public class XlcwAvatarActivity extends Activity {

    public static final String tag = "AvatarActivity";
    public static String MANIFEST_AUTHOR_NAME = "";

    static final int CODE_CAMERA_REQUEST = 0; // 打开相机
    static final int CODE_GALLERY_REQUEST = 1; // 打开相册
    private static final int CODE_RESULT_REQUEST = 2; //剪裁图片

    //拍照保存的照片路径
    private final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),  "photo.jpg");
    //剪裁图片保存的路径
    private final File fileCrop = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "crop_photo.jpg");
    private Uri cropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MANIFEST_AUTHOR_NAME = getAppProcessName(this);
        SwordLog.debug(tag, "APP PackageName = " + MANIFEST_AUTHOR_NAME);
        //防止OppoR11s等机型出现重复选择相片的问题
        if (savedInstanceState == null) {
            //检查权限 -- 权限平台统一管理
            //判断是打开相机还是相册，默认是打开相机
            Intent fromIntent = getIntent();
            int flag = fromIntent.getIntExtra("FLAG", 0);
            SwordLog.debug(tag, "getIntExtra flag==" + flag);

            switch (flag) {
                case CODE_CAMERA_REQUEST:
                    takePhoto();
                    break;
                case CODE_GALLERY_REQUEST:
                    getAlbumPhoto();
                    break;
                default:
                    break;
            }
        } else {
            SwordLog.error(tag, "Sdk层: bundle不为空，不重新走AvatarActivity");
        }
    }

    /**
     * 调用手机拍照
     */
    public void takePhoto() {
        if (hasSdcard()) {
            Uri imageUri = Uri.fromFile(file);
            //通过FileProvider创建一个content类型的Uri,第二个参数和manifest中保持一致
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(this, MANIFEST_AUTHOR_NAME, file);
            }
            takePicture(this, imageUri, CODE_CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
            SwordLog.error(tag, "设备没有SD卡");
            XlcwPhotoUtility.ToJson("OnSelectAvatar", 1, "");
            finish();
        }
    }

    
    /**
     * @param activity    当前activity
     * @param imageUri    拍照后照片存储路径
     * @param requestCode 调用系统相机请求码
     */
    public static void takePicture(Activity activity, Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    /**
     * 调用系统相册
     */
    public void getAlbumPhoto() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, CODE_GALLERY_REQUEST);
        } catch (Exception e) {
            SwordLog.error(tag, "getAlbumPhoto failed:" + e.getMessage());
            XlcwPhotoUtility.ToJson("OnSelectAvatar", 1, "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 剪裁的大小
        int output_X = 158, output_Y = 158;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCrop);
                    Uri sourceUrl = getImageContentUri(this, file);
                    XlcwPhotoUtility.cropImageUri(this, sourceUrl, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);

                    SwordLog.debug(tag, "CODE_CAMERA_REQUEST");

                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    SwordLog.debug(tag, "CODE_GALLERY_REQUEST");
                    if (hasSdcard()) {

                        //2018年7月28日19:11:24 更换选照片后裁剪方法测试
                        cropImageUri = Uri.fromFile(fileCrop);
                        SwordLog.debug(tag, "选择图片：" + data.getData());
                        XlcwPhotoUtility.cropImageUri(this, data.getData(), cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);

                    } else {
                        Toast.makeText(this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                        XlcwPhotoUtility.ToJson("OnSelectAvatar", 1, "");
                        finish();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    SwordLog.debug(tag, "CODE_RESULT_REQUEST");
                    if (cropImageUri != null) {
                        returnImgToGame(cropImageUri);
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    /**
     * 图片返回给unity,只需要返回图片路径，通过c# www获取
     */
    private void returnImgToGame(Uri uri) {
        JSONObject obj = new JSONObject();
        String imagePath = getImagePath(uri);
        SwordLog.debug(tag, "returnImgToGame imagePath=" + imagePath);
        try {
            obj.put("path", imagePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        XlcwPhotoUtility.ToJson("OnSelectAvatar", 0, obj.toString());
        finish();
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 用此方法将file文件转化成content://路径，否则裁剪程序可能找不到
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取图片路径
     */
    private String getImagePath(Uri uri) {
        if (null == uri) {
            return null;
        }
        String path = null;
        final String scheme = uri.getScheme();
        if (null == scheme) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            int nPhotoColumn;
            if (null != cursor) {
                nPhotoColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(nPhotoColumn);
                cursor.close();
            }
        }
        return path;
    }

    public String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }
}
