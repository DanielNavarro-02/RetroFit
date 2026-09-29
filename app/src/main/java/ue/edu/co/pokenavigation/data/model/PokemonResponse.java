package ue.edu.co.pokenavigation.data.model;
import java.util.List;
public class PokemonResponse {

    private int count;              // Guarda el total de elementos existentes en el servidor
    private String next;           // Guarda la URL de la página siguiente
    private String previous;       // Guarda la URL de la página anterior
    private List<Pokemon> results; // Guarda el listado principal de objetos Pokémon

    // Método getter para obtener el conteo total
    public int getCount() { return count; }

    // Método getter para obtener la lista de resultados
    public List<Pokemon> getResults() { return results; }

}
