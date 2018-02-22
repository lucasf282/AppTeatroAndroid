package br.com.appteatro.appteatro;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EventoActivity extends AppCompatActivity {

    private ConstraintLayout linhaData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        linhaData = (ConstraintLayout) findViewById(R.id.cl_data);
        linhaData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext() , "Clicou", Toast.LENGTH_SHORT).show();
            }
        });
        ((ConstraintLayout) findViewById(R.id.cl_local)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventoActivity.this, LocalActivity.class);
                startActivity(intent);
            }
        });
    }


}
