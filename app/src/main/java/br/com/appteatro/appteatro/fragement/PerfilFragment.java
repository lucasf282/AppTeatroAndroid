package br.com.appteatro.appteatro.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.appteatro.appteatro.Activities.EditarPerfilActivity;
import br.com.appteatro.appteatro.Activities.LoginActivity;
import br.com.appteatro.appteatro.R;

public class PerfilFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    private ImageView photoImageView;

    private int tipo;

    // Método para instanciar esse fragment pelo tipo.
    public static PerfilFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        PerfilFragment f = new PerfilFragment();
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
        // CarrosApplication.getInstance().getBus().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        uidTextView = (TextView) view.findViewById(R.id.uidTextView);
        photoImageView = (ImageView) view.findViewById(R.id.photoImageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();

            nameTextView.setText(name);
            emailTextView.setText(email);
            uidTextView.setText(uid);
            Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
        } else {
            goLoginScreen();
        }

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
        });

        view.findViewById(R.id.buttonEditar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEditarPerfilScreen();
            }
        });
    }

    private void goLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goEditarPerfilScreen(){
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }
}
