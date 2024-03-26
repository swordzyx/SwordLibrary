package sword.qrcode.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swordlibrary.R;
import com.google.zxing.Result;

import java.io.IOException;

import sword.qrcode.api.ScanResultCallback;
import sword.qrcode.decode.CodeDecoder;

public class PhotoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_PICTURE = 1;

    public static ScanResultCallback scanCallback = null;

    private ImageView photoImageView;
    private Uri selectPhotoUri;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cancel) {
                pickPhoto();
                return;
            }
            if (v.getId() == R.id.photo_import && selectPhotoUri != null) {
                scanPhoto(selectPhotoUri);
            }
        }
    };

    public static void setCallback(ScanResultCallback callback) {
        scanCallback = callback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickPhoto();

        setContentView(R.layout.activity_photo);
        photoImageView = findViewById(R.id.photo);

        findViewById(R.id.cancel).setOnClickListener(clickListener);
        findViewById(R.id.photo_import).setOnClickListener(clickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            if (scanCallback != null) {
                scanCallback.scanFinish("");
                scanCallback = null;
            }
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
            selectPhotoUri = data.getData();
            showPhoto(selectPhotoUri);
        }
    }

    private void pickPhoto() {
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PICTURE);
    }

    private void scanPhoto(Uri photoUri) {
        try {
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Result result = CodeDecoder.decodeQRCode(bitmap);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (scanCallback != null) {
                                scanCallback.scanFinish(result == null ? "" : result.toString());
                                scanCallback = null;
                            }
                        }
                    });
                }
            }).start();
        } catch (IOException e) {
            if (scanCallback != null) {
                scanCallback.scanFinish("");
                scanCallback = null;
            }
            e.printStackTrace();
        } finally {
            finish();
        }
    }

    private void showPhoto(Uri uri) {
        if (uri == null || photoImageView == null) {
            finish();
            return;
        }
        photoImageView.setImageURI(uri);
    }
}
