package ue.edu.co.pokenavigation.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Instancia singleton para centralizar la configuración de red
public class RetrofitClient {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/"; // URL Base (debe terminar en /)
    private static Retrofit retrofit;

    private RetrofitClient() {} // Constructor privado para evitar instanciación

    public static PokeApiService getService() {
        if (retrofit == null) {
            // Interceptor para depurar las peticiones en Logcat
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Construcción del motor Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()) // Conversión automática JSON a Java
                    .build();
        }
        return retrofit.create(PokeApiService.class);
    }

}
