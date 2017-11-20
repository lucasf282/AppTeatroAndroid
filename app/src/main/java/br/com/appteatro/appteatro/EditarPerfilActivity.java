package br.com.appteatro.appteatro;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditarPerfilActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText nomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        nomeTextView = findViewById(R.id.ed_txt_nome);

        nomeTextView.setText(user.getDisplayName());
    }

    public void atualizar(View view) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://marketplace.canva.com/MAB4Yqx-uWs/1/thumbnail/canva-young-executive-woman-profile-icon--MAB4Yqx-uWs.png"))
                .build();

        user.updateProfile(profileUpdates) .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    goMenuScreen();
                } else{
                    Toast.makeText(EditarPerfilActivity.this, "Erro ao Atualizar",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMenuScreen(){
        Intent intent = new Intent(this, MenuLateralActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
