package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.AgendaFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.CongregacoesFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.DiscursosFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.OradoresFragment;
import com.qi0.weslley.gerenciadordediscursos.fragments.SettingsFragment;

public class MainActivity extends BaseActivity {

    BottomNavigationView navigation;
    FloatingActionButton fab;
    Fragment fragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_congregacao:
                    fab.setImageResource(R.drawable.ic_congregacao_add);
                    fab.show();
                    fragment  = new CongregacoesFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_oradores:
                    fab.setImageResource(R.drawable.ic_orador_add);
                    fab.show();
                    fragment = new OradoresFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_agenda:
                    fab.hide();
                    fragment = new AgendaFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_discursos:
                    fab.setImageResource(R.drawable.ic_list_add);
                    fab.show();
                    fragment = new DiscursosFragment();
                    replaceFragment(R.id.fragment_container, fragment);
                    return true;
                case R.id.navigation_settings:
                    fab.hide();
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

        fragment = new AgendaFragment();
        replaceFragment(R.id.fragment_container, fragment);

        fab = findViewById(R.id.fab);
        fab.hide();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
    }

}
