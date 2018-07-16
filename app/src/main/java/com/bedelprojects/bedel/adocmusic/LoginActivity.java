package com.bedelprojects.bedel.adocmusic;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity
{


    EditText user;
    EditText pass;
    EditText mail;
    TextView msgError;
    Button iniciar;
    Button desaparecer;
    Button registrar;

    String sUser, sPass, sMail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.etUser);
        pass = (EditText)findViewById(R.id.etPass);
        mail =(EditText)findViewById(R.id.etMail);
        iniciar = (Button)findViewById(R.id.btnIniciar);
        desaparecer = (Button)findViewById(R.id.btnDesaparecer);
        registrar = (Button)findViewById(R.id.btnRegistro);
        msgError = (TextView)findViewById(R.id.tvError);

        //onClick del inicio de sesión
        iniciar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                msgError.setText("");
                sUser = user.getText().toString();
                sPass = pass.getText().toString();


                comprobarUser(sUser, sPass);

            }
        });

        //OnClik que del botón que abre el registro
        desaparecer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //hago desaparecer el botón de inicio de sesión, y hago aparecer el tv del mail
                mail.setVisibility(View.VISIBLE);
                iniciar.setVisibility(View.GONE);
                desaparecer.setVisibility(View.GONE);
                registrar.setVisibility(View.VISIBLE);

                msgError.setText("");

            }
        });
        //OnClick del botón para registrar al usuario
        registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sUser = user.getText().toString();
                sPass = pass.getText().toString();
                sMail = mail.getText().toString();

                registrarUser(sUser, sPass, sMail);
            }
        });
    }
    private void lanzarActivity(JSONObject iniciado) throws JSONException
    {
        boolean b=false;
            if (iniciado.get("exist").equals("true"))
            {
                b=true;
            }

        if (b)
        {
            AdminSQLite bdd = new AdminSQLite(this, "administracion", null, 4);
            SQLiteDatabase db = bdd.getWritableDatabase();
            ContentValues guardarUser = new ContentValues();

            guardarUser.put("nombre",sUser);
            db.insert("usuario",null, guardarUser);
            SingeltonUsuario.getUsuario(sUser);

            Intent inte=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(inte);

            finish();
        }
        else
        {
            msgError.setText("Usuario o contraseña incorrectos");
            msgError.requestFocus();
        }


    }

    private void comprobarUser(String user, String pass)
    {

        if (TextUtils.isEmpty(user))
        {
            msgError.setText("El campo de usuario está vacío.");
            msgError.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass))
        {
            msgError.setText("El campo de contraseña está vacío.");
            msgError.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("pass", pass);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_USER_EXIST, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    private void registrarUser(String user, String pass, String mail)
    {

        if (TextUtils.isEmpty(user))
        {
            msgError.setText("El campo de usuario está vacío.");
            msgError.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass))
        {
            msgError.setText("El campo de contraseña está vacío.");
            msgError.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mail))
        {
            msgError.setText("El campo de mail está vacío.");
            msgError.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("pass", pass);
        params.put("mail", mail);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_REGIS_USER, params, Api.CODE_POST_REQUEST);
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
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                Log.d("En post", s);
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error"))
                {
                    Log.d("En post", ""+object.toString());
                    lanzarActivity(object);
                }
            }
            catch (JSONException e) {
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
