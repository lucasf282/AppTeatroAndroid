package br.com.appteatro.appteatro.Activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.fragement.InformacaoFragment;

public class InformacaoActivity extends AppCompatActivity {

    private Evento evento;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao);

        progressBar = (ProgressBar) findViewById(R.id.progressImg);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        this.carregarEvento();
        this.carregaItensTela();

        getSupportActionBar().setTitle(evento.getNome());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Adiciona o fragment no layout
        if (savedInstanceState == null) {
            // Cria o fragment com o mesmo Bundle (args) da intent
            InformacaoFragment frag = new InformacaoFragment();
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout
            getSupportFragmentManager().beginTransaction().add(R.id.InformacaoFragment, frag).commit();
        }

    }

    private void carregarEvento(){
        Bundle bundle = getIntent().getExtras();
        evento = (Evento) bundle.get("evento");
    }

    private void carregaItensTela() {
        // Imagem de header na action bar
        ImageView appBarImg = (ImageView) findViewById(R.id.appBarImg);

        // Faz o download da foto e mostra o ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(evento.imagem).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(appBarImg);
    }

    public void setTitle(String s) {
        // O título deve ser setado na CollapsingToolbarLayout
        CollapsingToolbarLayout c = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        c.setTitle(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
