package com.example.antiaedes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.antiaedes.entities.Denuncia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ListDenunciationsActivity extends AppCompatActivity {

    private ArrayList<ItemListView> itens;
    private ListView listView;
    private AdapterListView adapterListView;
    private Spinner mSpinner;
    private Session mSession;
    private InfoMarker denuncias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_denunciations);

        mSpinner = (Spinner) findViewById(R.id.priority_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.denunciation_priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(1);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        createListView(denuncias);
                        break;
                    case 1:
                        createListView(denuncias,1);
                        break;
                    case 2:
                        createListView(denuncias,0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (getIntent().hasExtra("denunciations") && getIntent().hasExtra("session")) {
            denuncias = (InfoMarker) getIntent().getSerializableExtra("denunciations");
            mSession = (Session) getIntent().getSerializableExtra("session");
            listView = (ListView) findViewById(R.id.list_visits);
            createListView(denuncias,1);
        }
    }

    private void createListView(InfoMarker infoMarker, int prioridade) {
        //Criamos nossa lista que preenchera o ListView
        final ArrayList<Denuncia> list = infoMarker.getDenunciations();
        itens = new ArrayList<ItemListView>();
        for (int i = 0; i < list.size(); i++) {
            ItemListView item = new ItemListView(getTipo(list.get(i).getTipo()), !list.get(i).getImagem().isEmpty() ? getImage(list.get(i).getImagem()) : null);
            if(list.get(i).getPrioridade() == prioridade)
                itens.add(item);
        }

        //Cria o adapter
        adapterListView = new AdapterListView(this, itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), InfoDenunciationActivity.class);
                intent.putExtra("denunciations",list.get(position));
                intent.putExtra("session",mSession);
                startActivity(intent);
            }
        });
    }

    private void createListView(InfoMarker infoMarker) {
        //Criamos nossa lista que preenchera o ListView
        final ArrayList<Denuncia> list = infoMarker.getDenunciations();
        itens = new ArrayList<ItemListView>();
        for (int i = 0; i < list.size(); i++) {
            ItemListView item = new ItemListView(getTipo(list.get(i).getTipo()), !list.get(i).getImagem().isEmpty() ? getImage(list.get(i).getImagem()) : null);
                itens.add(item);
        }

        //Cria o adapter
        adapterListView = new AdapterListView(this, itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), InfoDenunciationActivity.class);
                intent.putExtra("denunciations",list.get(position));
                intent.putExtra("session",mSession);
                startActivity(intent);
            }
        });
    }

    public String getTipo(int id) {
        switch (id) {
            case 0:
                return "Suspeita de Foco";
            case 1:
                return "Foco Encontrado";
            default:
                return "";
        }
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
}
