package com.example.antiaedes.entities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antiaedes.R;

public class SeeObservationActivity extends Activity {

    private EditText obs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_observation);

        obs = (EditText) findViewById(R.id.verObservacao);
        obs.setText(getIntent().getExtras().getString("obs"));
        //Toast.makeText(this.getApplicationContext(), getIntent().getExtras().getString("obs"), Toast.LENGTH_SHORT).show();
    }

}
