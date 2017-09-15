package com.example.antiaedes;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.antiaedes.dao.DenunciaDao;
import com.example.antiaedes.dao.VisitaDao;
import com.example.antiaedes.entities.Denuncia;
import com.example.antiaedes.entities.Visita;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class InfoDenunciationActivity extends Activity {

    private ImageView denImg;
    private EditText denCEP;
    private EditText denBairro;
    private EditText denRua;
    private EditText denReferencias;
    private EditText denOBS;
    private EditText denDetalhes;
    //private Spinner mTypeDenunciation;
    private Button solucionar;
    private Denuncia den;
    private Session session;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_denunciation);

        //mTypeDenunciation = (Spinner) findViewById(R.id.info_type_spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_of_denunciation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //mTypeDenunciation.setAdapter(adapter);

        denImg = (ImageView) findViewById(R.id.den_image);
        denCEP = (EditText) findViewById(R.id.denCep);
        denOBS = (EditText) findViewById(R.id.denOBS);
        denCEP.setKeyListener(null);
        denBairro = (EditText) findViewById(R.id.denBairro);
        denBairro.setKeyListener(null);
        denRua = (EditText) findViewById(R.id.denRua);
        denRua.setKeyListener(null);
        denReferencias = (EditText) findViewById(R.id.denReferencia);
        denReferencias.setKeyListener(null);
        denDetalhes = (EditText) findViewById(R.id.denDetalhes);
        denDetalhes.setKeyListener(null);
        solucionar = (Button) findViewById(R.id.btDen);

        if (getIntent().hasExtra("denunciation") && getIntent().hasExtra("session")) {
            den = (Denuncia) getIntent().getSerializableExtra("denunciation");
            session = (Session) getIntent().getSerializableExtra("session");
            if (!den.getImagem().isEmpty()){
                Bitmap bitmap = ((BitmapDrawable) getImage(den.getImagem())).getBitmap();
                if(bitmap!=null) {
                    Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 500, 500, true));
                    //scaleImage(denImg,getImage(den.getImagem()));
                    denImg.setImageDrawable(dr);
                    //denImg.setBackground();
                }
            }

            denCEP.setText(den.getCep());
            denBairro.setText(den.getBairro());
            denRua.setText(den.getRua());
            denReferencias.setText(den.getReferencia());
            denDetalhes.setText(den.getDescricao());
            if (foiSolucionado(den.getId())) {
                solucionar.setClickable(false);
                solucionar.setText("Solucionado");
            } else {
                solucionar.setClickable(true);
                solucionar.setText("Solucionar");
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void scaleImage(ImageView view, Drawable draw)
    {
        Drawable drawing = draw;
        if (drawing == null) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding_x = ((View)view.getParent()).getWidth();//EXPECTED WIDTH
        int bounding_y = ((View)view.getParent()).getHeight();//EXPECTED HEIGHT

        float xScale = ((float) bounding_x) / width;
        float yScale = ((float) bounding_y) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(xScale, yScale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();
        BitmapDrawable result = new BitmapDrawable(this.getBaseContext().getResources(), scaledBitmap);

        view.setImageDrawable(result);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public boolean foiSolucionado(int denid) {
        DenunciaDao denunciaDao = new DenunciaDao();
        ArrayList<Denuncia> vs = denunciaDao.getAllDenunciationsActives();
        if (vs != null) {
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

    public void solucionar(View view) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar date = Calendar.getInstance();
        Visita visita = new Visita();
        visita.setSituation(0);
        visita.setObservacao(denOBS.getText().toString());
        visita.setData(dateFormat.format(date.getTimeInMillis()));
        visita.setId_den(den.getId());
        visita.setId_fun(session.getId());
        VisitaDao visitaDao = new VisitaDao();
        if (visitaDao.registerVisit(visita)) {
            Toast.makeText(this, "Solucionado", Toast.LENGTH_LONG).show();
            solucionar.setClickable(false);
            solucionar.setText("Solucionado");
        } else {
            Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("InfoDenunciation Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
