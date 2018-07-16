package com.bedelprojects.bedel.adocmusic;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityConfiguracion extends AppCompatActivity {

    TextView filtroEstilos, about, report;
    Switch swRepAut;
    Button btnAceptarFiltros, btnReporte;
    CheckBox cbAsiatica, cbBSO, cbClasica, cbElect, cbMetal, cbPop, cbRap, cbReaggeton, cbRock;
    ArrayList<CheckBox> lista= new ArrayList<>();
    EditText reportBug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        filtroEstilos = (TextView) findViewById(R.id.opcionFiltro);
        report= (TextView)findViewById(R.id.opcionReportBug);
        swRepAut =(Switch)findViewById(R.id.swVidAut);
        about=(TextView)findViewById(R.id.opcionAcercaDe);

        SharedPreferences prefe = getSharedPreferences("Ajustes", Context.MODE_PRIVATE);

        swRepAut.setChecked(prefe.getBoolean("reproduccion_automatica", true));

        //Listerner del click en la opción de la reproducción automática
        swRepAut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferencias=getSharedPreferences("Ajustes", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putBoolean("reproduccion_automatica", swRepAut.isChecked());
                editor.commit();
            }
        });

        //Listerner del click en la opción del filtro
        filtroEstilos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo el Alert Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConfiguracion.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_estilos, null);

                //Meto los cb en una lista, para recorrerla y asignar valores
                cbAsiatica=(CheckBox)view.findViewById(R.id.cbAsiatica);
                lista.add(cbAsiatica);
                cbBSO=(CheckBox)view.findViewById(R.id.cbBSO);
                lista.add(cbBSO);
                cbClasica=(CheckBox)view.findViewById(R.id.cbClasica);
                lista.add(cbClasica);
                cbElect=(CheckBox)view.findViewById(R.id.cbElect);
                lista.add(cbElect);
                cbMetal=(CheckBox)view.findViewById(R.id.cbMetal);
                lista.add(cbMetal);
                cbPop=(CheckBox)view.findViewById(R.id.cbPop);
                lista.add(cbPop);
                cbRap=(CheckBox)view.findViewById(R.id.cbRap);
                lista.add(cbRap);
                cbReaggeton=(CheckBox)view.findViewById(R.id.cbReaggeton);
                lista.add(cbReaggeton);
                cbRock=(CheckBox)view.findViewById(R.id.cbRock);
                lista.add(cbRock);

                AdminSQLite bdd = new AdminSQLite(ActivityConfiguracion.this, "administracion", null, 4);
                final SQLiteDatabase db = bdd.getWritableDatabase();
                Cursor cursor= db.rawQuery("SELECT * FROM estilos", null);
                int i=0;
                boolean b;
                cursor.moveToFirst();
                do{
                    if(cursor.getString(1).equals("true"))
                    {
                        b=true;
                    }
                    else
                    {
                        b=false;
                    }
                    lista.get(i).setChecked(b);
                    i++;
                }while (cursor.moveToNext());

                //Guardo los valores que salen del Dialog en la BD
                btnAceptarFiltros= (Button)view.findViewById(R.id.btnAceptarFiltros);

                builder.setView(view);
                final AlertDialog alertDialog = builder.create();

                btnAceptarFiltros.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbAsiatica.isChecked()+"' WHERE estilo = 'Asiatico' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbBSO.isChecked()+"' WHERE estilo = 'BSO' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbClasica.isChecked()+"' WHERE estilo = 'Clasica' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbElect.isChecked()+"' WHERE estilo = 'Electronica' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbMetal.isChecked()+"' WHERE estilo = 'Metal' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbPop.isChecked()+"' WHERE estilo = 'Pop' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbRap.isChecked()+"' WHERE estilo = 'Rap' ");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbReaggeton.isChecked()+"' WHERE estilo = 'Reggaeton'");
                        db.execSQL("UPDATE estilos SET seleccionado = '"+cbRock.isChecked()+"' WHERE estilo = 'Rock' ");
                        alertDialog.dismiss();
                        finish();

                        Cursor cursor2= db.rawQuery("SELECT * FROM estilos", null);
                        cursor2.moveToFirst();
                    }
                });
                alertDialog.show();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConfiguracion.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_report_bug, null);

                btnReporte=(Button)view.findViewById(R.id.btnEnviarBug);
                reportBug=(EditText)view.findViewById(R.id.tvReportBug);


                builder.setView(view);
                final AlertDialog alertDialog = builder.create();

                btnReporte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reporte = reportBug.getText().toString();
                        reportarBug(SingeltonUsuario.nUsuario, reporte);
                        Toast.makeText(ActivityConfiguracion.this, "Reporte enviado correctamente", Toast.LENGTH_SHORT);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pediCancionInetent = new Intent().setClass(
                        ActivityConfiguracion.this, ActivityAbout.class);
                startActivity(pediCancionInetent);
            }
        });
    }

    private void reportarBug(String user, String reporte) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("reporte", reporte);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_REPORT_BUG, params, Api.CODE_POST_REQUEST);
        request.execute();
    }
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == Api.CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == Api.CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
