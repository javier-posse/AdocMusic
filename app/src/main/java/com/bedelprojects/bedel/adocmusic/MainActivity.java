package com.bedelprojects.bedel.adocmusic;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{


    //private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    AudioManager audioManager;

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawer;
    ImageView imagen;
    ListView mDrawerListView;
    TextView tvNombre;
    ListView listaDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tvNombre=(TextView) findViewById(R.id.nombreUsuario);
        listaDrawer= (ListView) findViewById(R.id.left_drawer);

        tvNombre.setText(SingeltonUsuario.nUsuario);

        audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.app_name, R.string.app_name)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        imagen=(ImageView)findViewById(R.id.imgdrawer);
        imagen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });


        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        //Abro el activity de Pedir nueva canción
                        Intent pediCancionInetent = new Intent().setClass(
                                MainActivity.this, ActivityPedirCancion.class);
                        startActivity(pediCancionInetent);
                        break;

                    case 1:
                        //Abro el actovoty de Peticiones aceptadas
                        Intent petAceIntent = new Intent().setClass(
                                MainActivity.this, ActivityPeticionesAceptadas.class);
                        startActivity(petAceIntent);
                        break;

                    case 2:
                        //Abro el activity de la configuracion
                        Intent configIntent = new Intent().setClass(
                                MainActivity.this, ActivityConfiguracion.class);
                        startActivity(configIntent);
                        break;
                    case 3:
                        //Abro el activity de novedades
                        Intent NovedadesIntent = new Intent().setClass(
                                MainActivity.this, ActivityNovedades.class);
                        startActivity(NovedadesIntent);
                        break;
                    case 4:
                        AdminSQLite bdd = new AdminSQLite(getApplicationContext(), "administracion", null, 4);
                        SQLiteDatabase db = bdd.getWritableDatabase();
                        //Borro la sesión del la BD
                        db.execSQL("DELETE FROM usuario");

                        //Borro el Singelton del usuario
                        SingeltonUsuario.setNull();

                        //Devuelvo al login
                        Intent loginIntent = new Intent().setClass(
                                MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        break;
                }

                mDrawer.closeDrawer(GravityCompat.START);
            }
        });


        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)==0)
        {
            Toast.makeText(getApplicationContext(), "Suba el volumen, por favor", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupViewPager (ViewPager viewPager)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentRecomend(), "Aleatorias");
        adapter.addFragment(new FragmentFavs(), "Favoritas");
        viewPager.setAdapter(adapter);
    }

}
