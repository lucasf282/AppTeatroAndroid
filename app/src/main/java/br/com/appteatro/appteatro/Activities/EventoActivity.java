package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;

public class EventoActivity extends AppCompatActivity {

    Evento evento;
    private ConstraintLayout linhaLocal;
    private ImageView img_capa;
    private TextView txt_data;
    private TextView txt_local;
    private TextView txt_preco;
    private TextView txt_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        img_capa = (ImageView) findViewById(R.id.imgView_capaDesc);
        txt_data = (TextView) findViewById(R.id.txtView_data);
        txt_local = (TextView) findViewById(R.id.txtView_local);
        txt_preco = (TextView) findViewById(R.id.txtView_preco);
        txt_desc = (TextView) findViewById(R.id.txtView_desc);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            evento = (Evento) bundle.get("evento");

            //TODO: setar titulo, imagem, data e preco do evento
            Glide.with(this).load(evento.imagem).into(img_capa);
            txt_local.setText(evento.local.nome);
            txt_desc.setText(evento.descricao);
        }

        linhaLocal = (ConstraintLayout) findViewById(R.id.cl_local);

        linhaLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventoActivity.this, LocalActivity.class);
                intent.putExtra("local", evento.local);
                startActivity(intent);
            }
        });
    }
}
