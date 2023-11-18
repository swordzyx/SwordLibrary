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


  /**
   * @param context 上下文对象
   * @param uri     当前相册照片的Uri
   * @return 解析后的Uri对应的String
   */
  @SuppressLint("NewApi")
  public static String getPath(final Context context, final Uri uri) {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    String pathHead = "file:///";
    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        if ("primary".equalsIgnoreCase(type)) {
          return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
        }
      }
      // DownloadsProvider
      else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

        final String id = DocumentsContract.getDocumentId(uri);

        final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        return pathHead + getDataColumn(context, contentUri, null, null);
      }
      // MediaProvider
      else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};

        return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
      return pathHead + getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return pathHead + uri.getPath();
    }
    return null;
  }
  
  private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {column};
    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int column_index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(column_index);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
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
