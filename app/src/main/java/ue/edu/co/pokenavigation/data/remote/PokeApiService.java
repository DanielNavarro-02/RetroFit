package ue.edu.co.pokenavigation.data.remote;

import retrofit2.http.Path;
import ue.edu.co.pokenavigation.data.model.PokemonDetail;
import ue.edu.co.pokenavigation.data.model.PokemonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Define las firmas de endpoints requeridas
public interface PokeApiService {
    @GET("pokemon") // Concatenado a la URL Base
    Call<PokemonResponse> getPokemon(
            @Query("limit") int limit,   // Límite de elementos
            @Query("offset") int offset  // Desplazamiento inicial
    );

    @GET("pokemon/{name}")
    Call<PokemonDetail> getPokemonDetail(@Path("name") String name);


}
