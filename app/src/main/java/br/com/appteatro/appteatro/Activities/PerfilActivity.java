package br.com.appteatro.appteatro.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.fragement.PerfilFragment;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            PerfilFragment frag = new PerfilFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }
    }

}
