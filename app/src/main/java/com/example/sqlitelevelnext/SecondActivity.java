package com.example.sqlitelevelnext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SecondActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView listView;
    private static final int req_code=1;
    ArrayList<UserData> dataArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=findViewById(R.id.ListView);

        permissionCheck();

        inflateLayout();
    }

    private void inflateLayout() {

        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        Cursor cursor=databaseHelper.getData();

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        dataArrayList.clear();
        while(cursor.moveToNext())
        {
            UserData data=new UserData();
            data.setName(cursor.getString(1));
            data.setEmail(cursor.getString(3));
            data.setPhone(cursor.getString(2));
            data.setId(cursor.getInt(0));
            data.setImageBytes(cursor.getBlob(4));

            dataArrayList.add(data);

        }
        Collections.sort(dataArrayList, new Comparator<UserData>() {
            @Override
            public int compare(UserData o1, UserData o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        Myadapter myadapter=new Myadapter(this,R.layout.custom_layout,dataArrayList,SecondActivity.this);
        listView.setAdapter(myadapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.update:
                final Dialog dialog=new Dialog(this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.email_choose);
                final EditText choose_email;
                final Button choose_button=dialog.findViewById(R.id.choose_button);
                Button choose_cancel=dialog.findViewById(R.id.cancel_button);
                choose_email=dialog.findViewById(R.id.choose_email);
                choose_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(choose_email.getText().toString().equals(""))
                            Toast.makeText(SecondActivity.this, "please type your email address", Toast.LENGTH_SHORT).show();
                        else
                        {
                            updateData(choose_email.getText().toString());
                            dialog.cancel();
                        }
                    }
                });
                choose_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.delete:
                deleteData();
                break;
            case R.id.deleteAll:
                DatabaseHelper dbhelper=new DatabaseHelper(this);
                dbhelper.deleteAll();
                Intent intent=new Intent(SecondActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(SecondActivity.this, "Database cleared", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
        }

    private void deleteData() {
        final DatabaseHelper databaseHelper=new DatabaseHelper(this);
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.email_choose);
        final EditText email;
        Button delete,cancel;
        email=dialog.findViewById(R.id.choose_email);
        delete=dialog.findViewById(R.id.choose_button);
        cancel=dialog.findViewById(R.id.cancel_button);
        delete.setText("Delete");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().equals(""))
                {
                    Toast.makeText(SecondActivity.this, "please add your email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseHelper.delete(email.getText().toString());
                    dialog.cancel();
                    inflateLayout();
                    Cursor cursor=databaseHelper.getData();
                    if(cursor.getCount()==0)
                    {
                        Intent intent=new Intent(SecondActivity.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SecondActivity.this, "Database cleared", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(SecondActivity.this, "data deleted successfully", Toast.LENGTH_SHORT).show();

                }

            }
        });
        dialog.show();
    }

    private void updateData(final String email) {

        final DatabaseHelper helper=new DatabaseHelper(this);
        final Dialog dialog=new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_details);
        final EditText update_name,update_id,update_no;
        final Button update_data,cancel;
        update_id=dialog.findViewById(R.id.update_id);
        update_name=dialog.findViewById(R.id.update_name);
        update_no=dialog.findViewById(R.id.update_phone);
        update_data=dialog.findViewById(R.id.data_update);
        cancel=dialog.findViewById(R.id.data_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(update_id.getText().toString().equals("") || update_name.getText().toString().equals("") || update_no.getText().toString().equals(""))
                {
                    Toast.makeText(SecondActivity.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UserData data=new UserData();
                    data.setName(update_name.getText().toString().trim());
                    data.setId(Integer.parseInt(update_id.getText().toString()));
                    data.setPhone(update_no.getText().toString().trim());
                    data.setEmail(null);
                    data.setImageBytes(null);
                    helper.updateData(data,email);
                    inflateLayout();
                    dialog.cancel();
                    Toast.makeText(SecondActivity.this, "data Updated Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dialog.show();

    }

    public void permissionCheck()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, req_code);
        }
    }
    }
