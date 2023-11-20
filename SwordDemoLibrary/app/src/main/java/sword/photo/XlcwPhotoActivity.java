package sword.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swordlibrary.R;

import java.io.IOException;

import sword.logger.SwordLog;


public class XlcwPhotoActivity extends AppCompatActivity {
    private final String tag = "XlcwPhotoActivity";
    private static final int REQUEST_CODE_SELECT_PICTURE = 1;
    private static final int REQUEST_CODE_REQUEST_PERMISSION = 7701;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
            selectPhotoUri = data.getData();
            showPhoto(selectPhotoUri);
        }
    }

    private void pickPhoto() {
        Intent pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PICTURE);
    }

    private void scanPhoto(Uri photoUri) {
        try {
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
            SwordLog.debug(tag, "图片解析成功, 高：" + bitmap.getHeight() + ", 宽：" + bitmap.getWidth());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
