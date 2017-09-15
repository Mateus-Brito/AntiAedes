package com.example.antiaedes;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.antiaedes.dao.DenunciaDao;
import com.example.antiaedes.dao.VisitaDao;
import com.example.antiaedes.entities.Denuncia;
import com.example.antiaedes.entities.Visita;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class InfoDenunciationActivity extends AppCompatActivity {

    private ImageView denImg;
    private EditText denCEP;
    private EditText denBairro;
    private EditText denRua;
    private EditText denCidade;
    private EditText denReferencias;
    private EditText denDetalhes;
    private Button solucionar;
    private Denuncia den;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_denunciation);

        denImg = (ImageView) findViewById(R.id.denImg);
        denCEP = (EditText) findViewById(R.id.denCep);
        denCEP.setKeyListener(null);
        denBairro = (EditText) findViewById(R.id.denBairro);
        denBairro.setKeyListener(null);
        denRua = (EditText) findViewById(R.id.denRua);
        denRua.setKeyListener(null);
        denCidade = (EditText) findViewById(R.id.denCidade);
        denCidade.setKeyListener(null);
        denReferencias = (EditText) findViewById(R.id.denReferencia);
        denReferencias.setKeyListener(null);
        denDetalhes = (EditText) findViewById(R.id.denDetalhes);
        denDetalhes.setKeyListener(null);
        solucionar = (Button) findViewById(R.id.btDen);

        if (getIntent().hasExtra("denunciations") && getIntent().hasExtra("session")) {
            den = (Denuncia) getIntent().getSerializableExtra("denunciations");
            session = (Session) getIntent().getSerializableExtra("session");
            if(!den.getImagem().isEmpty())
                denImg.setImageDrawable(getImage(den.getImagem()));
            denCEP.setText(den.getCep());
            denBairro.setText(den.getBairro());
            denRua.setText(den.getRua());
            denCidade.setText(den.getCidade());
            denReferencias.setText(den.getReferencia());
            denDetalhes.setText(den.getDescricao());

            if(foiSolucionado(den.getId())) {
                solucionar.setClickable(false);
                solucionar.setText("Solucionado");
            }else{
                solucionar.setClickable(true);
                solucionar.setText("Solucionar");
            }
        }
    }

    public boolean foiSolucionado(int denid){
        DenunciaDao denunciaDao = new DenunciaDao();
        ArrayList<Denuncia> vs = denunciaDao.getAllDenunciationsActives();
        if(vs!=null) {
            for (Denuncia v : vs) {
                if (v.getId() == denid) {
                    return false;
                }
            }
        }
        return true;
    }
    public Drawable getImage(String img) {
        try {
            InputStream stream;

            Bitmap bb = StringToBitMap(img);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bb.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

            Drawable top = new BitmapDrawable(getResources(), bb);
            top = new ScaleDrawable(top, 0, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT).getDrawable();
            top.setBounds(0, 0, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            return top;
        } catch (Exception e) {
            Log.d("ERRO", e.getMessage().toString());
            return null;
        }

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void solucionar(View view){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar date = Calendar.getInstance();
        Visita visita = new Visita();
        visita.setSituation(0);
        visita.setData(dateFormat.format(date.getTimeInMillis()));
        visita.setId_den(den.getId());
        visita.setId_fun(session.getId());
        VisitaDao visitaDao = new VisitaDao();
        if(visitaDao.registerVisit(visita)){
            Toast.makeText(this,"Solucionado",Toast.LENGTH_LONG).show();
            solucionar.setClickable(false);
            solucionar.setText("Solucionado");
        }else{
            Toast.makeText(this,"Ocorreu um erro!",Toast.LENGTH_LONG).show();
        }
    }
}
