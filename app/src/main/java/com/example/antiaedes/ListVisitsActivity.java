package com.example.antiaedes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.antiaedes.entities.SeeObservationActivity;
import com.example.antiaedes.entities.Visita;

import java.util.ArrayList;

public class ListVisitsActivity extends Activity {

    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_visits);

        if (getIntent().hasExtra("visits")) {
            final ArrayList<Visita> visitas = (ArrayList<Visita>) getIntent().getSerializableExtra("visits");
            mList = (ListView) findViewById(R.id.list_denunciations_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, getListVisits(visitas));
            mList.setAdapter(adapter);

            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(view.getContext(), SeeObservationActivity.class);
                    i.putExtra("obs",visitas.get(position).getObservacao());
                    startActivity(i);
                }
            });
        }
    }

    public String[] getListVisits(ArrayList<Visita> list) {
        String[] visits = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String situacao = "Pendente";
            if(list.get(i).getSituation() == 0)
                situacao = "Resolvido";
            else if(list.get(i).getSituation() == 1)
                situacao = "Pendente";
            else
                situacao = "Falso Alarme";
            visits[i] = list.get(i).getData().toString() + " - " + situacao;
        }
        return visits;
    }
}
