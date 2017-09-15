package com.example.antiaedes;

import android.graphics.drawable.Drawable;

/**
 * Created by Mateus on 23/07/2016.
 */
public class ItemListView
{
    private String texto;
    private Drawable iconeRid;

    public ItemListView()
    {
    }

    public ItemListView(String texto, Drawable iconeRid)
    {
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

}