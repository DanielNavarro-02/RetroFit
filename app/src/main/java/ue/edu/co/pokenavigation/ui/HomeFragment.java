package ue.edu.co.pokenavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ue.edu.co.pokenavigation.R;
import ue.edu.co.pokenavigation.data.model.Pokemon;
import ue.edu.co.pokenavigation.data.model.PokemonResponse;
import ue.edu.co.pokenavigation.data.repository.PokemonRepository;
import ue.edu.co.pokenavigation.ui.adapter.PokemonAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerPokemon;
    private CircularProgressIndicator progressIndicator;
    private LinearLayout errorContainer;
    private TextView tvError;
    private MaterialButton btnRetry;

    private PokemonAdapter adapter;
    private PokemonRepository repository;
    private Call<PokemonResponse> currentCall;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vincular componentes del layout
        recyclerPokemon = view.findViewById(R.id.recyclerPokemon);
        progressIndicator = view.findViewById(R.id.progressIndicator);
        errorContainer = view.findViewById(R.id.errorContainer);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);

        // Configurar Adapter y LayoutManager
        adapter = new PokemonAdapter(this::mostrarPokemonSeleccionado);
        repository = new PokemonRepository();

        recyclerPokemon.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerPokemon.setHasFixedSize(true);
        recyclerPokemon.setAdapter(adapter);

        btnRetry.setOnClickListener(v -> cargarPokemon());
        cargarPokemon(); // Petición inicial
    }

    private void cargarPokemon() {
        mostrarCargando();
        currentCall = repository.obtenerPokemon(30, 0); // Pide 30 registros

        // Ejecución asíncrona de Retrofit
        currentCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(@NonNull Call<PokemonResponse> call, @NonNull Response<PokemonResponse> response) {
                if (!isAdded()) return; // Previene fallos si el usuario cambió de pantalla

                PokemonResponse body = response.body();
                if (response.isSuccessful() && body != null && body.getResults() != null) {
                    adapter.actualizarDatos(body.getResults());
                    mostrarContenido();
                } else {
                    mostrarError("No se obtuvieron datos. Código HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonResponse> call, @NonNull Throwable throwable) {
                if (call.isCanceled() || !isAdded()) return;
                mostrarError("Error de conexión. Verifique internet e intente nuevamente.");
            }
        });
    }

    private void mostrarPokemonSeleccionado(Pokemon pokemon) {
        DetailFragment detailFragment = DetailFragment.newInstance(pokemon.getName(), pokemon.getUrl());

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null) // Permite regresar a la lista con el botón atrás
                .commit();
    }

    private void mostrarCargando() {
        progressIndicator.setVisibility(View.VISIBLE);
        recyclerPokemon.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarContenido() {
        progressIndicator.setVisibility(View.GONE);
        recyclerPokemon.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {
        progressIndicator.setVisibility(View.GONE);
        recyclerPokemon.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        tvError.setText(mensaje);
    }

    @Override
    public void onDestroyView() {
        // Cancela llamadas activas para evitar 'Memory Leaks' o colapsos
        if (currentCall != null) {
            currentCall.cancel();
        }
        recyclerPokemon.setAdapter(null);
        super.onDestroyView();
    }
}