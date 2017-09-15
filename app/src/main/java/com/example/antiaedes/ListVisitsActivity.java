package com.example.antiaedes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.antiaedes.entities.Visita;

import java.util.ArrayList;

public class ListVisitsActivity extends AppCompatActivity {

    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_visits);

        if (getIntent().hasExtra("visits")) {
            ArrayList<Visita> visitas = (ArrayList<Visita>) getIntent().getSerializableExtra("visits");
            mList = (ListView) findViewById(R.id.list_denunciations_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, getListVisits(visitas));
            mList.setAdapter(adapter);
        }
    }

    public String[] getListVisits(ArrayList<Visita> list) {
        String[] visits = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String situacao = "Pendente";
            if(list.get(i).getSituation() == 1)
                situacao = "Pendente";
            visits[i] = list.get(i).getData().toString() + " - " + situacao;
        }
        return visits;
    }
}
