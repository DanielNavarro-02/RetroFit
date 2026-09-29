package ue.edu.co.pokenavigation.data.model;
import com.google.gson.annotations.SerializedName; // Importa la anotación para mapear claves JSON a variables Java
import java.util.List; // Importa la clase List para manejar colecciones de datos


// Clase Java que mapea el objeto JSON retornado por el endpoint /pokemon/{name}
public class PokemonDetail {
    private String name; // Nombre del Pokémon
    private int height;  // Altura devuelta por la API (en decímetros)
    private int weight;  // Peso devuelto por la API (en hectogramos)

    @SerializedName("base_experience") // Vincula el atributo 'base_experience' del JSON con esta variable
    private int baseExperience;

    private Sprites sprites;      // Objeto conteniendo las URLs de las imágenes
    private List<TypeSlot> types; // Lista con los tipos asignados al Pokémon (fuego, agua, etc.)

    // Métodos Getter para acceder a las propiedades privadas desde fuera de la clase
    public String getName() { return name; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
    public int getBaseExperience() { return baseExperience; }
    public Sprites getSprites() { return sprites; }
    public List<TypeSlot> getTypes() { return types; }

    // Clase interna para mapear la estructura de la imagen dentro del JSON
    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault; // Imagen por defecto

        @SerializedName("other")
        private Other other; // Objeto contenedor de arte oficial

        // Método auxiliar para obtener la mejor calidad de imagen disponible
        public String getOfficialArtworkUrl() {
            if (other != null && other.officialArtwork != null && other.officialArtwork.frontDefault != null) {
                return other.officialArtwork.frontDefault; // Retorna la ilustración oficial si existe
            }
            return frontDefault; // Retorna la imagen básica de respaldo
        }

        // Estructura interna para navegar en: other -> official-artwork -> front_default
        public static class Other {
            @SerializedName("official-artwork")
            private OfficialArtwork officialArtwork;
        }

        public static class OfficialArtwork {
            @SerializedName("front_default")
            private String frontDefault;
        }
    }

    // Clase interna para extraer los nombres de los tipos (ej: "fire", "water")
    public static class TypeSlot {
        private Type type;
        public Type getType() { return type; }

        public static class Type {
            private String name;
            public String getName() { return name; }
        }
    }
}
