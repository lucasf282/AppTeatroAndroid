package br.com.appteatro.appteatro.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Local;

/**
 * Created by Windows 10 on 19/08/2018.
 */

public class TeatroAdapter extends RecyclerView.Adapter<TeatroAdapter.TeatrosViewHolder> {

    protected static final String TAG = "livroandroid";
    private final List<Local> locais;
    private final Context context;
    private TeatroOnClickListener teatroOnClickListener;

    public TeatroAdapter(Context context, List<Local> locais, TeatroOnClickListener
            teatroOnClickListener) {
        this.context = context;
        this.locais = locais;
        this.teatroOnClickListener = teatroOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.locais != null ? this.locais.size() : 0;
    }

    @Override
    public TeatrosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_teatro, viewGroup, false);
        TeatrosViewHolder holder = new TeatrosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TeatrosViewHolder holder, final int position) {
        // Atualiza a view
        Local c = locais.get(position);
        holder.tNome.setText(c.nome);
        holder.progress.setVisibility(View.VISIBLE);
        // Faz o download da foto e mostra o ProgressBar
        Glide.with(this.context).load(c.imagem).listener(new RequestListener<String, GlideDrawable>() {
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
        }).into(holder.img);
        // Click
        if (teatroOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // A variável position é final
                    teatroOnClickListener.onClickTeatro(holder.itemView, position);
                }
            });
        }
    }

    public interface TeatroOnClickListener {
        void onClickTeatro(View view, int idx);
    }

    // ViewHolder com as views
    public static class TeatrosViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        ImageView img;
        ProgressBar progress;
        CardView cardView;

        public TeatrosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
