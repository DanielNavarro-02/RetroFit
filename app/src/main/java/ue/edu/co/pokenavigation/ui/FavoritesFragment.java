package ue.edu.co.pokenavigation.ui; // Declaración del paquete visual del proyecto

import android.os.Bundle; // Importa Bundle para el manejo de estado
import android.view.View; // Importa View para manipular la interfaz visual
import android.widget.Toast; // Importa Toast para notificaciones emergentes
import androidx.annotation.NonNull; // Anotación de parámetro no nulo
import androidx.annotation.Nullable; // Anotación de parámetro opcional
import androidx.fragment.app.Fragment; // Clase base Fragment
import androidx.recyclerview.widget.LinearLayoutManager; // Gestor de diseño lineal
import androidx.recyclerview.widget.RecyclerView; // Componente RecyclerView
import ue.edu.co.pokenavigation.R; // Recursos R del proyecto
import ue.edu.co.pokenavigation.data.local.FavoritesManager; // Gestor de almacenamiento local de favoritos
import ue.edu.co.pokenavigation.data.model.Pokemon; // Modelo de datos Pokémon
import ue.edu.co.pokenavigation.ui.adapter.PokemonAdapter; // Adaptador para el RecyclerView
import java.util.List; // Interfaz List

// Fragmento encargado de listar los Pokémon guardados en la sección Favoritos
public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerFavorites; // Componente donde se listarán los Pokémon guardados
    private PokemonAdapter adapter;         // Adaptador que conecta los datos con el RecyclerView
    private FavoritesManager favoritesManager; // Controlador para consultar SharedPreferences

    // Constructor público asociando la vista XML de fragment_favorites
    public FavoritesFragment() {
        super(R.layout.fragment_favorites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instancia el gestor de favoritos con el contexto del fragmento
        favoritesManager = new FavoritesManager(requireContext());

        // Enlaza el RecyclerView del layout XML
        recyclerFavorites = view.findViewById(R.id.recyclerFavorites);

        // Inicializa el adaptador pasando la acción al hacer clic sobre un Pokémon favorito
        adapter = new PokemonAdapter(this::abrirDetallePokemon);

        // Configura el RecyclerView con orientación vertical
        recyclerFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerFavorites.setHasFixedSize(true);
        recyclerFavorites.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarFavoritos(); // Se ejecuta siempre que el usuario entra o regresa a esta pantalla
    }

    // Consulta la lista persistida en SharedPreferences y actualiza la pantalla
    private void cargarFavoritos() {
        List<Pokemon> favoritosList = favoritesManager.getFavorites(); // Carga la lista guardada

        if (favoritosList.isEmpty()) {
            Toast.makeText(requireContext(), "No tienes Pokémon en favoritos", Toast.LENGTH_SHORT).show();
        }

        adapter.actualizarDatos(favoritosList); // Envía la lista al adaptador para dibujarla
    }

    // Abre la pantalla de detalle al hacer clic en un elemento de la lista
    private void abrirDetallePokemon(Pokemon pokemon) {
        DetailFragment detailFragment = DetailFragment.newInstance(pokemon.getName(), pokemon.getUrl());

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment) // Reemplaza en el contenedor principal
                .addToBackStack(null) // Agrega a la pila para poder volver atrás con el botón físico
                .commit();
    }
}