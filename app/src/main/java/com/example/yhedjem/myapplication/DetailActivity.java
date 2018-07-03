package com.example.yhedjem.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String name = getIntent().getStringExtra("name");
        String msg = getIntent().getStringExtra("msg");

        TextView tv_name = findViewById(R.id.address);
        tv_name.setText(name);

    }
}
