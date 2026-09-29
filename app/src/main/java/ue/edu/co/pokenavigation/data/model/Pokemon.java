package ue.edu.co.pokenavigation.data.model;

public class Pokemon {
    private String name; // Guarda el nombre del Pokémon devuelto por el JSON
    private String url;  // Guarda la URL con los detalles del Pokémon

    // Método getter para obtener el nombre
    public String getName() { return name; }

    // Método getter para obtener la URL
    public String getUrl() { return url; }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
