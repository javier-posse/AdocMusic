package com.bedelprojects.bedel.adocmusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityCancion extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String DEVELOPER_KEY = "AIzaSyCxuniXMEckycDrSrOEcTdtVyNS7niaDx0";//Esta es la clave que te da google en el panel de desarrollador
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    TextView tvLetra;
    String VIDEO_ID;
    ImageView fav, report;
    Cancion c;
    boolean clikado = false;
    boolean corazon = false;
    private YouTubePlayerView youTubeView;
    SharedPreferences prefe;
    LinearLayout fondo;
    Button  enviarReport;
    Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion2);

        tvLetra = (TextView) findViewById(R.id.tvLetra);
        fav = (ImageView) findViewById(R.id.corasao);
        c = (Cancion) getIntent().getExtras().getSerializable("objeto");
        fondo = (LinearLayout)findViewById(R.id.fondoCancion);
        report= (ImageView)findViewById(R.id.banderita);

        comprobarFav(SingeltonUsuario.nUsuario, c.getId());

        switch (c.getEstilo())
        {
            case "Asiático": fondo.setBackgroundColor(Color.parseColor("#9090ee"));
                break;
            case "BSO": fondo.setBackgroundColor(Color.parseColor("#90EE90"));
                break;
            case "Pop": fondo.setBackgroundColor(Color.parseColor("#EE82EE"));
                break;
            case "Rock": fondo.setBackgroundColor(Color.parseColor("#8B0000"));
                break;
            case "Rap": fondo.setBackgroundColor(Color.parseColor("#FFD700"));
                break;
            case "Metal": fondo.setBackgroundColor(Color.parseColor("#000000"));
                break;
            case "Clásica": fondo.setBackgroundColor(Color.parseColor("#8B4513"));
                break;
            case "Electrónica":fondo.setBackgroundColor(Color.parseColor("#000099"));
                break;
            case "Reggaeton": fondo.setBackgroundColor(Color.parseColor("#FF9900"));
                break;
        }

        if (!c.getLetra().equals("null"))
        {
            tvLetra.setText(c.getLetra());
        }
        else
        {
            tvLetra.setText("Esta canción no tiene letra");
        }
        VIDEO_ID = c.getUrl();
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        youTubeView.initialize(DEVELOPER_KEY, this);

        prefe = getSharedPreferences("Ajustes", Context.MODE_PRIVATE);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!corazon && !clikado) {
                    clikado = true;
                    registrarFav(SingeltonUsuario.nUsuario, c.getId());
                } else {
                    eliminarFav(SingeltonUsuario.nUsuario, c.getId());
                }

            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCancion.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_report_canciones, null);
                enviarReport=(Button)view.findViewById(R.id.btnEnviarReporteCancion);
                spin=(Spinner)view.findViewById(R.id.spinner);

                String[] opciones ={"Letra equivocada", "Vídeo equivocado", "Artista equivocado", "Nombre equivado", "Estilo equivocado"};
                spin.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, opciones));

                builder.setView(view);
                final AlertDialog alertDialog = builder.create();

                enviarReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reportarCancion(SingeltonUsuario.nUsuario, c.getId(), spin.getSelectedItem().toString());
                        Toast.makeText(ActivityCancion.this, "Reporte enviado correctamente", Toast.LENGTH_SHORT);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }

    protected YouTubePlayerView getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            if (prefe.getBoolean("reproduccion_automatica", true) == true) {
                youTubePlayer.loadVideo(VIDEO_ID);
            } else {
                youTubePlayer.cueVideo(VIDEO_ID);
            }
            //Si se usa el load, el video se carga automaticamente al abrirse la view.
            //Si se usa el cue, el video no se cargará automaticamente.
        }
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarFav(String user, String idCancion) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("idCancion", idCancion);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_REGIST_FAV, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    private void eliminarFav(String user, String idCancion) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("idCancion", idCancion);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_FAV, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    private void comprobarFav(String user, String idCancion) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("idCancion", idCancion);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_SEARCH_FAV, params, Api.CODE_POST_REQUEST);
        request.execute();
    }
    private void reportarCancion(String user, String idCancion, String reporte) {

        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("idCancion", idCancion);
        params.put("reporte", reporte);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_REPORT_SON, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    private void llenarCorazon() {
        fav.setImageResource(R.drawable.corazonpetao);
        corazon = true;
        clikado = false;
    }

    private void vaciarCorazon() {
        fav.setImageResource(R.drawable.corazonvacio);
        corazon = false;
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
                    if (object.has("find") && object.getBoolean("find")) {
                        llenarCorazon();
                    } else if (object.has("deleted") && object.getBoolean("deleted")) {
                        vaciarCorazon();
                    } else if (object.has("inserted") && object.getBoolean("inserted")) {
                        llenarCorazon();
                    }
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
