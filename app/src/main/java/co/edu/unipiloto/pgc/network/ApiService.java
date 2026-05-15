package co.edu.unipiloto.pgc.network;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<User> login(@Body LoginRequest request);

    @POST("users")
    Call<User> createUser(@Body User user);

    @GET("users/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("auth/verificarUsername/{username}")
    Call<Boolean> verificarUsername(String username);

    @GET("users/distributors")
    Call<ArrayList<User>> getAllDistributors();
}
