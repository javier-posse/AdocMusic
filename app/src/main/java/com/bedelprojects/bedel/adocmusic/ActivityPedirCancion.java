package com.bedelprojects.bedel.adocmusic;

import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityPedirCancion extends AppCompatActivity
{

    EditText nombreCanci, nombreArtista, enlace;
    TextView msgError;
    Button btnEnviar;
    String sNombreCanci, sNombreArtista, sEnlace;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedircancion);

        nombreCanci =(EditText)findViewById(R.id.etNombreCancion);
        nombreArtista =(EditText)findViewById(R.id.etNombreArtista);
        enlace = (EditText)findViewById(R.id.etEnlace);
        btnEnviar =(Button)findViewById(R.id.btnEnviarCancion);
        msgError=(TextView)findViewById(R.id.tvErrorEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sNombreArtista=nombreArtista.getText().toString();
                sNombreCanci=nombreCanci.getText().toString();
                sEnlace=enlace.getText().toString();
                if (TextUtils.isEmpty(sNombreArtista)||TextUtils.isEmpty(sNombreCanci)||TextUtils.isEmpty(sEnlace))
                {
                    msgError.setText("¡ERROR! No puede haber campos vacíos.");
                }
                else
                {
                    //Meto el split en un try-catch porque existe la posibilidad de que no tenga la estructura deseada, y si no lo hago así, la aplicación probablemente cerraría sin más, siendo poco robusta
                    try
                    {
                        sEnlace=sEnlace.split("https://youtu.be/")[1];
                    }
                    catch (Exception e)
                    {
                        //TODO cambiar este mensaje de error por dios, es muy poco descriptivo
                        msgError.setText("¡ERROR! Error en el campo enlace");
                    }
                    enviarCancion(SingeltonUsuario.nUsuario, sNombreCanci, sNombreArtista, sEnlace);
                }
            }
        });

        //Si el usuario va a añadir una nueva canción, es mejor que el mensaje de error se borre. Esto lo puedo hacer con un OnClick en cada editText

        nombreCanci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgError.setText("");
            }
        });
        nombreArtista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgError.setText("");
            }
        });
        enlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgError.setText("");
            }
        });

    }
    private void enviarCancion(String user, String nombreCancion, String artista, String enlace)
    {

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("nombre", nombreCancion);
        params.put("artista", artista);
        params.put("enlace", enlace);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREAT_SONG, params, Api.CODE_POST_REQUEST);
        request.execute();
    }
    private void cancionEnviada()
    {
        Toast.makeText(this, "Canción enviada correctamente", Toast.LENGTH_SHORT);
        nombreArtista.setText("");
        nombreCanci.setText("");
        enlace.setText("");
        msgError.setTextColor(Color.GREEN);
        msgError.setText("Canción enviada correctamente");
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String>
    {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode)
        {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                Log.d("Enviando cancion", s);
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error"))
                {
                    if (object.has("inserted")&&object.getBoolean("inserted"))
                    {
                        cancionEnviada();
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == Api.CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == Api.CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
