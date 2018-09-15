package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.EventoFilter;
import br.com.appteatro.appteatro.fragement.EventoFragment;

public class EventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        // setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Título
        getSupportActionBar().setTitle("Resultado");
        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            EventoFragment frag = new EventoFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }

        EventoFilter e = (EventoFilter) getIntent().getExtras().get("filtro");

        setTextString(R.id.textoFiltro, "FILTRO: " + concatFiltro(e));
        ImageButton buttonCancelar = (ImageButton) findViewById(R.id.button2);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventosActivity.this, MenuLateralActivity.class));
                finish();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }

    private String concatFiltro(EventoFilter e){
        String x = "";
        if(e.getNome() != null && !e.getNome().isEmpty()){
            x = e.getNome() + "/";
        }
        x = x + e.getNomeLocal() + "/" + e.getGenero().getDescricao();

        return x;
    }

    protected void setTextString(int resId, String text) {
        TextView t = (TextView) findViewById(resId);
        if (t != null) {
            t.setText(text);
        }
    }
}
