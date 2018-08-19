package br.com.appteatro.appteatro.fragement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.appteatro.appteatro.Activities.EventoActivity;
import br.com.appteatro.appteatro.Activities.LocalActivity;
import br.com.appteatro.appteatro.AppTeatroApplication;
import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.adapter.EventoAdapter;
import br.com.appteatro.appteatro.adapter.TeatroAdapter;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.Local;
import br.com.appteatro.appteatro.utils.AndroidUtils;
import br.com.appteatro.appteatro.utils.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeatrosFragment extends Fragment {

    protected RecyclerView recyclerView;
    private int tipo;
    private List<Local> locais;
    private SwipeRefreshLayout swipeLayout;
    private ActionMode actionMode;
    private Intent shareIntent;

    // Método para instanciar esse fragment pelo tipo.
    public static TeatrosFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        TeatrosFragment f = new TeatrosFragment();
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
//        AppTeatroApplication.getInstance().getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Cancela o recebimento de eventos.
        //CarrosApplication.getInstance().getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teatros, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
// Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    // Atualiza ao fazer o gesto Pull to Refresh
                    taskTeatros(true);
                } else {
                    swipeLayout.setRefreshing(false);
                    //snack(recyclerView, R.string.msg_error_conexao_indisponivel);
                }
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskTeatros(false);
    }

    private void taskTeatros(boolean pullToRefresh) {
        // Busca os carros: Dispara a Task
        //startTask("carros", new GetCarrosTask(pullToRefresh), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
        this.obterListaLocais();
    }

    private void obterListaLocais(){
        Call<List<Local>> call = new RetrofitConfig().getLocalService().buscarLocais();

        call.enqueue(new Callback<List<Local>>() {
            @Override
            public void onResponse(Call<List<Local>> call, Response<List<Local>> response) {
                System.out.println(response);
                locais = response.body();
                atualizaTabelaLocais(locais);
            }

            @Override
            public void onFailure(Call<List<Local>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao carregar os eventos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizaTabelaLocais(List<Local> locais) {
        TeatrosFragment.this.locais = locais;
        recyclerView.setAdapter(new TeatroAdapter(getContext(), TeatrosFragment.this.locais, onClickTeatro()));
    }

    private TeatroAdapter.TeatroOnClickListener onClickTeatro(){
        return new TeatroAdapter.TeatroOnClickListener() {
            @Override
            public void onClickTeatro(View view, int idx) {
                Local local = locais.get(idx);
                Intent intent = new Intent(getContext(), LocalActivity.class);
                intent.putExtra("local", local);
                startActivity(intent);
            }
        };
    }



}
