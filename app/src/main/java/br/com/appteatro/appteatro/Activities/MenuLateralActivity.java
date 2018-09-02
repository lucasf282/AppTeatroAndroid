package br.com.appteatro.appteatro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.adapter.TabsAdapter;
import br.com.appteatro.appteatro.fragement.PerfilFragment;
import br.com.appteatro.appteatro.fragement.dialog.AboutDialog;
import br.com.appteatro.appteatro.utils.HttpHelper;

public class MenuLateralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MenuLateralActivity";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);

        // Configura Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configura Nav Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupNavDrawer();

        // Configura ViewPage + Tabs
        setupViewPagerTabs();

        //acionarFragmentInicial();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
//            AboutDialog.showAbout(getSupportFragmentManager());
//            return true;
            Intent intent = new Intent(this, FiltroActivity.class);
            startActivity(intent);
        }
//        if (id == R.id.action_push) {
//            HttpHelper httpHelper = new HttpHelper();
//
//            if (this.user.getDisplayName() != null) {
//
//            }
//
//            try {
//                httpHelper.doRequestNotification("https://fcm.googleapis.com/fcm/send", this.user.getDisplayName() != null ? this.user.getDisplayName() : this.user.getEmail(), "Seja bem vindo ao aplicativo +Teatro", "UTF-8");
//                Toast.makeText(MenuLateralActivity.this, "Notificação solicitada. Aguarde alguns minutos.", Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
//                Toast.makeText(MenuLateralActivity.this, "Não foi possível solicitar a notificação.", Toast.LENGTH_LONG).show();
//            }
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person) {
            if(user != null) {
                Intent intent = new Intent(this, PerfilActivity.class);
                startActivity(intent);
            } else{
                this.goLoginScreen();
            }
        } else if (id == R.id.nav_event) {
            // Tela Eventos
        } else if (id == R.id.nav_local) {
            Intent intent = new Intent(this, TeatrosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat) {
            Intent intent = new Intent(this, RoomActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_help) {
            AboutDialog.showAbout(getSupportFragmentManager());
        }

        this.acionarFragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void acionarFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            //ft.replace(R.id.screen_area, fragment);

            ft.commit();
        }
    }

    private void acionarFragmentInicial() {
        Fragment fragment = new PerfilFragment();
        acionarFragment(fragment);
    }

    // Configura o Nav Drawer
    protected void setupNavDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            // Atualiza os dados do header do Navigation View
            setNavViewValues(navigationView);
        }
    }

    // Atualiza os dados do header do Navigation View
    public void setNavViewValues(NavigationView navView) {

        if (user != null) {
            View headerView = navView.getHeaderView(0);
            TextView tNome = (TextView) headerView.findViewById(R.id.textViewNome);
            TextView tEmail = (TextView) headerView.findViewById(R.id.textViewEmail);
            ImageView imgView = (ImageView) headerView.findViewById(R.id.imageView);
            tNome.setText(this.user.getDisplayName());
            tEmail.setText(this.user.getEmail());
            Glide.with(this).load(this.user.getPhotoUrl()).into(imgView);
        }

    }

    private void setupViewPagerTabs() {
        // ViewPager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        final TabsAdapter mPagerAdapter = new TabsAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // Cria as tabs com o mesmo adapter utilizado pelo ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
