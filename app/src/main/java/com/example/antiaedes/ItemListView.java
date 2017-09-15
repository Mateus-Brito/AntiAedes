package com.example.antiaedes;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mateus on 23/07/2016.
 */
public class ItemListView
{
    private String texto;
    private Drawable iconeRid;
    private int id;

    public ItemListView()
    {
    }

    public ItemListView(int id, String texto, Drawable iconeRid)
    {
        this.id=id;
        this.texto = texto;
        this.iconeRid = iconeRid;

    }

    public Drawable getIconeRid()
    {
        return iconeRid;
    }

    public void setIconeRid(Drawable iconeRid)
    {
        this.iconeRid = iconeRid;
    }

    public String getTexto()
    {
        return texto;
    }

    public void setTexto(String texto)
    {
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}