package com.example.antiaedes;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Session mSession;
    private View mView;
    private TextView mCoins;
    private TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("denunciado"))
            Toast.makeText(this, "Denuncia realizada!",Toast.LENGTH_LONG).show();

        mView = findViewById(R.id.user_panel);
        mName = (TextView) findViewById(R.id.main_username);
        mCoins = (TextView) findViewById(R.id.tvCoins);

        if (getIntent().hasExtra("session")) {
            mSession = (Session) getIntent().getSerializableExtra("session");
            mName.setText(mSession.getNome());
            mCoins.setText(mSession.getSaldo()+"");
        }
        else
            mView.setVisibility(View.GONE);
    }

    public void denounce(View view) {
        Intent intent = new Intent(this, DenunciationActivity.class);
        if (mSession != null) intent.putExtra("session", mSession);
        startActivity(intent);
    }

    public void openMaps(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        if (mSession != null) intent.putExtra("session", mSession);
        startActivity(intent);
    }

    public void openCare(View view) {
        Intent intent = new Intent(this, CareActivity.class);
        startActivity(intent);
    }

    public void leave(View view){
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}
