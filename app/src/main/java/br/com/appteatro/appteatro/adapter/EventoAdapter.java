package br.com.appteatro.appteatro.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.appteatro.appteatro.EventoActivity;
import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventosViewHolder> {

    private final List<Evento> eventos;
    private final Context context;

    public EventoAdapter(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventoActivity.class);
                intent.putExtra("evento", eventos.get(position));
                context.startActivity(intent);
            }
        });
    }


    // ViewHolder com as views
    public static class EventosViewHolder extends RecyclerView.ViewHolder{
        public TextView tNome;
        public ImageView img;
        public TextView tGenero;

        public EventosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img);
            tGenero = (TextView) view.findViewById(R.id.genero);
        }
    }
}
