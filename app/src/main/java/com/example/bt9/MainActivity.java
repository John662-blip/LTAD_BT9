package com.example.bt9;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bt9.Utils.RealPathUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {

    Button btnChose,btnUpload,btnBack;
    ImageView imageViewChose;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = MainActivity.class.getName();
    public static String[] store_permissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };
    private ActivityResultLauncher<Intent>  mActivityResultLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultContract<ActivityResult>(){
                public void onActivityResult(ActivityResult result){
                    if (result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data==null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imageViewChose.setImageBitmap(bitmap);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload);
        Anhxa();
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please wait upload.....");
        btnChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null){
                    UploadImage1();
                }
            }
        });
    }
    public void UploadImage1(){
        mProgressDialog.show();
        RequestBody requestID = RequestBody.create(MediaType.parse("multipart/form-data"),"5");
        String ImagePart = RealPathUtil.getRealPath(this,mUri);
        File file = new File(ImagePart);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part partbody = MultipartBody.Part.createFormData("images",file.getName(),requestFile);

    }
    private void Anhxa(){
        btnChose = findViewById(R.id.button2);
        btnUpload = findViewById(R.id.button3);
        btnBack = findViewById(R.id.button1);
        imageViewChose = findViewById(R.id.circleImageView2);
    }

    public static String[] permissions(){
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            p = storage_permissions_33;
        }else{
            p = store_permissions;
        }
        return p;
    }
    private void CheckPermission(){
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else{
            requestPermissions(permissions(),MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLaucher.launch(Intent.createChooser(intent,"Select Picture"));
    }
}