package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;

import br.com.appteatro.appteatro.CustomView.TheaterLoadView;
import br.com.appteatro.appteatro.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int SIGN_IN_CODE = 777;
    private CallbackManager callbackManager;
    private LoginButton facebookButton;
    private GoogleApiClient googleApiClient;
    private SignInButton googleButton;
    private Button entrarButton;
    private Button btn_sigin;
    private ImageView img_logo;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private TheaterLoadView progressBar;

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mEmailField = findViewById(R.id.ed_text_email);
        mPasswordField = findViewById(R.id.ed_text_password);

        //Facebook Login
        this.loginWithFacebook();

        //Google Login
        this.loginWithGoogle();

        //Email/Senha
        this.loginWithEmailAndPassword();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };

        progressBar = (TheaterLoadView) findViewById(R.id.progressBar);
        img_logo = (ImageView) findViewById(R.id.img_logo);

        btn_sigin = (Button) findViewById(R.id.btn_sigin);
        btn_sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginWithFacebook() {

        callbackManager = CallbackManager.Factory.create();

        facebookButton = (LoginButton) findViewById(R.id.facebookButton);

        facebookButton.setReadPermissions(Arrays.asList("email"));

        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.login_cancelado, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.login_erro, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        this.sumirComponentesTelaAoCarregar();

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.login_erro_firebase, Toast.LENGTH_LONG).show();
                    aparecerComponentesTela();
                }
            }
        });
    }

    private void loginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleButton = (SignInButton) findViewById(R.id.googleButton);

        googleButton.setSize(SignInButton.SIZE_WIDE);

        googleButton.setColorScheme(SignInButton.COLOR_DARK);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
            aparecerComponentesTela();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        this.sumirComponentesTelaAoCarregar();

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.not_firebase_auth, Toast.LENGTH_SHORT).show();
                    aparecerComponentesTela();
                }
            }
        });
    }

    private void loginWithEmailAndPassword() {
        entrarButton = (Button) findViewById(R.id.entrarButton);
        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar(mEmailField.getText().toString().trim(), mPasswordField.getText().toString().trim());
            }
        });
    }

    private void logar(String email, String password) {

        if (this.isEmailAndPasswordValidos(email, password)) {

            this.sumirComponentesTelaAoCarregar();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                goMainScreen();
                            } else {
                                // If sign in fails, display a message to the user.
                                aparecerComponentesTela();
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private boolean isEmailAndPasswordValidos(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Digite seu email!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Digite sua senha!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void sumirComponentesTelaAoCarregar() {
        progressBar.setVisibility(View.VISIBLE);
        img_logo.setVisibility(View.GONE);
        googleButton.setVisibility(View.GONE);
        facebookButton.setVisibility(View.GONE);
        entrarButton.setVisibility(View.GONE);
        btn_sigin.setVisibility(View.GONE);
        mEmailField.setVisibility(View.GONE);
        mPasswordField.setVisibility(View.GONE);

    }

    private void aparecerComponentesTela() {
        progressBar.setVisibility(View.GONE);
        img_logo.setVisibility(View.VISIBLE);
        googleButton.setVisibility(View.VISIBLE);
        facebookButton.setVisibility(View.VISIBLE);
        entrarButton.setVisibility(View.VISIBLE);
        btn_sigin.setVisibility(View.VISIBLE);
        mEmailField.setVisibility(View.VISIBLE);
        mPasswordField.setVisibility(View.VISIBLE);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MenuLateralActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
