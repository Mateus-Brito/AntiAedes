package com.example.antiaedes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainFunctionaryActivity extends Activity {

    private Menu menu;
    private Session mSession;
    private MenuItem mName;
    private MenuItem reputacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_functionary);

        if(getIntent().hasExtra("registrado"))
            Toast.makeText(this, "visita registrada!",Toast.LENGTH_LONG).show();

        if(getIntent().hasExtra("denunciado"))
            Toast.makeText(this, "Denuncia realizada!",Toast.LENGTH_LONG).show();

        if(getIntent().hasExtra("session"))
            mSession = (Session) getIntent().getSerializableExtra("session");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if(mSession!=null) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem count = menu.findItem(R.id.menu_reputacao);
            count.setTitle("");
            return super.onCreateOptionsMenu(menu);
        } else return false;
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
        if (mSession != null) intent.putExtra("session", mSession);
        startActivity(intent);
    }

    public void leave(View view){
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    public void openRegisterVisit(View view){
        Intent intent = new Intent(this, RegisterVisitActivity.class);
        if (mSession != null) intent.putExtra("session", mSession);
        startActivity(intent);
    }

    public void openHistoricVisit(View view){
        Intent intent = new Intent(this, HistoricVisitActivity.class);
        if (mSession != null) intent.putExtra("session", mSession);
        startActivity(intent);
    }

    public void onQuit(MenuItem  item){
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}
