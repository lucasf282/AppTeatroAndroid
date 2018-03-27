package br.com.appteatro.appteatro.fragement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.appteatro.appteatro.Activities.EventoActivity;
import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.adapter.EventoAdapter;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.service.EventoService;
import br.com.appteatro.appteatro.utils.AndroidUtils;
import br.com.appteatro.appteatro.utils.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoFragment extends Fragment {

    public List<Evento> listaEvents = new ArrayList<>();
    protected RecyclerView recyclerView;
    private int tipo;
    private List<Evento> listaEventos;
    private SwipeRefreshLayout swipeLayout;
    private ProgressBar progressBar;

    // Método para instanciar esse fragment pelo tipo.
    public static EventoFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        EventoFragment f = new EventoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Lê o tipo dos argumentos.
            this.tipo = getArguments().getInt("tipo");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evento, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_frag_eventos);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskEventos(false);
    }

    private void taskEventos(boolean pullToRefresh) {
        //this.listaEventos = new ArrayList<>();
        //this.listaEventos = EventoService.getEventos(this.tipo);

        // Atualiza a view na UI Thread
        //recyclerView.setAdapter(new EventoAdapter(getContext(), this.listaEventos, onClickEvento()));
        //new GetEventosTask(this.tipo).execute();
        obterListaEventos();
    }

    private EventoAdapter.EventoOnClickListener onClickEvento(){
        return new EventoAdapter.EventoOnClickListener() {
            @Override
            public void onClickCarro(View view, int idx) {
                Evento evento = listaEventos.get(idx);
                Intent intent = new Intent(getContext(), EventoActivity.class);
                intent.putExtra("evento", evento);
                startActivity(intent);
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    // Atualiza ao fazer o gesto Pull to Refresh
                    taskEventos(true);
                    swipeLayout.setRefreshing(false);
                } else {
                    swipeLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), R.string.msg_error_conexao_indisponivel, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void obterListaEventos(){
        Call<List<Evento>> call = new RetrofitConfig().getEventService().buscarEventos();
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                System.out.println(response);
                listaEvents = response.body();
                atualizaTabelaEventos(listaEvents);
//                    CEP cep = response.body();
//                    resposta.setText(cep.toString());
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Log.e("CEPService   ", "Erro ao buscar o cep:" + t.getMessage());
            }
        });
    }

    private void atualizaTabelaEventos(List<Evento> eventos) {
        if (!eventos.isEmpty()) {
            EventoFragment.this.listaEventos = eventos;
            EventoFragment.this.progressBar.setVisibility(View.GONE);
            EventoFragment.this.swipeLayout.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(new EventoAdapter(getContext(), EventoFragment.this.listaEventos, onClickEvento()));
        }
    }

}
