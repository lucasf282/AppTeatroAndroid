package br.com.appteatro.appteatro.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.EventoFilter;
import br.com.appteatro.appteatro.domain.model.Genero;
import br.com.appteatro.appteatro.utils.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText mNomeEventoField;
    private Spinner spinnerTeatro;
    private Spinner spinnerGenero;

    private Button entrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        // Views
        mNomeEventoField = findViewById(R.id.ed_nome_evento);

        spinnerTeatro = (Spinner) findViewById(R.id.spinner_teatro);
        // Spinner click listener
        spinnerTeatro.setOnItemSelectedListener(this);
        montarDropTeatro();

        spinnerGenero = (Spinner) findViewById(R.id.spinner_genero);
        // Spinner click listener
        spinnerGenero.setOnItemSelectedListener(this);
        montarDropgGenero();

        entrarButton = (Button) findViewById(R.id.buttonFiltrar);
        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarEventosFiltrados();
            }
        });

    }

    private void montarDropTeatro(){
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Item 1");
        categories.add("Item 2");
        categories.add("Item 3");
        categories.add("Item 4");
        categories.add("Item 5");
        categories.add("Item 6");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerTeatro.setAdapter(dataAdapter);
    }

    private void montarDropgGenero(){
        // Creating adapter for spinner
        ArrayAdapter<Genero> dataAdapter = new ArrayAdapter<Genero>(this, android.R.layout.simple_spinner_item, Genero.values());

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerGenero.setAdapter(dataAdapter);
    }

    private void buscarEventosFiltrados(){
        EventoFilter efiltro = new EventoFilter();
        efiltro.setNome(mNomeEventoField.getText().toString().trim());
        efiltro.setGenero((Genero) spinnerGenero.getSelectedItem());
        efiltro.setNomeLocal(spinnerTeatro.getSelectedItem().toString());

        Call<List<Evento>> call;
        if(user != null){
            call = new RetrofitConfig().getEventService().buscarEventosFiltrados(user.getUid(), efiltro.build());
        } else{
            call = new RetrofitConfig().getEventService().buscarEventos();
        }

        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                System.out.println(t.getMessage());
                //Toast.makeText(getActivity(), "Erro ao carregar os eventos.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();
        if(adapterView.getId() == R.id.spinner_teatro){
            // Showing selected spinner item
            Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        } else{
            // Showing selected spinner item
            Toast.makeText(adapterView.getContext(), "Selected Grupo: " + item, Toast.LENGTH_LONG).show();
        }




    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
