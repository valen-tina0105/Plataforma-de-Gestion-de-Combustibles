package co.edu.unipiloto.pgc.network;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.model.Delivery;
import co.edu.unipiloto.pgc.model.Fuel;
import co.edu.unipiloto.pgc.model.Inventory;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.Rule;
import co.edu.unipiloto.pgc.model.Station;
import co.edu.unipiloto.pgc.model.Subsidy;
import co.edu.unipiloto.pgc.model.Transaction;
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

    //Delivery Services
    @POST("deliveries")
    Call<Void> createDelivery(@Body Delivery delivery);

    @GET("deliveries/by-state")
    Call<ArrayList<Delivery>> getDeliveriesByState(@Query("userId") int userId, @Query("estado") String estado);

    @PUT("deliveries/{id}/delivered")
    Call<Void> markAsDelivered(@Path("id") int id, @Query("placa") String placa);

    @PUT("deliveries/{id}/confirm")
    Call<Void> confirmDelivery(@Path("id") int id);

    //Transaction Services
    @GET("transactions")
    Call<ArrayList<Transaction>> getTransactionsByStation(@Query("estacionId") int estacionId);

    @GET("transactions/by-user")
    Call<ArrayList<Transaction>> getTransactionsByUser(@Query("userId") int userId);

    @GET("transactions/by-user-by-station")
    Call<ArrayList<Transaction>> getTransactionsByUserOrderedByStation(@Query("userId") int userId);

    @GET("transactions/by-user-by-date")
    Call<ArrayList<Transaction>> getTransactionsByUserOrderedByDate(@Query("userId") int userId);

    @GET("transactions/validated")
    Call<ArrayList<Transaction>> getValidatedTransactions();

    @POST("transactions/insert")
    Call<Void> insertTransaction(@Body Transaction transaction);
}
