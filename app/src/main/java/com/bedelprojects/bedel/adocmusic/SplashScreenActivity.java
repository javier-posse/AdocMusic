package com.bedelprojects.bedel.adocmusic;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class SplashScreenActivity extends Activity
{

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final boolean b;
        //Quito la barra
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);

        //Creo la base de datos
        AdminSQLite bdd = new AdminSQLite(this, "administracion", null, 4);
        SQLiteDatabase db = bdd.getWritableDatabase();

        //Compruebo si se ha iniciado ya sesión alguna vez y está guardada
        Cursor consultaUser = db.rawQuery("select nombre from usuario", null);
        if (consultaUser.moveToFirst())
        {
            b=true;
            SingeltonUsuario.getUsuario(consultaUser.getString(0));
        }
        else
        {
            b=false;
        }

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {

                //En caso de que haya un user guardado, accedo directamente a la aplicación. Si no, a iniciar/registrar
                if(b)
                {
                    Intent mainIntent = new Intent().setClass(
                            SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                else
                {
                    Intent loginIntent = new Intent().setClass(
                            SplashScreenActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                // Cierro activity para que el usuario no pueda volver al splashscreen
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

}