package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.appteatro.appteatro.R;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mRepeatPassword;

    private Button cadastrarButton;
    private Button CancelarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = findViewById(R.id.ed_txt_email);
        mPasswordField = findViewById(R.id.ed_txt_password);
        mRepeatPassword = findViewById(R.id.ed_txt_confirm_password);

        // Buttons
        cadastrarButton = (Button) findViewById(R.id.btn_cadastrar);
        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString().trim(), mPasswordField.getText().toString().trim(), mRepeatPassword.getText().toString().trim());
            }
        });

        CancelarButton = (Button) findViewById(R.id.btn_cancelar);
        CancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastroActivity.this.finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goMainScreen();
        }
    }

    private void createAccount(String email, String password, String repeatPassword) {

        if (this.isEmailAndPasswordValidos(email, password, repeatPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                goMainScreen();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(CadastroActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean isEmailAndPasswordValidos(String email, String password, String repeatPassword) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Email é obrigatório!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Senha deve conter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Senha é obrigatório!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (TextUtils.isEmpty(repeatPassword)) {
                Toast.makeText(getApplicationContext(), "Confirmar a Senha é obrigatório!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (!password.equals(repeatPassword)) {
                    Toast.makeText(getApplicationContext(), "A senha dever ser igual ao Confirmar Senha!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
