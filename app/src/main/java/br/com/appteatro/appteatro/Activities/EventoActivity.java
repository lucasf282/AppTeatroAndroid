package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CalendarContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.Favorito;
import br.com.appteatro.appteatro.utils.RetrofitConfig;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoActivity extends AppCompatActivity {

    private final String TAG = "EventoActivity";

    Evento evento;
    private ImageView img_capa;
    private TextView txt_data;
    private TextView txt_hora;
    private TextView txt_local;
    private TextView txt_preco;
    private TextView txt_desc;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        img_capa = (ImageView) findViewById(R.id.imgView_capaDesc);
        txt_data = (TextView) findViewById(R.id.txtView_data);
        txt_hora = (TextView) findViewById(R.id.txtView_hora);
        txt_local = (TextView) findViewById(R.id.txtView_local);
        txt_preco = (TextView) findViewById(R.id.txtView_preco);
        txt_desc = (TextView) findViewById(R.id.txtView_desc);
        progressBar = (ProgressBar) findViewById(R.id.progressImg);
        toggleButton = (ToggleButton) findViewById(R.id.tglBtn_share);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (user != null) {
                    Favorito favarito = new Favorito();
                    favarito.setUid(user.getUid());
                    favarito.setEvento(evento);
                    if (isChecked && evento.getFavoritado().equals(Boolean.FALSE)) {
                        evento.setFavoritado(true);
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_black_24dp));
                        salvarFavorito(favarito);
                    } else {
                        evento.setFavoritado(false);
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_border_black_24dp));
                        deletarFavorito(favarito);
                    }
                } else {
                    //IMPLEMNTAR MODAL
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        if (savedInstanceState != null) {
            this.evento = (Evento) savedInstanceState.getSerializable("evento");
            Log.d(TAG, "Event Read" + this.evento.toString());
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                evento = (Evento) bundle.get("evento");

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
                }).into(img_capa);
                txt_data.setText(converteData(evento.listaAgenda.get(0).getData()));
                txt_hora.setText(evento.listaAgenda.get(0).getHorario());
                txt_local.setText(evento.getLocal().getNome());
                txt_preco.setText(evento.listaAgenda.get(0).getListaIngresso().get(0).getPreco());
                txt_desc.setText(evento.getDescricao());
                if (user != null) {
                    if (evento.favoritado.equals(Boolean.TRUE)) {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_black_24dp));
                    } else {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_border_black_24dp));
                    }
                }
                setTitle(evento.getNome());
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(2018, 3, 25, 19, 30);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(2018, 3, 25, 22, 30);
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
                } else {
                    Toast.makeText(EventoActivity.this, "Esta ação não esta disponivel para esta versão.", Toast.LENGTH_SHORT);
                }
            }
        });

        ConstraintLayout linhaInformacao = (ConstraintLayout) findViewById(R.id.cl_desc);
        linhaInformacao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventoActivity.this, InformacaoActivity.class);
                intent.putExtra("evento", evento);
                startActivity(intent);
            }
        });

    }

    private String converteData(Date data) {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

        return dt.format(data);
    }

    private void salvarFavorito(Favorito favarito) {
        Call<Favorito> call = new RetrofitConfig().getFavoriteService().inserirFavorito(favarito);
        call.enqueue(new Callback<Favorito>() {
            @Override
            public void onResponse(Call<Favorito> call, Response<Favorito> response) {

            }

            @Override
            public void onFailure(Call<Favorito> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Erro ao favoritar o evento.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletarFavorito(Favorito favarito) {
        Call<ResponseBody> call = new RetrofitConfig().getFavoriteService().deletarFavorito(favarito);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Erro ao deletar o evento.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareContent() {

        Bitmap bitmap = getBitmapFromView(img_capa);
        try {
            File file = new File(this.getExternalCacheDir(), "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, evento.getNome() + " - " + evento.getLocal().getNome()
                    + " (" + converteData(evento.listaAgenda.get(0).getData()) + " às " + evento.listaAgenda.get(0).getHorario() + ")");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Compartilhar evento..."));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
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
        if (savedInstanceState != null) {
            this.evento = (Evento) savedInstanceState.getSerializable("evento");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareContent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
