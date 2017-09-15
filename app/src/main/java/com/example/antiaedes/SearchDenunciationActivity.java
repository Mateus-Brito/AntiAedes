package com.example.antiaedes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.antiaedes.dao.DenunciaDao;
import com.example.antiaedes.entities.Denuncia;

import java.util.ArrayList;

public class SearchDenunciationActivity extends AppCompatActivity {

    private ListView mDenunciations;
    private ListView mPriority;
    private TabHost host;

    private Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_denunciation);

        mDenunciations = (ListView) findViewById(R.id.list_denunciations);
        mPriority = (ListView) findViewById(R.id.list_priority);

        if (getIntent().hasExtra("session")) {
            mSession = (Session) getIntent().getSerializableExtra("session");
        }
        host = (TabHost) findViewById(R.id.tabHost);
        TabHost.TabSpec spec = host.newTabSpec("Denuncias");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        spec = host.newTabSpec("Prioridades");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        DenunciaDao denunciaDao = new DenunciaDao();
        final ArrayList<Denuncia> denuncias = denunciaDao.getAllDenunciation();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getListDenunciations(denuncias));
        mDenunciations.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getPriority(denuncias));
        mPriority.setAdapter(adapter2);

        mDenunciations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), InformationDenunciationActivity.class);
                intent.putExtra("session", mSession);
                intent.putExtra("denuncia", denuncias.get(position));
                startActivity(intent);
            }
        });
    }

    public String[] getListDenunciations(ArrayList<Denuncia> denuncias) {
        String[] list = new String[denuncias.size()];
        for (int i = 0; i < denuncias.size(); i++) {
            list[i] = getType(denuncias.get(i).getTipo());
        }
        return list;
    }

    public String getType(int index) {
        switch (index) {
            case 0:
                return "Suspeita do Mosquit";
            case 1:
                return "Casos de Zica";
            case 2:
                return "Casos de Dengue";
            case 3:
                return "Casos de Chikungunya";
            case 4:
                return "Casos de Febre Amarela";
            default:
                return "Desconhecido";
        }
    }

    public String[] getPriority(ArrayList<Denuncia> denuncias) {
        String[] newArray = new String[denuncias.size()];
        for (int i = 0; i < denuncias.size(); i++) {
            if (denuncias.get(i).isPrioridade())
                newArray[i] = getType(denuncias.get(i).getTipo());
        }
        return newArray;
    }
}
