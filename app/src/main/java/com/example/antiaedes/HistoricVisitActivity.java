package com.example.antiaedes;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antiaedes.dao.VisitaDao;
import com.example.antiaedes.entities.Visita;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class HistoricVisitActivity extends Activity {

    private EditText mCep;
    private EditText mNumHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_visit);

        mCep = (EditText) findViewById(R.id.historic_visit_cep);
        mNumHouse = (EditText) findViewById(R.id.historic_num_house);

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            try {
                String[] vl = contents.split("-");
                int situation = Integer.parseInt(vl[0]);
                String cep = vl[1];
                int number = Integer.parseInt(vl[2]);
                String neighborhood = vl[3];
                String street = vl[4];
                fillFields(cep,number);
            } catch (ClassCastException e) {
                Toast.makeText(getBaseContext(), R.string.qrcode_error, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void fillFields(String cep, int num_house){
        mCep.setText(cep);
        mNumHouse.setText(num_house+"");
    }

    public void readQRCode(View view) {
        //Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
        //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        //startActivityForResult(intent, 0);
    }

    public void searchHistoric(View view){
        VisitaDao visitaDao = new VisitaDao();
        ArrayList<Visita>  arrayList = visitaDao.getVisitsBy(mCep.getText().toString(), Integer.parseInt(!mNumHouse.getText().toString().isEmpty() ? mNumHouse.getText().toString():"0"));
        if(arrayList!=null) {
            Intent intent = new Intent(HistoricVisitActivity.this, ListVisitsActivity.class);
            intent.putExtra("visits", arrayList);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Dados inválidos!", Toast.LENGTH_LONG).show();
        }
    }
}
