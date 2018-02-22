package br.com.appteatro.appteatro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

public class LocalActivity extends AppCompatActivity {

    ImageView imgCapa;
    ListView descLocal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
    }
}
