package com.example.sqlitelevelnext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText name,id,phone,email;
    private Button submit,view;
    private ImageView imageView;
    public static final int req_camera=2;
    public static final int req_code=1;
    public static final int req_gallery=3;
    Bitmap bitmap;
    byte[] Image;
    UserData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.CollapsingLayout);
        name=findViewById(R.id.Name);
        id=findViewById(R.id.ID);
        phone=findViewById(R.id.Phone);
        email=findViewById(R.id.Email);
        submit=findViewById(R.id.submit);
        view=findViewById(R.id.view);
        imageView=findViewById(R.id.image);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDataBase();
            }
        });


        if(Build.VERSION.SDK_INT>=21) {
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingLayoutNew);
        }
        else
        {
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingLayoutOld);
        }

        permissionCheck();


    }

    public void chooseImage()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");
        builder.setIcon(R.drawable.ic_crop_original);
        final String items[]={"Camera","Gallery","Cancel"};
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("Camera"))
                {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                    {
                        permissionCheck();
                    }
                    else
                    {
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,req_camera);
                    }

                }
                else if(items[which].equals("Gallery"))
                {
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        permissionCheck();

                    }
                    else
                    {
                        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.putExtra("crop","true");
                        intent.putExtra("scale",true);
                        intent.putExtra("outputX",256);
                        intent.putExtra("outputY",256);
                        intent.putExtra("aspectX",1);
                        intent.putExtra("aspectY",1);
                        intent.putExtra("return-data",true);
                        startActivityForResult(intent,req_gallery);

                    }

                }
                else
                {
                    dialog.cancel();
                }
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void permissionCheck() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, req_code);
            }
        }
    }

    public void submitData()
    {

        String uname,uphone,uid,uemail;
        uname=name.getText().toString();
        uphone=phone.getText().toString().trim();
        uid=id.getText().toString();
        uemail=email.getText().toString();

        if(uname.equals("")||uphone.equals("")||uid.equals("")||uemail.equals(""))
            Toast.makeText(this, "Fields shouldn't be empty", Toast.LENGTH_SHORT).show();
        else
        {
            if(imageView.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_add).getConstantState()) {
                Toast.makeText(this, "Please add your profile", Toast.LENGTH_SHORT).show();
                chooseImage();
            }
            else
            {
                DatabaseHelper db=new DatabaseHelper(this);
                if(db.userFound(uemail))
                {
                    Toast.makeText(this, "User already registered with this email", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    id.setText("");
                    imageView.setImageResource(R.drawable.ic_add);
                }
                else
                {
                    data=new UserData();
                    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    Image=outputStream.toByteArray();

                    data.setImageBytes(Image);
                    data.setId(Integer.parseInt(uid));
                    data.setPhone(uphone);
                    data.setEmail(uemail);
                    data.setName(uname);

                    if(db.addData(data)) {
                        Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        phone.setText("");
                        email.setText("");
                        id.setText("");
                        imageView.setImageResource(R.drawable.ic_add);
                    }
                    else
                        Toast.makeText(this, "Error occurred! Can't insert data", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    public void ViewDataBase()
    {
        Cursor cursor=new DatabaseHelper(this).getData();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case req_camera:
            case req_gallery:
                if(resultCode==RESULT_OK)
                {
                    Bundle bundle=data.getExtras();

                    if(bundle!=null)
                    {
                        bitmap=bundle.getParcelable("data");
                        imageView.setImageBitmap(bitmap);
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==req_code)
        {
            if(grantResults.length>0)
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED || grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
