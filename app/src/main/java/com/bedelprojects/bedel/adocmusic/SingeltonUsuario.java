package com.bedelprojects.bedel.adocmusic;

import android.util.Log;

public class SingeltonUsuario
{

    public static String nUsuario;

    private static  SingeltonUsuario usuario=null;

    public static SingeltonUsuario getUsuario (String nUsuario)
    {
        if (usuario==null)
        {
            usuario = new SingeltonUsuario(nUsuario);
        }
        return usuario;
    }
    public static void setNull()
    {
        usuario=null;
    }

    private SingeltonUsuario (String nUsuario)
    {
        this.nUsuario=nUsuario;
    }
    public String getnUsuario()
    {
        return nUsuario;
    }

    public void setnUsuario(String nUsuario)
    {
        this.nUsuario=nUsuario;
    }
    @Override
    public SingeltonUsuario clone()
    {
        try
        {
            throw new CloneNotSupportedException();
        }
        catch (CloneNotSupportedException ex)
        {
            Log.d("Singelto", "Ya existe un usuario");
        }
        return null;
    }
}
