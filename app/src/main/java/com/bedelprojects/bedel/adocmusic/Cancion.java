package com.bedelprojects.bedel.adocmusic;

import java.io.Serializable;

public class Cancion implements Serializable
{
    private String nombre;
    private String artista;
    private String letra;
    private String url;
    private String id;
    private String estilo;

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getLetra()
    {
        return letra;
    }

    public void setLetra(String letra)
    {
        this.letra = letra;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getArtista()
    {
        return artista;
    }

    public void setArtista(String artista)
    {
        this.artista = artista;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
    public String getEstilo()
    {
        return estilo;
    }

    public void setEstilo(String estilo)
    {
        this.estilo = estilo;
    }

    public Cancion(String nombre, String artista, String letra, String url, String id, String estilo)
    {
        this.nombre = nombre;
        this.artista = artista;
        this.letra = letra;
        this.url = url;
        this.id = id;
        this.estilo = estilo;
    }
}
