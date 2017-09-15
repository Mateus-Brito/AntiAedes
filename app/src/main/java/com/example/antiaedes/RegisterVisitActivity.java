package com.example.antiaedes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.antiaedes.dao.VisitaDao;
import com.example.antiaedes.entities.Denuncia;
import com.example.antiaedes.entities.Visita;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterVisitActivity extends AppCompatActivity {

    private Spinner mSituation;
    private EditText mCep;
    private EditText mNumHouse;
    private EditText mNeighborhood;
    private EditText mStreet;
    private EditText mObservation;

    private GPSLocation gps;
    private Session mSession;
    private Denuncia hasDenunciation;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_visit);

        if (getIntent().hasExtra("session"))
            mSession = (Session) getIntent().getSerializableExtra("session");
        if (getIntent().hasExtra("denunciation"))
            hasDenunciation = (Denuncia) getIntent().getSerializableExtra("denunciation");

        mSituation = (Spinner) findViewById(R.id.visit_spinner);
        mCep = (EditText) findViewById(R.id.visit_cep);
        mNumHouse = (EditText) findViewById(R.id.visit_num_house);
        mNeighborhood = (EditText) findViewById(R.id.visit_neighborhood);
        mStreet = (EditText) findViewById(R.id.denunciation_street);
        mObservation = (EditText) findViewById(R.id.visit_observation);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.visit_situation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSituation.setAdapter(adapter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

        gps = new GPSLocation(this);
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gps);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void readQRCode(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                try {
                    String[] vl = contents.split("-");
                    int situation = Integer.parseInt(vl[0]);
                    String cep = vl[1];
                    int number = Integer.parseInt(vl[2]);
                    String neighborhood = vl[3];
                    String street = vl[4];
                    fillFields(situation, cep, number, neighborhood, street);
                } catch (ClassCastException e) {
                    Toast.makeText(getBaseContext(), R.string.qrcode_error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getBaseContext(), R.string.qrcode_canceled, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void fillFields(int situation, String cep, int num_house, String neighborhood, String street) {
        mSituation.setId(situation);
        mCep.setText(cep);
        mNumHouse.setText(num_house);
        mNeighborhood.setText(neighborhood);
        mStreet.setText(street);
    }

    public void registerVisit(View view) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar date = Calendar.getInstance();
        Visita visita = new Visita();
        visita.setId_fun(mSession.getId());
        visita.setSituation(mSituation.getSelectedItemPosition());
        visita.setData(dateFormat.format(date.getTimeInMillis()));
        visita.setObservacao(mObservation.getText().toString());
        VisitaDao visitaDao = new VisitaDao();

        Denuncia denuncia = new Denuncia();
        denuncia.setCep(mCep.getText().toString());
        denuncia.setData(dateFormat.format(date.getTimeInMillis()));
        denuncia.setNum_casa(Integer.parseInt(mNumHouse.getText().toString()));
        denuncia.setCodigo(20);
        denuncia.setBairro(mNeighborhood.getText().toString());
        denuncia.setLatitude(String.valueOf(gps.getLatitude()));
        denuncia.setLongitude(String.valueOf(gps.getLongitude()));
        denuncia.setDescricao(mObservation.getText().toString());
        denuncia.setId_fun(mSession.getId());
        visitaDao.registerVisit2(visita, denuncia);
        Intent intent = new Intent(this, MainFunctionaryActivity.class);
        intent.putExtra("registrado","registrado");
        intent.putExtra("session",mSession);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterVisit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.antiaedes/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterVisit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.antiaedes/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
