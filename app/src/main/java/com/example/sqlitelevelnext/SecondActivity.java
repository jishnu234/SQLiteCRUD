package com.example.sqlitelevelnext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
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
                Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteAll:
                Toast.makeText(this, "deleteAll", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
        }
    public void permissionCheck()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, req_code);
        }
    }
    }
