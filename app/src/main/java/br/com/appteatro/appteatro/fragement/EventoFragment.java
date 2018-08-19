package br.com.appteatro.appteatro.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import br.com.appteatro.appteatro.Activities.EventoActivity;
import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.adapter.EventoAdapter;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.Favorito;
import br.com.appteatro.appteatro.domain.model.Usuario;
import br.com.appteatro.appteatro.fragement.dialog.AboutDialog;
import br.com.appteatro.appteatro.utils.AndroidUtils;
import br.com.appteatro.appteatro.utils.RetrofitConfig;
import okhttp3.ResponseBody;
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
    private TextView emptyView;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        emptyView = (TextView) view.findViewById(R.id.empty_view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //taskEventos(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            //do something  //Load or Refresh Data
            taskEventos(true);
        }
    }

    private void taskEventos(boolean pullToRefresh) {
        if (this.tipo == 0) {
            obterListaEventos();
        } else if (this.tipo == 1) {
            obterListaEventos();
        } else {
            obterListaEventosFavorito();
        }
    }

    private EventoAdapter.EventoOnClickListener onClickEvento(){
        return new EventoAdapter.EventoOnClickListener() {
            @Override
            public void onClickEvento(View view, int idx) {
                Evento evento = listaEventos.get(idx);
                Intent intent = new Intent(getContext(), EventoActivity.class);
                intent.putExtra("evento", evento);
                startActivity(intent);
            }
        };
    }

    private EventoAdapter.FavoritoOnCheckedChangeListener onCheckedChangeListener(){
        return new EventoAdapter.FavoritoOnCheckedChangeListener() {
            @Override
            public void onClickFavorito(View view, int idx, boolean isChecked) {
                if(user != null) {
                    Favorito favarito = new Favorito();
                    favarito.setUid(user.getUid());
                    favarito.setEvento(listaEventos.get(idx));
                    if (isChecked) {
                        salvarFavorito(favarito);
                    } else {
                        // TODO Implementar serviço para remover Favorito
                        deletarFavorito(favarito);
                    }
                } else{
                    //IMPLEMNTAR MODAL
                    AboutDialog.showAbout(getFragmentManager());
                }

            }
        };
    }

    private void salvarFavorito(Favorito favarito){
        Call<Favorito> call = new RetrofitConfig().getFavoriteService().inserirFavorito(favarito);
        call.enqueue(new Callback<Favorito>() {
            @Override
            public void onResponse(Call<Favorito> call, Response<Favorito> response) {

            }

            @Override
            public void onFailure(Call<Favorito> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao favoritar o evento.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletarFavorito(Favorito favarito){
        Call<ResponseBody> call = new RetrofitConfig().getFavoriteService().deletarFavorito(favarito);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao deletar o evento.", Toast.LENGTH_SHORT).show();
            }
        });
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
        Call<List<Evento>> call;
        if(user != null){
            call = new RetrofitConfig().getEventService().buscarEventosComUsuarioLogado(user.getUid());
        } else{
            call = new RetrofitConfig().getEventService().buscarEventos();
        }

        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                System.out.println(response);
                listaEvents = response.body();
                atualizaTabelaEventos(listaEvents);
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao carregar os eventos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obterListaEventosFavorito(){
        if(user != null) {
            Call<List<Evento>> call = new RetrofitConfig().getEventService().buscarEventosFavoritoPorUsuario(user.getUid());
            call.enqueue(new Callback<List<Evento>>() {
                @Override
                public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                    System.out.println(response);
                    listaEvents = response.body();

                    if(listaEvents.isEmpty()) {
                        EventoFragment.this.progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else{
                        atualizaTabelaEventos(listaEvents);
                    }
                }

                @Override
                public void onFailure(Call<List<Evento>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Erro ao carregar os eventos.", Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            EventoFragment.this.progressBar.setVisibility(View.GONE);
            EventoFragment.this.swipeLayout.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void atualizaTabelaEventos(List<Evento> eventos) {
        EventoFragment.this.listaEventos = eventos;
        EventoFragment.this.progressBar.setVisibility(View.GONE);
        EventoFragment.this.swipeLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        recyclerView.setAdapter(new EventoAdapter(getContext(), EventoFragment.this.listaEventos, onClickEvento(), onCheckedChangeListener()));
    }

}
