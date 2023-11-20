package sword.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import sword.logger.SwordLog;

public class XlcwPhotoUtility {
  public static void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
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


  public static boolean compressImage(String imagePath, int maxWidth, int maxHeight, int maxQuality,
                                      long maxFileSize, String outputPath, int times) {

    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
    int originalWidth = bitmap.getWidth();
    int originalHeight = bitmap.getHeight();

    int scaledWidth, scaledHeight;
    if (originalWidth > originalHeight) {
      scaledWidth = Math.min(maxWidth, originalWidth);
      scaledHeight = (int) (originalHeight * (float) Math.min(maxWidth, originalWidth) / originalWidth);
    } else {
      scaledWidth = (int) (originalWidth * (float) Math.min(maxHeight, originalHeight) / originalHeight);
      scaledHeight = Math.min(maxHeight, originalHeight);
    }

    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

    OutputStream outputStream = null;
    try {
      File outputFile = new File(outputPath);
      if (outputFile.exists()) {
        outputFile.delete();
      }

      outputStream = new FileOutputStream(outputPath);
      if(maxQuality > 100){
        maxQuality = 100;
      }
      scaledBitmap.compress(Bitmap.CompressFormat.JPEG, maxQuality, outputStream);

      if (outputFile.length() > maxFileSize
          && maxQuality > 50
          && times > 0) {
        return compressImage(imagePath, maxWidth, maxHeight, maxQuality - 10,
            maxFileSize, outputPath, times - 1);
      } else {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  public static void ToJson(String functionName, int errorCode, String data) {
    JSONObject jsonData = new JSONObject();
    try {
      jsonData.put("FunctionName", functionName);
      jsonData.put("ErrorCode", errorCode);
      jsonData.put("Data", data);
      SwordLog.debug("xlcw", "结果回调：" + jsonData);
    } catch (Exception e) {
      e.printStackTrace();
      SwordLog.debug("xlcw", "结果回调：Json 解析错误");
    }
  }

}
