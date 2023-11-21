package sword.photo;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;


import sword.io.JavaFileIO;
import sword.logger.SwordLog;
import sword.ToastUtilKt;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 启动系统相册Activity
 */
public class XlcwGalleryActivity extends Activity {

  public static final String tag = "AlbumActivity";
  public static final int REQUEST_PICK_PHOTO = 1;
  public static final int REQUEST_CROP_PHOTO = 2;
  public String m_imagePath = null;
  public int CompressSize = 40;
  public int CompressWidth = 800;
  public int CompressHeight = 500;
  public int CompressMiniSize = 40;
  public int CompressMiniWidth = 250;
  public int CompressMiniHeight = 150;

  private File uploadFile = null;
  private File cropTempFile = null;
  private Uri cropUri = null;
  /**
   * 是否裁剪选择的图片，默认为 true，即裁剪
   */
  private boolean cropPhoto = true;
  private String selectPictureAction = "";
  public static final String EXTRA_KEY_SELECT_ACTION = "selectPictureAction";
  public static final String EXTRA_KEY_CROP_IMAGE = "crop_photo";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (m_imagePath != null) {
      return;
    }
    SwordLog.debug(tag, "Sdk层:打开系统相册");
    super.onCreate(savedInstanceState);

    if (getIntent().hasExtra(EXTRA_KEY_SELECT_ACTION)) {
      selectPictureAction = getIntent().getStringExtra(EXTRA_KEY_SELECT_ACTION);
    } else {
      selectPictureAction = Intent.ACTION_OPEN_DOCUMENT;
    }

    //防止OppoR11s等机型出现重复选择相片的问题
    if (savedInstanceState == null) {
      //打开系统相册
      cropPhoto = getIntent().getBooleanExtra(EXTRA_KEY_CROP_IMAGE, true);
      if (cropPhoto) {
        cropTempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "upload_image.jpg");
      }

      uploadFile = new File(getExternalCacheDir(), "upload_image.jpg");

