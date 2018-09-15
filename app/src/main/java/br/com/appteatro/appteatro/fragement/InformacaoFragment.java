package br.com.appteatro.appteatro.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.domain.model.Evento;
import br.com.appteatro.appteatro.domain.model.Genero;


public class InformacaoFragment extends Fragment {

    private Evento evento;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        evento = (Evento) getArguments().get("evento");

        return inflater.inflate(R.layout.fragment_informacao, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        setTextString(R.id.tSinopse, evento.getDescricao());
        setTextString(R.id.tElenco, evento.getInformacao().getElenco());
        setTextString(R.id.tFichaTecnica, evento.getInformacao().getFicha());
        setTextString(R.id.tGenero, Genero.valueOf(evento.getGenero()).getDescricao());
        setTextString(R.id.tDuracao, evento.getInformacao().getDuracao());

    }

    protected void setTextString(int resId, String text) {
        View view = getView();
        if (view != null) {
            TextView t = (TextView) view.findViewById(resId);
            if (t != null) {
                t.setText(text);
            }
        }
    }
}
