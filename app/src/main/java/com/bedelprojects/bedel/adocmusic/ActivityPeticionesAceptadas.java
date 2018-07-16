package com.bedelprojects.bedel.adocmusic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

public class ActivityPeticionesAceptadas extends AppCompatActivity
{

    TextView nivel, cancionesAceptadas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticionesaceptadas);

        nivel=(TextView)findViewById(R.id.tvUserLevel);
        cancionesAceptadas=(TextView)findViewById(R.id.tvAceptadas);
        cancionesAceptadas.setMovementMethod(new ScrollingMovementMethod());



        comprobarCancionesAceptadas(SingeltonUsuario.nUsuario);

    }

    private void cambiarTvs(JSONArray canciones) throws JSONException
    {
        String canci="";
        for (int i = 0; i < canciones.length(); i++)
        {
            canci=canci+canciones.get(i)+"\n\n";
        }
        cancionesAceptadas.setText(canci);
        nivel.setText((""+canciones.length()));

    }

    private void comprobarCancionesAceptadas(String user)
    {

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ACCEP, params, Api.CODE_POST_REQUEST);
        request.execute();
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
                Log.d("En Post", s);
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error"))
                {
                    cambiarTvs(object.getJSONArray("canciones"));
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
