package com.bedelprojects.bedel.adocmusic;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class AdaptadorRecyclerView extends RecyclerView.Adapter<AdaptadorRecyclerView.ViewHolder>
{

    private  List <Cancion> canciones;
    CustomItemClickListener listener;

    public AdaptadorRecyclerView( List <Cancion> canciones, CustomItemClickListener listener)
    {
        this.canciones = canciones;
        this.listener = listener;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        CardView cv;
        public TextView nombreCancion;
        public TextView artistaCancion;
        final YouTubeThumbnailView thumb;
        ConstraintLayout constraintLayout;



        public ViewHolder(View v) {
            super(v);
            cv= (CardView)itemView.findViewById((R.id.cvCanciones));
            nombreCancion = (TextView)itemView.findViewById(R.id.nombreCancion);
            artistaCancion=(TextView)itemView.findViewById(R.id.artistaCancion);
            thumb = (YouTubeThumbnailView)itemView.findViewById(R.id.yt_thumbnail);
            constraintLayout=(ConstraintLayout)itemView.findViewById(R.id.layoutCartita);
        }
    }



    @Override
    public AdaptadorRecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewprincipal_layout, viewGroup, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                listener.onItemClick(view, vh.getPosition());
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {

        holder.nombreCancion.setText(canciones.get(i).getNombre());

        holder.artistaCancion.setText(canciones.get(i).getArtista());

        switch (canciones.get(i).getEstilo())
        {
            case "Asiático": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_animu);
                break;
            case "BSO": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_bso);
                break;
            case "Pop": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_pop);
                break;
            case "Rock": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_rock);
                break;
            case "Rap": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_rap);
                break;
            case "Metal": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_metal);
                break;
            case "Clásica": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_clasica);
                break;
            case "Electrónica": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_electronica);
                break;
            case "Reggaeton": holder.constraintLayout.setBackgroundResource(R.drawable.gradient_regeloquesea);
                break;
        }

        final int n=i;

        holder.thumb.initialize("AIzaSyAXbO-CZtxJSaTgzQuSMgivONmty3J3CoE", new YouTubeThumbnailView.OnInitializedListener()
        {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader)
            {
                youTubeThumbnailLoader.setVideo(canciones.get(n).getUrl());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener()
                {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s)
                    {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason)
                    {

                    }
                });

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult)
            {

            }

        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount()
    {
        return canciones.size();
    }


}