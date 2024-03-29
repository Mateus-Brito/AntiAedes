package com.example.antiaedes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.antiaedes.dao.DenunciaDao;
import com.example.antiaedes.entities.Denuncia;

import java.util.ArrayList;

import static android.R.attr.id;

public class AdapterListView extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<ItemListView> itens;
    private int[] colors = new int[] { Color.parseColor("#FFFFFF"), Color.parseColor("#00E676") };

    public AdapterListView(Context context, ArrayList<ItemListView> itens)
    {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }
    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount()
    {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public ItemListView getItem(int position)
    {
        return itens.get(position);
    }

    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        //Pega o item de acordo com a posção.
        ItemListView item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.listview_item, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.


        if(isResolved(itens.get(position).getId()))
            view.setBackgroundColor(colors[1]);
        else
            view.setBackgroundColor(colors[0]);
        ((TextView) view.findViewById(R.id.textview)).setText(item.getTexto());
        ((ImageView) view.findViewById(R.id.imagemview)).setImageDrawable(item.getIconeRid());

        return view;
    }

    public boolean isResolved(int id){
        DenunciaDao dd = new DenunciaDao();
        for(Denuncia d : dd.getAllDenunciationsActives()){
            if(d.getId()==id)
                return false;
        }
        return true;
    }
}