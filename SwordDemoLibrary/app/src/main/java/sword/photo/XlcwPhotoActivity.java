package sword.photo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickPhoto();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_REQUEST_PERMISSION);
        }

        setContentView(R.layout.activity_photo);
        photoImageView = findViewById(R.id.photo);

        findViewById(R.id.cancel).setOnClickListener(clickListener);
        findViewById(R.id.photo_import).setOnClickListener(clickListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_REQUEST_PERMISSION && Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhoto();
            } else {
                Toast.makeText(this, "storage permission is not authorized.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
