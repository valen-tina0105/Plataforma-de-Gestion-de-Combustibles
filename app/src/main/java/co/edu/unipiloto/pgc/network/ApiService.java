package co.edu.unipiloto.pgc.network;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    //Rol Services
    @GET("roles")
    Call<ArrayList<Rol>> getAllRoles();

    //User Services
    @POST("auth/login")
    Call<User> login(@Body LoginRequest request);

    @POST("users")
    Call<User> createUser(@Body User user);

    @GET("users/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("auth/verificarUsername/{username}")
    Call<Boolean> verificarUsername(@Path("username") String username);

    @GET("users/distributors")
    Call<ArrayList<User>> getAllDistributors();

    //Rule Services
    @GET("rules")
    Call<ArrayList<Rule>> getAllRulees();

    @POST("rules")
    Call<Rule> saveRule(@Body Rule rule);
}
