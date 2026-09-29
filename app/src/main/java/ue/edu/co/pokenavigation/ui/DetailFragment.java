package ue.edu.co.pokenavigation.ui;

import android.os.Bundle; // Manejo de argumentos y estado
import android.view.View; // Importa clase View
import android.widget.ImageView; // Componente para mostrar imágenes
import android.widget.TextView; // Componente para mostrar cadenas de texto
import android.widget.Toast; // Notificaciones cortas emergentes
import androidx.annotation.NonNull; // Anotación de parámetro no nulo
import androidx.annotation.Nullable; // Anotación de parámetro que permite nulo
import androidx.fragment.app.Fragment; // Clase base Fragment
import com.bumptech.glide.Glide; // Librería de procesamiento y carga de imágenes
import com.google.android.material.button.MaterialButton; // Componente botón de Material Design
import com.google.android.material.progressindicator.CircularProgressIndicator; // Indicador de carga circular
import ue.edu.co.pokenavigation.R; // Recursos R del proyecto
import ue.edu.co.pokenavigation.data.local.FavoritesManager; // Gestor de persistencia local
import ue.edu.co.pokenavigation.data.model.Pokemon; // Modelo Pokémon
import ue.edu.co.pokenavigation.data.model.PokemonDetail; // Modelo del detalle
import ue.edu.co.pokenavigation.data.repository.PokemonRepository; // Repositorio de la API
import retrofit2.Call; // Interfaz Call para la petición
import retrofit2.Callback; // Callback para recibir la respuesta
import retrofit2.Response; // Objeto con la respuesta HTTP

// Fragmento encargado de cargar y renderizar la información detallada de un Pokémon
public class DetailFragment extends Fragment {

    // Claves constantes para el paso de argumentos entre fragmentos
    private static final String ARG_POKEMON_NAME = "pokemon_name";
    private static final String ARG_POKEMON_URL = "pokemon_url";

    // Componentes de la interfaz visual XML
    private ImageView imgPokemon;
    private TextView tvDetailName, tvTypes, tvHeight, tvWeight, tvExperience;
    private MaterialButton btnFavorite;
    private CircularProgressIndicator detailProgress;

    // Controladores y servicios
    private PokemonRepository repository;
    private FavoritesManager favoritesManager;
    private String pokemonName;
    private String pokemonUrl;

    // Constructor público asociando la vista XML
    public DetailFragment() {
        super(R.layout.fragment_detail);
    }

    // Patrón Factory Method para instanciar el fragmento pasando los datos requeridos
    public static DetailFragment newInstance(String name, String url) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle(); // Contenedor de argumentos
        args.putString(ARG_POKEMON_NAME, name); // Inserta el nombre del Pokémon
        args.putString(ARG_POKEMON_URL, url);   // Inserta la URL asociada
        fragment.setArguments(args); // Asigna el Bundle al Fragmento
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recupera los argumentos enviados si la bolsa no es nula
        if (getArguments() != null) {
            pokemonName = getArguments().getString(ARG_POKEMON_NAME);
            pokemonUrl = getArguments().getString(ARG_POKEMON_URL);
        }

        // Inicializa los controladores
        repository = new PokemonRepository();
        favoritesManager = new FavoritesManager(requireContext());

        // Enlaza las referencias con sus componentes XML
        imgPokemon = view.findViewById(R.id.imgPokemon);
        tvDetailName = view.findViewById(R.id.tvDetailName);
        tvTypes = view.findViewById(R.id.tvTypes);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvExperience = view.findViewById(R.id.tvExperience);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        detailProgress = view.findViewById(R.id.detailProgress);

        actualizarBotonFavoritos(); // Estado inicial del botón Favoritos
        cargarDetalle();             // Realiza la petición a la red

        // Configura la acción de clic para agregar o quitar de la lista de favoritos
        btnFavorite.setOnClickListener(v -> {
            if (favoritesManager.isFavorite(pokemonName)) {
                favoritesManager.removeFavorite(pokemonName); // Lo remueve de la base local
                Toast.makeText(requireContext(), "Eliminado de Favoritos", Toast.LENGTH_SHORT).show();
            } else {
                Pokemon p = new Pokemon(); // Instancia un nuevo objeto Pokémon
                // Se registra el nombre y la URL base dentro del objeto
                p.setName(pokemonName);
                p.setUrl(pokemonUrl);
                favoritesManager.addFavorite(p); // Guarda el elemento
                Toast.makeText(requireContext(), "Añadido a Favoritos", Toast.LENGTH_SHORT).show();
            }
            actualizarBotonFavoritos(); // Refresca el texto visual del botón
        });
    }

    // Realiza la consulta a la PokéAPI para obtener el detalle específico
    private void cargarDetalle() {
        detailProgress.setVisibility(View.VISIBLE); // Muestra el indicador de carga
        repository.obtenerDetallePokemon(pokemonName).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetail> call, @NonNull Response<PokemonDetail> response) {
                if (!isAdded()) return; // Evita manipular la UI si el fragmento fue destruido
                detailProgress.setVisibility(View.GONE); // Oculta el spinner de carga

                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail detail = response.body(); // Extrae el cuerpo procesado del JSON

                    // Asigna el texto a los distintos componentes en pantalla
                    tvDetailName.setText(detail.getName().toUpperCase());
                    tvHeight.setText("Altura: " + (detail.getHeight() / 10.0) + " m");  // Convierte a metros
                    tvWeight.setText("Peso: " + (detail.getWeight() / 10.0) + " kg");   // Convierte a kilogramos
                    tvExperience.setText("Exp. Base: " + detail.getBaseExperience());

                    // Construye una sola cadena con la lista de tipos asignados
                    StringBuilder tipos = new StringBuilder("Tipos: ");
                    for (PokemonDetail.TypeSlot slot : detail.getTypes()) {
                        tipos.append(slot.getType().getName()).append(" ");
                    }
                    tvTypes.setText(tipos.toString().trim());

                    // Usa Glide para descargar e insertar la imagen en la vista
                    Glide.with(requireContext())
                            .load(detail.getSprites().getOfficialArtworkUrl()) // URL de la imagen
                            .into(imgPokemon); // Imagen destino
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDetail> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                detailProgress.setVisibility(View.GONE); // Oculta el spinner de carga
                Toast.makeText(requireContext(), "Error al cargar el detalle", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cambia la etiqueta del botón dinámicamente según la persistencia actual
    private void actualizarBotonFavoritos() {
        if (favoritesManager.isFavorite(pokemonName)) {
            btnFavorite.setText("Quitar de Favoritos");
        } else {
            btnFavorite.setText("Guardar en Favoritos");
        }
    }
}