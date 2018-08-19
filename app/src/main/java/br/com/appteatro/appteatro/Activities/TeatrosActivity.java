package br.com.appteatro.appteatro.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.fragement.TeatrosFragment;


public class TeatrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teatros);

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            TeatrosFragment frag = new TeatrosFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }
    }
}
