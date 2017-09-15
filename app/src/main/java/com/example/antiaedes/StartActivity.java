package com.example.antiaedes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this.getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    public void goToMain(View  view){
        Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
