package com.example.antiaedes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Session mSession;
    private View mView;
    private Menu menu;
    static Button notifCount;
    static int mNotifCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("denunciado"))
            Toast.makeText(this, "Denuncia realizada!",Toast.LENGTH_LONG).show();

        if (getIntent().hasExtra("session")) {
            mSession = (Session) getIntent().getSerializableExtra("session");

        }

        //else
            //mView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
        if(mSession!=null) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem count = menu.findItem(R.id.menu_reputacao);
            count.setTitle(mSession.getSaldo()+" pontos");
            return super.onCreateOptionsMenu(menu);
        } else return false;
    }

    private void setReputacion(int value){
        MenuItem count = menu.findItem(R.id.menu_reputacao);
        count.setTitle(value+"");
        invalidateOptionsMenu();
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

    public void onQuit(MenuItem  item){
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}
