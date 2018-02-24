package br.com.appteatro.appteatro.fragement;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.adapter.EventoAdapter;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.service.EventoService;
import br.com.appteatro.appteatro.utils.AndroidUtils;

public class EventoFragment extends Fragment {

    protected RecyclerView recyclerView;
    private int tipo;
    private List<Evento> listaString;
    private SwipeRefreshLayout swipeLayout;

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

        // Registra a classe para receber eventos.
        //CarrosApplication.getInstance().getBus().register(this);
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


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskCarros(false);
    }

    private void taskCarros(boolean pullToRefresh) {
        String nome0 = "Thiago";
        String nome1 = "Diogo";
        String nome2 = "Sônia";

        this.listaString = new ArrayList<>();

        // Salva a lista de carros no atributo da classe
//        EventoFragment.this.listaString.add(nome0);
//        EventoFragment.this.listaString.add(nome1);
//        EventoFragment.this.listaString.add(nome2);

        this.listaString = EventoService.getEventos(this.tipo);

        // Atualiza a view na UI Thread
        recyclerView.setAdapter(new EventoAdapter(getContext(), this.listaString));
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    // Atualiza ao fazer o gesto Pull to Refresh
                    taskCarros(true);
                    swipeLayout.setRefreshing(false);
                } else {
                    swipeLayout.setRefreshing(false);
                    Toast.makeText(getActivity(),  R.string.msg_error_conexao_indisponivel, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }



}
