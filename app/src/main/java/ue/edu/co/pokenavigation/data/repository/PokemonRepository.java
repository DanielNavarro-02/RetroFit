package ue.edu.co.pokenavigation.data.repository;

import ue.edu.co.pokenavigation.data.model.PokemonDetail;
import ue.edu.co.pokenavigation.data.model.PokemonResponse;
import ue.edu.co.pokenavigation.data.remote.PokeApiService;
import ue.edu.co.pokenavigation.data.remote.RetrofitClient;
import retrofit2.Call;

// Capa intermedia para abstener a la UI del origen exacto de la petición
public class PokemonRepository {
    private final PokeApiService service;

    public PokemonRepository() {
        this.service = RetrofitClient.getService();
    }

    public Call<PokemonResponse> obtenerPokemon(int limit, int offset) {
        return service.getPokemon(limit, offset);
    }

    public Call<PokemonDetail> obtenerDetallePokemon(String name) {
        return service.getPokemonDetail(name);
    }
}
