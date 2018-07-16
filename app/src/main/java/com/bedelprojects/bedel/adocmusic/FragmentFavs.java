package com.bedelprojects.bedel.adocmusic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentFavs extends android.support.v4.app.Fragment
{

    List <Cancion> canciones = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;
    RecyclerView rv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.activity_principal, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rvCanciones);
        swipeContainer=(SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
        rv.setLayoutManager(llm);

        readFavSongs(SingeltonUsuario.nUsuario);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                canciones.clear();
                readFavSongs(SingeltonUsuario.nUsuario);
            }
        });

        return view;
    }
    private void listarCanciones(JSONArray cancion) throws JSONException
    {
        Cancion c;
        for (int i = 0; i < cancion.length(); i++)
        {
            JSONObject obj = cancion.getJSONObject(i);

            c=(new Cancion(
                    obj.getString("nombre"),
                    obj.getString("artista"),
                    obj.getString("letra"),
                    obj.getString("enlace"),
                    obj.getString("idCancion"),
                    obj.getString("estilo")
            ));
            canciones.add(c);

        }

        AdaptadorRecyclerView adapter = new AdaptadorRecyclerView(canciones, new CustomItemClickListener()
        {
            @Override
            public void onItemClick(View v, int position)
            {
                Cancion obj=(Cancion)canciones.get(position);
                Intent inte=new Intent(getContext(), ActivityCancion.class);
                inte.putExtra("objeto", (Serializable)obj);

                startActivity(inte);
            }
        });
        swipeContainer.setRefreshing(false);
        rv.setAdapter(adapter);

    }

    private void readFavSongs(String user)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_USRFA, params, Api.CODE_POST_REQUEST);
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

            Log.d("En OnPost JSON", s);
            super.onPostExecute(s);

            try
            {
                JSONObject object = new JSONObject(s);

                if (!object.getBoolean("error"))
                {
                    listarCanciones(object.getJSONArray("songs"));
                }
            }
            catch (JSONException e)
            {
                Log.e("ERROR JSON",""+e);
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
