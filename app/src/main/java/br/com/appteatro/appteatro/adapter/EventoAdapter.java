package br.com.appteatro.appteatro.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventosViewHolder> {

    private final List<Evento> eventos;
    private static  Context context;
    private EventoOnClickListener eventoOnClickListener;
    private FavoritoOnCheckedChangeListener favoritoOnCheckedChangeListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_events_row, viewGroup, false);
        EventosViewHolder holder = new EventosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EventosViewHolder holder, final int position) {
        // Atualiza a view
        final Evento e = eventos.get(position);
        holder.progress.setVisibility(View.VISIBLE);
        Glide.with(this.context).load(e.imagem).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                holder.progress.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.progress.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.img_thumb);
        holder.txt_nome.setText(e.nome);
        System.out.println(e.listaAgenda.get(0).getData());
        holder.txt_data.setText(converteData(e.listaAgenda.get(0).getData()));
        holder.txt_local.setText(e.getLocal().getNome());
        holder.txt_preco.setText(e.listaAgenda.get(0).getListaIngresso().get(0).getPreco());

        if(user != null) {
            if (e.favoritado.equals(Boolean.TRUE)) {
                e.setFavoritado(true);
                holder.toggleButton.setChecked(e.favoritado);
                holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_24dp));
            } else {
                e.setFavoritado(false);
                holder.toggleButton.setChecked(e.favoritado);
                holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));
            }
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
                    if(user != null) {
                        if (isChecked) {
                            e.setFavoritado(Boolean.TRUE);
                            holder.toggleButton.setChecked(e.favoritado);
                            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_24dp));
                            favoritoOnCheckedChangeListener.onClickFavorito(holder.itemView, position, isChecked);
                        } else {
                            e.setFavoritado(Boolean.FALSE);
                            holder.toggleButton.setChecked(e.favoritado);
                            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));
                            favoritoOnCheckedChangeListener.onClickFavorito(holder.itemView, position, isChecked);
                        }
                    } else{
                        favoritoOnCheckedChangeListener.onClickFavorito(holder.itemView, position, isChecked);
                    }
                }
            });
        }
    }

    private String converteData(Date data){
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

        return dt.format(data);
    }

    public interface EventoOnClickListener {
        public void onClickEvento(View view, int idx);
    }

    public interface FavoritoOnCheckedChangeListener{
        public void onClickFavorito(View view, int idx, boolean isChecked);
    }

    // ViewHolder com as views
    public static class EventosViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_nome;
        public TextView txt_data;
        public TextView txt_local;
        public TextView txt_preco;
        public ImageView img_thumb;
        public ToggleButton toggleButton;
        public ProgressBar progress;

        public EventosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            img_thumb = (ImageView) view.findViewById(R.id.img_thumb);
            txt_nome = (TextView) view.findViewById(R.id.txt_event_name);
            txt_data = (TextView) view.findViewById(R.id.txt_event_data);
            txt_local = (TextView) view.findViewById(R.id.txt_event_place);
            txt_preco = (TextView) view.findViewById(R.id.txt_event_price);

            toggleButton = (ToggleButton) view.findViewById(R.id.tglBtn_favorito);
            toggleButton.setChecked(Boolean.FALSE);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_black_24dp));

            progress = (ProgressBar) view.findViewById(R.id.progressImg);
        }
    }
}
