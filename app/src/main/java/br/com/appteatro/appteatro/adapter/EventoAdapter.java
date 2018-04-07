package br.com.appteatro.appteatro.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventosViewHolder> {

    private final List<Evento> eventos;
    private static  Context context;
    private EventoOnClickListener eventoOnClickListener;
    private FavoritoOnCheckedChangeListener favoritoOnCheckedChangeListener;

    public EventoAdapter(Context context, List<Evento> eventos, EventoOnClickListener eventoOnClickListener, FavoritoOnCheckedChangeListener favoritoOnCheckedChangeListener) {
        this.context = context;
        this.eventos = eventos;
        this.eventoOnClickListener = eventoOnClickListener;
        this.favoritoOnCheckedChangeListener = favoritoOnCheckedChangeListener;
    }

    @Override
    public int getItemCount() {
        return this.eventos != null ? this.eventos.size() : 0;
    }

    @Override
    public EventosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_evento, viewGroup, false);
        EventosViewHolder holder = new EventosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EventosViewHolder holder, final int position) {
        // Atualiza a view
        Evento e = eventos.get(position);
        holder.tNome.setText(e.nome);
        Glide.with(this.context).load(e.imagem).into(holder.img);
        holder.tGenero.setText(e.genero);
        if(e.favoritado != null && e.favoritado) {
            holder.toggleButton.setChecked(e.favoritado);
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_24dp));
        }

        if (eventoOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // A variável position é final
                    eventoOnClickListener.onClickEvento(holder.itemView, position);
                }
            });
        }

        if(favoritoOnCheckedChangeListener != null){
            holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.ic_favorite_black_24dp));
                        favoritoOnCheckedChangeListener.onClickFavorito(holder.itemView, position, isChecked);
                    }
                    else {
                        holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));
                        favoritoOnCheckedChangeListener.onClickFavorito(holder.itemView, position, isChecked);
                    }
                }
            });
        }
    }

    public interface EventoOnClickListener {
        public void onClickEvento(View view, int idx);
    }

    public interface FavoritoOnCheckedChangeListener{
        public void onClickFavorito(View view, int idx, boolean isChecked);
    }

    // ViewHolder com as views
    public static class EventosViewHolder extends RecyclerView.ViewHolder{
        public TextView tNome;
        public ImageView img;
        public TextView tGenero;
        public ToggleButton toggleButton;

        public EventosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img);
            tGenero = (TextView) view.findViewById(R.id.genero);

            toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
            toggleButton.setChecked(false);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));
        }
    }
}
