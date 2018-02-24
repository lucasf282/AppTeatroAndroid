package br.com.appteatro.appteatro;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView imgCapa;
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

        imgCapa = (ImageView) findViewById(R.id.imgView_capaLocalDesc);
        txtView_nome = (TextView) findViewById(R.id.txtView_nome);
        txtView_endereco = (TextView) findViewById(R.id.txtView_endereco);
        txtView_complemento = (TextView) findViewById(R.id.txtView_complemento);
        txtView_cidade = (TextView) findViewById(R.id.txtView_cidade);
        txtView_telefone = (TextView) findViewById(R.id.txtView_telefone);
        mapViewLocal = (MapView) findViewById(R.id.mapView_local);
        
        mapViewLocal.getMapAsync(this);
        mapViewLocal.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-15.794020, -47.882611))
                .title("Marker"));

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
