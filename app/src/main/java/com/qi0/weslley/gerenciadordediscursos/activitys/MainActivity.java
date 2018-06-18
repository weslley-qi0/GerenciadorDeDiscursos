package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.AgendaFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.CongregacoesFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.DiscursosFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.OradoresFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.SettingsFragment;

import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity {

    BottomNavigationView navigation;
    MaterialSearchView searchView;
    Toolbar toolbarPrincipal;

    FloatingActionButton fab;
    Fragment fragment = null;

    Boolean doubleBackToExitPressedOnce = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_congregacao:
                    fab.setImageResource(R.drawable.ic_congregacao_add);
                    fab.show();
                    searchView.closeSearch();
                    fragment  = new CongregacoesFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_oradores:
                    fab.setImageResource(R.drawable.ic_orador_add);
                    fab.show();
                    searchView.closeSearch();
                    fragment = new OradoresFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_agenda:
                    fab.hide();
                    searchView.closeSearch();
                    fragment = new AgendaFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_discursos:
                    fab.setImageResource(R.drawable.ic_list_add);
                    fab.show();
                    searchView.closeSearch();
                    fragment = new DiscursosFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_settings:
                    fab.hide();
                    searchView.closeSearch();
                    fragment = new SettingsFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarPrincipal = findViewById(R.id.toolbar_pricipal);
        toolbarPrincipal.setVisibility(View.INVISIBLE);
        toolbarPrincipal.setBackgroundColor(Color.YELLOW);
        setSupportActionBar(toolbarPrincipal);

        getSupportActionBar().setTitle("Home");

        fragment = new AgendaFragment();
        replaceFragment(R.id.fragment_container, fragment);

        fab = findViewById(R.id.fab);
        fab.hide();

        searchView = findViewById(R.id.material_search_view);
        navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_agenda);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (navigation.getSelectedItemId()){
                    case R.id.navigation_congregacao:
                        intent = new Intent(getApplicationContext(), AdicionarEditarActivity.class);
                        intent.putExtra("qualFragmentAbrir", "AddCongregacaoFragment");
                        break;
                    case R.id.navigation_oradores:
                        intent = new Intent(getApplicationContext(), AdicionarEditarActivity.class);
                        intent.putExtra("qualFragmentAbrir", "AddOradorFragment");
                        break;
                    case R.id.navigation_discursos:
                        intent = new Intent(getApplicationContext(), AdicionarEditarActivity.class);
                        intent.putExtra("qualFragmentAbrir", "AddDiscursosFragment");
                        break;
                }
                startActivity(intent);
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                switch (navigation.getSelectedItemId()){
                    case R.id.navigation_congregacao:
                        CongregacoesFragment congregacoesFragment = (CongregacoesFragment) fragment;
                        congregacoesFragment.recaregarCongregacoes();
                        break;
                    case R.id.navigation_oradores:
                        OradoresFragment oradoresFragment = (OradoresFragment) fragment;
                        oradoresFragment.recaregarOradores();
                        break;
                    case R.id.navigation_discursos:
                        DiscursosFragment discursosFragment = (DiscursosFragment) fragment;
                        discursosFragment.recaregarDiscursos();
                        break;
                }
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null && !newText.isEmpty()){
                    switch (navigation.getSelectedItemId()){
                        case R.id.navigation_congregacao:
                            CongregacoesFragment congregacoesFragment = (CongregacoesFragment) fragment;
                            congregacoesFragment.pequisarCongregacoes(newText.toLowerCase());
                            if (congregacoesFragment.congregacoesList.size() == 0)
                                searchView.closeSearch();
                            break;
                        case R.id.navigation_oradores:
                            OradoresFragment oradoresFragment = (OradoresFragment) fragment;
                            oradoresFragment.pequisarOradores(newText);
                            if (oradoresFragment.oradoresList.size() == 0)
                                searchView.closeSearch();
                            break;
                        case R.id.navigation_discursos:
                            DiscursosFragment discursosFragment = (DiscursosFragment) fragment;
                            discursosFragment.pequisarDiscursos(newText);
                            if (discursosFragment.discursosList.size() == 0)
                                searchView.closeSearch();
                            break;
                    }
                }else {
                    switch (navigation.getSelectedItemId()){
                        case R.id.navigation_congregacao:
                            CongregacoesFragment congregacoesFragment = (CongregacoesFragment) fragment;
                            congregacoesFragment.recaregarCongregacoes();
                            break;
                        case R.id.navigation_oradores:
                            OradoresFragment oradoresFragment = (OradoresFragment) fragment;
                            oradoresFragment.recaregarOradores();
                            break;
                        case R.id.navigation_discursos:
                            DiscursosFragment discursosFragment = (DiscursosFragment) fragment;
                            discursosFragment.recaregarDiscursos();
                            break;
                    }
                }


                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toasty.info(MainActivity.this, "Pressione novamente para sair!", Toast.LENGTH_SHORT, false).show();
            new Handler().postDelayed(new Runnable() {@Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.menu_seach_view);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_seach_view) {

        }
        return super.onOptionsItemSelected(item);
    }
}
