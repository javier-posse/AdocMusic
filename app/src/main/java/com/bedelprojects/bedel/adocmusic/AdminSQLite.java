package com.bedelprojects.bedel.adocmusic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLite extends SQLiteOpenHelper {

    public AdminSQLite(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, nombre, factory, version);

    }

    @Override

    public void onCreate(SQLiteDatabase db)
    {

        //aquí creamos la tabla de usuario (nombre)
        //Solo almaceno el nombre, porque en realidad una vez he comprobado que la contraseña ha sido correcta una vez, mejor ahorrar esa verificación a futuro. Además es más seguro
        //TODOS LOS NOMBRES TIENEN QUE SER COMO EN LA BD DEL SERVER. NO TOCAR A NO SER QUE SE CAMBIE EN EL PROPIO SERVIDOR
        db.execSQL("create table usuario(nombre text)");
        db.execSQL("create table estilos(estilo text, seleccionado text)");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Asiatico', 'true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('BSO','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Clasica','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Electronica','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Metal','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Pop','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Rap','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Reggaeton','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Rock','true') ");
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int version1, int version2)
    {

        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists estilos");

        db.execSQL("create table usuario(nombre text)");
        db.execSQL("create table estilos(estilo text, seleccionado text)");

        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Asiatico','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('BSO','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Clasica','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Electronica','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Metal','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Pop','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Rap','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Reggaeton','true') ");
        db.execSQL("INSERT INTO estilos (estilo,seleccionado) VALUES ('Rock','true') ");

    }

}