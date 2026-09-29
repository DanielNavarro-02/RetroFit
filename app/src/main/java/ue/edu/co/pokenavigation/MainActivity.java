package ue.edu.co.pokenavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ue.edu.co.pokenavigation.ui.FavoritesFragment;
import ue.edu.co.pokenavigation.ui.HomeFragment;
import ue.edu.co.pokenavigation.ui.InfoFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjects();
        configurarBottomNavigation();

        // Carga la pantalla inicial la primera vez que se construye la Activity
        if (savedInstanceState == null) {
            cargarFragment(new HomeFragment());
        }
    }

    private void initObjects() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    // Mapea el ID seleccionado en la barra con la instancia del Fragment objetivo
    private Fragment obtenerFragment(int itemId) {
        if (itemId == R.id.navigation_home) {
            return new HomeFragment();
        } else if (itemId == R.id.navigation_favorites) {
            return new FavoritesFragment();
        } else if (itemId == R.id.navigation_info) {
            return new InfoFragment();
        }
        return null;
    }

    // Transición e intercambio dentro del FragmentContainerView
    private void cargarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void configurarBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = obtenerFragment(item.getItemId());
            if (fragment == null) {
                return false;
            }
            cargarFragment(fragment);
            return true;
        });
    }
}