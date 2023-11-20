package sword.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
  public static final int SELECT_PICTURE = 1;
  public static final int CODE_RESULT_REQUEST = 2;
  public String m_imagePath = null;
  public int CompressSize = 40;
  public int CompressWidth = 800;
  public int CompressHeight = 500;
  public int CompressMiniSize = 40;
  public int CompressMiniWidth = 250;
  public int CompressMiniHeight = 150;

  private File uploadFile = null;
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

    //uploadFile = new File(getExternalCacheDir(), "upload_image.jpg");
    uploadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "upload_image.jpg");
    if (getIntent().hasExtra(EXTRA_KEY_SELECT_ACTION)) {
      selectPictureAction = getIntent().getStringExtra(EXTRA_KEY_SELECT_ACTION);
    } else {
      selectPictureAction = Intent.ACTION_OPEN_DOCUMENT;
    }
    
    //防止OppoR11s等机型出现重复选择相片的问题
    if (savedInstanceState == null) {
      //打开系统相册
      cropPhoto = getIntent().getBooleanExtra(EXTRA_KEY_CROP_IMAGE, true);
      doOpenAlbum();
    }
  }

  //打开相册
  private void doOpenAlbum() {
    try {
      Intent intent = null;
      if (TextUtils.isEmpty(selectPictureAction)) {
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
      } else {
        intent = new Intent(selectPictureAction);
      }
      if (!Intent.ACTION_PICK.equals(selectPictureAction)) {
        intent.addCategory(Intent.CATEGORY_OPENABLE);
      }
      intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
      startActivityForResult(intent, SELECT_PICTURE);
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
    if (requestCode == SELECT_PICTURE) {
      Uri uri = data.getData();
      SwordLog.debug(tag, "图片选取成功，action 为： " + selectPictureAction + "，选取的图片 Uri：" + uri);
      if (uri == null) {
        //没有在相册选中图片
        SwordLog.debug(tag, "Sdk层:没有选择相片");
        finish();
      } else {
        if (cropPhoto) {
          //调用系统裁剪功能
          SwordLog.debug(tag, "裁剪图片, 裁剪的图片 Uri：" + uri);
          cropUri = Uri.fromFile(uploadFile);
          XlcwPhotoUtility.cropImageUri(this, uri, cropUri, 1, 1, 400, 400, CODE_RESULT_REQUEST);
        } else {
          SwordLog.debug(tag, "不裁剪图片，直接返回，返回的图片 uri：" + uri);
          uploadFile = new File(getExternalCacheDir(), "upload_image.jpg");
          if (!uploadFile.exists()) {
            try {
              uploadFile.createNewFile();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          try (BufferedInputStream input = new BufferedInputStream(getContentResolver().openInputStream(uri));
               BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(uploadFile))) {
            JavaFileIO.copyFile(input, output);
          } catch (IOException e) {
            e.printStackTrace();
          }
          returnImgToGame(uri);
        }
      }
    } else if (requestCode == CODE_RESULT_REQUEST) {
      if (cropUri != null) {
        returnImgToGame(cropUri);
      }
    }
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

  /**
   * 图片返回给unity,只需要返回图片路径，通过c# www获取
   */
  private void returnImgToGame(Uri uri) {
    JSONObject obj = new JSONObject();
    try {
      String path = getImagePath(uri);
      SwordLog.debug(tag, "uri 转 path：" + path);
      obj.put("path", path);

      SwordLog.debug("object: " + obj);
      ToastUtilKt.snackBar(this, obj.toString(), true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (callback != null) {
      callback.onFinish(uri);
    }
    finish();
  }
}

