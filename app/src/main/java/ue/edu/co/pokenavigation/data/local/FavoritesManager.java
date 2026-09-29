package ue.edu.co.pokenavigation.data.local;
import android.content.Context; // Importa el contexto de la aplicación
import android.content.SharedPreferences; // Importa el almacenamiento local clave-valor de Android
import com.google.gson.Gson; // Importa la librería Gson para convertir objetos a cadenas JSON
import com.google.gson.reflect.TypeToken; // Importa TypeToken para mantener tipos genéricos al deserializar
import ue.edu.co.pokenavigation.data.model.Pokemon; // Importa la clase Pokémon
import java.lang.reflect.Type; // Importa Type
import java.util.ArrayList; // Importa la clase ArrayList
import java.util.List; // Importa la interfaz List


// Clase encargada de guardar y consultar la lista de Pokémon favoritos en el dispositivo
public class FavoritesManager {
    private static final String PREF_NAME = "pokemon_favorites_pref"; // Nombre del archivo SharedPreferences
    private static final String KEY_FAVORITES = "key_favorites_list";  // Clave del objeto guardado
    private final SharedPreferences prefs; // Referencia a SharedPreferences
    private final Gson gson;                // Instancia de Gson para serialización/deserialización

    // Constructor que inicializa los componentes requeridos mediante el contexto
    public FavoritesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Recupera la lista completa de Pokémon favoritos guardados en formato JSON
    public List<Pokemon> getFavorites() {
        String json = prefs.getString(KEY_FAVORITES, null); // Obtiene el texto JSON
        if (json == null) return new ArrayList<>(); // Si no hay datos guardados, retorna una lista vacía
        Type type = new TypeToken<ArrayList<Pokemon>>() {}.getType(); // Define el tipo genérico List<Pokemon>
        return gson.fromJson(json, type); // Convierte la cadena JSON de vuelta a un ArrayList de objetos
    }

    // Consulta si un Pokémon específico está guardado buscando por su nombre
    public boolean isFavorite(String name) {
        List<Pokemon> favorites = getFavorites(); // Carga la lista actual
        for (Pokemon p : favorites) { // Recorre cada elemento
            if (p.getName().equalsIgnoreCase(name)) return true; // Si coincide el nombre, está en favoritos
        }
        return false; // Retorna falso si no se encontró
    }

    // Agrega un objeto Pokémon a la lista de favoritos
    public void addFavorite(Pokemon pokemon) {
        List<Pokemon> favorites = getFavorites(); // Recupera la lista actual
        if (!isFavorite(pokemon.getName())) {     // Comprueba que no exista previamente
            favorites.add(pokemon);               // Agrega el Pokémon
            saveList(favorites);                  // Persiste la lista actualizada
        }
    }

    // Elimina un Pokémon de la lista de favoritos buscando por su nombre
    public void removeFavorite(String name) {
        List<Pokemon> favorites = getFavorites(); // Recupera la lista actual
        favorites.removeIf(p -> p.getName().equalsIgnoreCase(name)); // Filtra y remueve el elemento
        saveList(favorites); // Persiste los cambios
    }

    // Convierte la lista de objetos a texto JSON y la almacena en el disco
    private void saveList(List<Pokemon> list) {
        prefs.edit().putString(KEY_FAVORITES, gson.toJson(list)).apply(); // Guarda de manera asíncrona
    }
}