package com.example.bt9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleImageView circleImageView = findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(Profile_Activity.this,MainActivity.class);
                startActivity(itent);
                finish();
            }
        });
    }
}
