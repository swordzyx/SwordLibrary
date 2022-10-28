package com.example.swordlibrary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;


import com.sword.LogUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 启动系统相册Activity
 */
public class AlbumActivity extends Activity {

  public static final String LOG_TAG = "AlbumActivity";
  public static final int ALBUM_CODE = 1;
  public static final int CODE_RESULT_REQUEST = 2;
  public String m_imagePath = null;
  public int CompressSize = 40;
  public int CompressWidth = 800;
  public int CompressHeight = 500;
  public int CompressMiniSize = 40;
  public int CompressMiniWidth = 250;
  public int CompressMiniHeight = 150;

  private File fileCrop = null;
  private Uri cropUri = null;
  /**
   * 是否裁剪选择的图片，默认为 true，即裁剪
   */
  private boolean cropPhoto = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (m_imagePath != null) {
      return;
    }
    LogUtil.debug(LOG_TAG, "Sdk层:打开系统相册");
    super.onCreate(savedInstanceState);

    fileCrop = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "crop_photo.jpg");

    //防止OppoR11s等机型出现重复选择相片的问题
    if (savedInstanceState == null) {
      //打开系统相册
      cropPhoto = getIntent().getBooleanExtra("crop_photo", true);
      doOpenAlbum();
    } else {
      //系统重建Activity,不执行逻辑，但不能finish,重启后才会执行onActivityResult
      LogUtil.debug(LOG_TAG, "Sdk层: bundle不为空，不打开相册");
    }
  }

  //打开相册
  private void doOpenAlbum() {
    String data = getJson(this.getApplicationContext(), "AlbumConfiguration.txt");
    try {
      JSONObject jsonObject = new JSONObject(data);
      CompressSize = jsonObject.getInt("CompressSize");
      CompressWidth = jsonObject.getInt("CompressWidth");
      CompressHeight = jsonObject.getInt("CompressHeight");
      CompressMiniSize = jsonObject.getInt("CompressMiniSize");
      CompressMiniWidth = jsonObject.getInt("CompressMiniWidth");
      CompressMiniHeight = jsonObject.getInt("CompressMiniHeight");

      Intent intent = new Intent("android.intent.action.PICK", null);
      intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
      startActivityForResult(intent, ALBUM_CODE);
    } catch (Exception e) {
      LogUtil.debug(LOG_TAG, "Sdk层:Sdk层:打开系统相册 错误" + e.toString());
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    LogUtil.debug(LOG_TAG, "Sdk层: onSaveInstanceState");
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    LogUtil.debug(LOG_TAG, "Sdk层: onRestoreInstanceState");
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  protected void onDestroy() {
    LogUtil.debug(LOG_TAG, "Sdk层: onDestroy");
    super.onDestroy();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    LogUtil.debug(LOG_TAG, "--- Sdk层: onConfigurationChanged ---");
    super.onConfigurationChanged(newConfig);
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
    if (requestCode == ALBUM_CODE) {
      Uri uri = data.getData();
      if (uri == null) {
        //没有在相册选中图片
        LogUtil.debug(LOG_TAG, "Sdk层:没有选择相片");
        finish();
      } else if (hasSdcard()) {
        if (cropPhoto) {
          //调用系统裁剪功能
          cropUri = Uri.fromFile(fileCrop);
          cropImageUri(this, uri, cropUri, 1, 1, 400, 400, CODE_RESULT_REQUEST);
        } else {
          returnImgToGame(uri);
        }
      } else {
        LogUtil.debug(LOG_TAG, "SDK层：设备没有 SD 卡");
        finish();
      }
    } else if (requestCode == CODE_RESULT_REQUEST) {
      if (cropUri != null) {
        returnImgToGame(cropUri);
      }
    }
  }

  private void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
    Intent intent = new Intent("com.android.camera.action.CROP");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
    intent.setDataAndType(orgUri, "image/*");
    intent.putExtra("crop", "false");
    intent.putExtra("aspectX", aspectX);
    intent.putExtra("aspectY", aspectY);
    intent.putExtra("outputX", width);
    intent.putExtra("outputY", height);
    intent.putExtra("scale", true);
    //将剪切的图片保存到目标Uri中
    intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
    intent.putExtra("return-data", false);
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", true);
    activity.startActivityForResult(intent, requestCode);
  }

  private boolean hasSdcard() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
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

  /**
   * 图片返回给unity,只需要返回图片路径，通过c# www获取
   */
  private void returnImgToGame(Uri uri) {
    JSONObject obj = new JSONObject();
    try {
      obj.put("path", getImagePath(uri));

      LogUtil.debug("object: " + obj);
    } catch (Exception e) {
      e.printStackTrace();
    }
    finish();
  }
}

