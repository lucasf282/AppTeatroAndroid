package br.com.appteatro.appteatro.Activities;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.Local;

public class LocalActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected Local local;
    private ImageView img_capa;
    private TextView txtView_nome;
    private TextView txtView_endereco;
    private TextView txtView_complemento;
    private TextView txtView_cidade;
    private TextView txtView_telefone;
    private MapView mapViewLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        img_capa = (ImageView) findViewById(R.id.imgView_capaLocalDesc);
        txtView_nome = (TextView) findViewById(R.id.txtView_nome);
        txtView_endereco = (TextView) findViewById(R.id.txtView_endereco);
        txtView_complemento = (TextView) findViewById(R.id.txtView_complemento);
        txtView_cidade = (TextView) findViewById(R.id.txtView_cidade);
        txtView_telefone = (TextView) findViewById(R.id.txtView_telefone);
        mapViewLocal = (MapView) findViewById(R.id.mapView_local);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            local = (Local) bundle.get("local");

            //TODO: setar imagem do local
            //Glide.with(this).load(local.imagem).into(img_capa);
            txtView_nome.setText(local.nome);
            txtView_endereco.setText(local.endereco);
            txtView_complemento.setText(local.complemento);
            txtView_cidade.setText(local.cidade);
            txtView_telefone.setText(local.telefone);
        }

        mapViewLocal.getMapAsync(this);
        mapViewLocal.onCreate(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (local != null){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(local.latitude),
                            Double.parseDouble(local.longitude)))
                    .title(local.nome));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(local.latitude),
                            Double.parseDouble(local.longitude)), 14));
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        mapViewLocal.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapViewLocal.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewLocal.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapViewLocal.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewLocal.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapViewLocal.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewLocal.onLowMemory();
    }
}
