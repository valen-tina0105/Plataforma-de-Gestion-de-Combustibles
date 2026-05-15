package co.edu.unipiloto.pgc.network;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Station;
import co.edu.unipiloto.pgc.model.Subsidy;
import co.edu.unipiloto.pgc.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    //Fuel Services
    @GET("fuels")
    Call<ArrayList<Fuel>> getAllFuels();

    //Subsidy Services
    @GET("subsidies/{id}")
    Call<Subsidy> getSubsidyById(@Path("id") int id);

    //Station Services
    @GET("stations/nearby")
    Call<ArrayList<Station>> getNearbyStations(@Query("lat") double lat, @Query("lon") double lon);

    //Inventory Services
    @GET("inventories/station/{id}")
    Call<ArrayList<Inventory>> getInventoriesByStation(@Path("id") int stationId);

    @PUT("inventories/{id}/cantidad")
    Call<Void> updateInventoryQuantity(@Path("id") int inventoryId, @Query("cantidad") double cantidad);
}
