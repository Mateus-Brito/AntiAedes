package com.example.antiaedes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.antiaedes.dao.VisitaDao;
import com.example.antiaedes.entities.Denuncia;
import com.example.antiaedes.entities.Visita;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InformationDenunciationActivity extends AppCompatActivity {

    private Session mSession;
    private Denuncia mDenuncia;

    private Spinner mTypeDenunciation;
    private EditText mCep;
    private EditText mNumHouse;
    private EditText mNeighborhood;
    private EditText mStreet;
    private EditText mReference;
    private EditText mObservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_denunciation);

        if(this.getIntent().hasExtra("session") && getIntent().hasExtra("denunciation")){
            mSession = (Session) getIntent().getSerializableExtra("session");
            mDenuncia = (Denuncia) getIntent().getSerializableExtra("denunciation");

            fillFields(mDenuncia);
        }

        mTypeDenunciation = (Spinner) findViewById(R.id.info_type_spinner);
        mCep = (EditText) findViewById(R.id.info_cep);
        mNumHouse = (EditText) findViewById(R.id.info_num_house);
        mNeighborhood = (EditText) findViewById(R.id.info_neighborhood);
        mStreet = (EditText) findViewById(R.id.info_street);
        mReference = (EditText) findViewById(R.id.info_reference);
        mObservation = (EditText) findViewById(R.id.info_observation);

    }

    public void fillFields(Denuncia denuncia){
        mTypeDenunciation.setId(denuncia.getTipo());
        mCep.setText(denuncia.getCep());
        mNumHouse.setText(denuncia.getNum_casa());
        mNeighborhood.setText(denuncia.getBairro());
        mStreet.setText(denuncia.getRua());
        mReference.setText(denuncia.getReferencia());
    }

    public void registerVisit(View view){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar date = Calendar.getInstance();
        VisitaDao visitaDao = new VisitaDao();
        Visita visita = new Visita();
        visita.setId_fun(mSession.getId());
        visita.setId_den(mDenuncia.getId());
        visita.setData(dateFormat.format(date.getTimeInMillis()));
        visita.setObservacao(mObservation.getText().toString());
        if(visitaDao.registerVisit(visita)){
            Intent intent = new Intent(view.getContext(), SearchDenunciationActivity.class);
            startActivity(intent);
        }else
            Toast.makeText(view.getContext(),R.string.error,Toast.LENGTH_LONG).show();
    }
}
