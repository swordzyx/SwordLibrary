package sword.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sword.logger.SwordLog;


/**
 * 迅龙 Android 平台工程中的选取图片的代码逻辑
 */
public class XlcwPhotoPicker {
  
  private final String tag = "XlcwPhotoPicker";
  
  public static View mainView(Context context) {
    LinearLayout view = new LinearLayout(context) {

    };
    return view;
  }
  
  /**
   * 选取图片
   */
  public static void ShowPhoneAlbum(Activity activity, boolean cropPhoto) {
    Intent intent = new Intent(activity, XlcwGalleryActivity.class);
    intent.putExtra("crop_photo", cropPhoto);
    activity.startActivity(intent);
  }


  /**
   * 图片压缩 
   */
  public void _compressPic(String json) {
    SwordLog.debug(tag, "_compressPic >> json:" + json);

    try {
      JSONObject jsonObject = new JSONObject(json);
      String pic_path = jsonObject.optString("pic_path", "");

      File file = new File(pic_path);
      String outputFileName = "compress_" + System.currentTimeMillis() / 1000 + "_" + file.getName();

      File outputFile = new File(file.getParent(), outputFileName);

      long max_file_size = jsonObject.optLong("max_file_size", 2 * 1024 * 1024);
      int max_width = jsonObject.optInt("max_width", 1920);
      int max_height = jsonObject.optInt("max_height", 1080);
      int max_quality = jsonObject.optInt("max_quality", 80);

      boolean isCompress = XlcwPhotoUtility.compressImage(pic_path, max_width, max_height,
          max_quality, max_file_size, outputFile.getAbsolutePath(), 3);

      if(isCompress){
        JSONObject callJsonObj = new JSONObject();
        callJsonObj.put("compress_pic_path", outputFile.getAbsolutePath());
        XlcwPhotoUtility.ToJson("OnCompressPic", 0, callJsonObj.toString());
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    XlcwPhotoUtility.ToJson("OnCompressPic", 1, "");
  }
  

  /**
   * 选择头像
   */
  public static void SelectAvatar(Activity activity, int flag) {
    Intent intent = new Intent(activity, XlcwAvatarActivity.class);
    intent.putExtra("FLAG", flag);
    activity.startActivity(intent);
  }

  /**
   * 二维扫码模块选取二维码
   */
  public static void scanPicture(Context context) {
    context.startActivity(new Intent(context, XlcwPhotoActivity.class));
  }

  /**
   * 保存图片到相册
   */
  private void saveImageToGallery(Activity activity, Bitmap bmp, String fileName) {
    if (bmp == null) {
      Toast.makeText(activity.getApplicationContext(), " 照片读取失败 ", Toast.LENGTH_SHORT).show();
      return;
    }
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      String cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
          .getAbsolutePath() + "/Camera/";

      File cameraDir = new File(cameraPath);
      if (!cameraDir.exists()) {
        cameraDir.mkdirs();
      }
      File file = new File(cameraPath, fileName);
      SwordLog.debug(tag, "cameraDirPath: " + file.getAbsolutePath());
      try {
        if (!file.exists()) {
          file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      // 最后通知图库更新
      activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
      MediaStore.Images.Media.insertImage(activity.getContentResolver(), bmp, fileName, null);
      activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }
  }

}
