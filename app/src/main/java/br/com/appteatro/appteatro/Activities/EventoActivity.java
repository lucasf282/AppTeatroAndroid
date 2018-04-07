package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.CalendarContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;

public class EventoActivity extends AppCompatActivity {

    private final String TAG = "EventoActivity";

    Evento evento;
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

        if (savedInstanceState != null){
            this.evento = (Evento) savedInstanceState.getSerializable("evento");
            Log.d(TAG, "Event Read" + this.evento.toString());
        }else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                evento = (Evento) bundle.get("evento");

                //TODO: setar titulo, imagem, data e preco do evento
                Glide.with(this).load(evento.imagem).into(img_capa);
                txt_local.setText(evento.local.nome);
                txt_desc.setText(evento.descricao);
            }
        }
        ConstraintLayout linhaLocal = (ConstraintLayout) findViewById(R.id.cl_local);
        linhaLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventoActivity.this, LocalActivity.class);
                intent.putExtra("local", evento.local);
                startActivity(intent);
            }
        });

        //TODO: implementar suporte para versões anterirores
        ConstraintLayout linhaAgenda = (ConstraintLayout) findViewById(R.id.cl_data);
        linhaAgenda.setOnClickListener(new View.OnClickListener() {

            //TODO: criar suporte para APIs antigas.
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(2018, 5, 3, 19, 30);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(2018, 5, 3, 22, 30);
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                            .putExtra(CalendarContract.Events.TITLE, evento.getNome())
                            .putExtra(CalendarContract.Events.DESCRIPTION, evento.getDescricao())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, evento.getLocal().getNome())
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                    //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                    startActivity(intent);
                }else{
                    Toast.makeText(EventoActivity.this, "Esta ação não esta disponivel para esta versão.", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Paused");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroyed");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("evento", evento);
        Log.d(TAG, "Event Saved");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("evento", evento);
        Log.d(TAG, "Event Saved (persistent)");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            this.evento = (Evento) savedInstanceState.getSerializable("evento");
        }
    }
}