      pickPhoto();
    }
  }

  //打开相册
  private void pickPhoto() {
    try {
      Intent intent;
      if (TextUtils.isEmpty(selectPictureAction)) {
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
      } else {
        intent = new Intent(selectPictureAction);
      }
      if (!Intent.ACTION_PICK.equals(selectPictureAction)) {
        intent.addCategory(Intent.CATEGORY_OPENABLE);
      }
      intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
      startActivityForResult(intent, REQUEST_PICK_PHOTO);
    } catch (Exception e) {
      SwordLog.debug(tag, "Sdk层:Sdk层:打开系统相册 错误" + e);
    }
  }

  /**
   * 选择照片后回调此方法
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == 0) {
      finish();
      return;
    }
    //选择图片后返回Uri
    if (requestCode == REQUEST_PICK_PHOTO) {
      Uri uri = data.getData();
      if (uri == null) {
        //没有在相册选中图片
        SwordLog.debug(tag, "Sdk层:没有选择相片");
        finish();
        return;
      }
      SwordLog.debug(tag, "图片选取成功，action 为： " + selectPictureAction + "，选取的图片 Uri：" + uri);

      if (cropPhoto) {
        if (cropTempFile.exists()) {
          cropTempFile.delete();
        }

        //调用系统裁剪功能
        SwordLog.debug(tag, "裁剪图片, 裁剪的图片 Uri：" + uri);
        cropTempUri = addCropTempFileToMediaStore(Environment.DIRECTORY_DCIM + File.separator, "upload_image.jpg");
        SwordLog.debug(tag, "将 dcim 下的文件添加到 MediaStore 中：" + cropTempUri);

        XlcwPhotoUtility.cropImageUri(this, uri, Uri.fromFile(cropTempFile), 1, 1, 400, 400, REQUEST_CROP_PHOTO);
      } else {
        SwordLog.debug(tag, "不裁剪图片，直接返回，返回的图片 uri：" + uri);
        photoPickFinish(data.getData());
      }
    } else if (requestCode == REQUEST_CROP_PHOTO) {
      //todo: 将 DCIM 下的内容拷贝到 uploadFile 中
      if (copyImageFromDcmiToCache() && uploadFile.exists()) {
        returnImgToGame(uploadFile.getAbsolutePath());
      } else {
        returnImgToGame(cropTempFile.getAbsolutePath());
      }
    }
  }

  private boolean copyImageFromDcmiToCache() {
    try {
      if (uploadFile.exists()) {
        uploadFile.delete();
      }
      uploadFile.createNewFile();

      if (cropTempUri == null) {
        cropTempUri = getDcimUri("upload_image.jpg", Environment.DIRECTORY_DCIM + File.separator);
        SwordLog.debug(tag, "重新获取 dcim uri：" + cropTempUri);
      }
      SwordLog.debug(tag, "cropTempUri: " + cropTempUri);

      if (cropTempUri == null) {
        return false;
      }

      try (BufferedInputStream input = new BufferedInputStream(getContentResolver().openInputStream(cropTempUri));
           BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(uploadFile))) {
        JavaFileIO.copyFile(input, output);
      }
      return true;

    } catch (IOException exception) {
      exception.printStackTrace();
      return false;
    }
  }

  private Uri getDcimUri(String fileName, String dcimPath) {
    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projections = new String[]{MediaStore.Images.Media._ID};
    String selection = MediaStore.Images.Media.DISPLAY_NAME + "=? AND " + MediaStore.Images.Media.RELATIVE_PATH + "=?";
    String[] selectionsArgs = new String[]{"upload_image.jpg", Environment.DIRECTORY_DCIM + File.separator};

    Cursor cursor = getContentResolver().query(contentUri, projections, selection, selectionsArgs, null);
    try {
      if (cursor != null && cursor.moveToFirst()) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
  }

  Uri cropTempUri = null;

  private void photoPickFinish(Uri uri) {
    if (uri == null) {
      return;
    }

    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    try {
      if (uploadFile.exists()) {
        uploadFile.delete();
      }
      uploadFile.createNewFile();

      input = new BufferedInputStream(getContentResolver().openInputStream(uri));
      output = new BufferedOutputStream(new FileOutputStream(uploadFile));
      JavaFileIO.copyFile(input, output);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (input != null) {
          input.close();
        }

        if (output != null) {
          output.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    returnImgToGame(uploadFile.getAbsolutePath());
  }

  private Uri addCropTempFileToMediaStore(String relativePath, String fileName) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath);
    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    return uri;
  }

  /**
   * 图片返回给unity,只需要返回图片路径，通过c# www获取
   */
  private void returnImgToGame(String filePath) {
    JSONObject obj = new JSONObject();
    try {
      SwordLog.debug(tag, "上传图片路径：" + filePath);
      obj.put("path", filePath);

      SwordLog.debug("object: " + obj);
      ToastUtilKt.snackBar(this, obj.toString(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (callback != null) {
      callback.onFinish(Uri.fromFile(new File(filePath)));
    }
    finish();
  }

  private boolean hasSdcard() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }


  private void readCropSizeConfig() {
    try {
      String data = getJson(this.getApplicationContext(), "AlbumConfiguration.txt");
      JSONObject jsonObject = new JSONObject(data);
      CompressSize = jsonObject.getInt("CompressSize");
      CompressWidth = jsonObject.getInt("CompressWidth");
      CompressHeight = jsonObject.getInt("CompressHeight");
      CompressMiniSize = jsonObject.getInt("CompressMiniSize");
      CompressMiniWidth = jsonObject.getInt("CompressMiniWidth");
      CompressMiniHeight = jsonObject.getInt("CompressMiniHeight");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 读取配置文件
   */
  public String getJson(Context mContext, String fileName) {
    StringBuilder sb = new StringBuilder();
    AssetManager am = mContext.getAssets();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));
      String next = "";
      while (null != (next = br.readLine())) {
        sb.append(next);
      }
    } catch (IOException e) {
      e.printStackTrace();
      sb.delete(0, sb.length());
    }
    return sb.toString().trim();
  }


  interface Callback {
    void onFinish(Uri resultUri);
  }

  private static Callback callback;

  public static void setCallback(Callback callback) {
    XlcwGalleryActivity.callback = callback;
  }

}

