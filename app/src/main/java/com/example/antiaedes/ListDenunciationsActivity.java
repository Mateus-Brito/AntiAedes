package com.example.antiaedes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.antiaedes.dao.DenunciaDao;
import com.example.antiaedes.entities.Denuncia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ListDenunciationsActivity extends Activity {

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

        mSpinner= (Spinner) findViewById(R.id.btVisu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.denunciation_priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        if (getIntent().hasExtra("denunciations") && getIntent().hasExtra("session")) {
            denuncias = (InfoMarker) getIntent().getSerializableExtra("denunciations");
            mSession = (Session) getIntent().getSerializableExtra("session");
            listView = (ListView) findViewById(R.id.list_visits);
            createListView(denuncias);
        }

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    createListView(denuncias);
                }else if(position == 1){
                    createListView(denuncias,true);
                }else if(position==2){
                    createListView(denuncias,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void createListView(InfoMarker infoMarker, boolean resolvido) {
        //Criamos nossa lista que preenchera o ListView
        final ArrayList<Denuncia> list = new ArrayList<>();
        if(!resolvido){
            list.addAll(infoMarker.getDenuciationsResolved());
        }else{
            list.addAll(infoMarker.getDenuciationsPending());
        }
        itens = new ArrayList<ItemListView>();
        for (int i = 0; i < list.size(); i++) {
            DenunciaDao dn = new DenunciaDao();
            Denuncia dd = dn.getDenunciaById(list.get(i).getId());
            if (dd!=null){
                if(dd.getImagem()!=null) {
                    if (!dd.getImagem().isEmpty())
                        list.get(i).setImagem(dd.getImagem());
                }
            }
            ItemListView item = new ItemListView(list.get(i).getId(),getTipo(list.get(i).getTipo()), list.get(i).getImagem()!=null ? getImage(list.get(i).getImagem()) : null);
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
                intent.putExtra("denunciation",list.get(position));
                intent.putExtra("session",mSession);
                startActivity(intent);
            }
        });
        adapterListView.notifyDataSetChanged();
    }

    private void createListView(InfoMarker infoMarker) {
        final ArrayList<Denuncia> list = infoMarker.getDenunciations();

        itens = new ArrayList<ItemListView>();
        for (int i = 0; i < list.size(); i++) {
            DenunciaDao dn = new DenunciaDao();
            Denuncia dd = dn.getDenunciaById(list.get(i).getId());
            if (dd!=null){
            if(dd.getImagem()!=null) {
                if (!dd.getImagem().isEmpty())
                    list.get(i).setImagem(dd.getImagem());
            }
            }
            ItemListView item = new ItemListView(list.get(i).getId(),getTipo(list.get(i).getTipo()), list.get(i).getImagem()!=null ? getImage(list.get(i).getImagem()) : null);
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
                intent.putExtra("denunciation",list.get(position));
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
                return "????";
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
