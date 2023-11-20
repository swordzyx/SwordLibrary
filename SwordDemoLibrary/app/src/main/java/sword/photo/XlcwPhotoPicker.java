package sword.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.swordlibrary.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sword.ScreenSize;
import sword.logger.SwordLog;


/**
 * 迅龙 Android 平台工程中的选取图片的代码逻辑
 */
public class XlcwPhotoPicker {

    private final static String tag = "XlcwPhotoPicker";

    public static View mainView(Activity activity) {
        FrameLayout view = new FrameLayout(activity) {
            private int marginInternal = ScreenSize.dp(5);

            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);

                int halfWidth = getMeasuredWidth() >> 1;
                int childTop = marginInternal;

                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if ((i & 1) == 0) {
                        child.layout(0, childTop,
                                halfWidth - marginInternal,
                                childTop + child.getMeasuredHeight());

                    } else {
                        child.layout(
                                getMeasuredWidth() >> 1,
                                childTop,
                                getMeasuredWidth() - marginInternal,
                                childTop + child.getMeasuredHeight()
                        );
                        childTop += child.getMeasuredHeight() + marginInternal;
                    }
                }

            }
        };

        Button buttonShowPhoneAlbum = createButton(activity, "选取图片", v -> ShowPhoneAlbum(activity, true));
        view.addView(buttonShowPhoneAlbum);

        //todo: 填充参数
        JSONObject paramJson = new JSONObject();
        Button buttonCompressPic = createButton(activity, "图片压缩", v -> {
            _compressPic(paramJson.toString());
        });
        view.addView(buttonCompressPic);

        Button buttonSelectAvtar = createButton(activity, "选取头像", v -> {
            //todo：查看平台工程里面的 flag 传的是什么？
            selectAvatar(activity);
        });
        view.addView(buttonSelectAvtar);

        Button buttonSelectPictureScan = createButton(activity, "选取二维码扫描", v -> {
            scanPicture(activity);
        });
        view.addView(buttonSelectPictureScan);

        Button buttonSaveImageToGallery = createButton(activity, "保存图片到相册", v -> {
            //todo: 参考平台工程填充一个实际的 Bitmap
            Bitmap bitmap = Bitmap.createBitmap(ScreenSize.dp(20), ScreenSize.dp(20), Bitmap.Config.ARGB_8888);
            //todo：参考平台工程填充实际的文件名称
            String fileName = "";
            saveImageToGallery(activity, bitmap, fileName);
        });
        view.addView(buttonSaveImageToGallery);
        return view;
    }

    private static AppCompatButton createButton(Context context, String title, View.OnClickListener clickListener) {
        AppCompatButton button = new AppCompatButton(context) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int newWidth = MeasureSpec.getSize(widthMeasureSpec) >> 1;
                int newWidthSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY);
                int newHeightSpec = MeasureSpec.makeMeasureSpec(ScreenSize.dp(40), MeasureSpec.EXACTLY);
                super.onMeasure(newWidthSpec, newHeightSpec);
            }
        };
        button.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_right_angle_fill_blue, null));
        button.setText(title);
        button.setOnClickListener(clickListener);
        return button;
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
    public static void _compressPic(String json) {
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

            if (isCompress) {
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
     * 从相册选择头像
     */
    public static void selectAvatar(Activity activity) {
        Intent intent = new Intent(activity, XlcwAvatarActivity.class);
        intent.putExtra("FLAG", XlcwAvatarActivity.CODE_GALLERY_REQUEST);
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
    private static void saveImageToGallery(Activity activity, Bitmap bmp, String fileName) {
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
