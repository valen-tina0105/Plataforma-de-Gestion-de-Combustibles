package co.edu.unipiloto.pgc.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.edu.unipiloto.pgc.database.DatabaseHelper;
import co.edu.unipiloto.pgc.model.Rol;
import co.edu.unipiloto.pgc.model.User;

import co.edu.unipiloto.pgc.network.ApiService;
import co.edu.unipiloto.pgc.network.LoginRequest;
import co.edu.unipiloto.pgc.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDAO {
    private ApiService apiService;

    public interface LoginCallback {
        void onSuccess(User user);

        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess(User user);

        void onError(String message);
    }

    public interface UsernameCallback {
        void onSuccess(boolean exists);

        void onError(String message);
    }

    public interface DistributorCallback {
        void onSuccess(ArrayList<User> distributors);
        void onError(String message);
    }

    public UserDAO(Context context) {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void insertarUsuario(User user, RegisterCallback callback) {
        Call<User> call = apiService.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al crear usuario");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void logIn(String username, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(username, password);
        Call<User> call = apiService.login(request);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Usuario o Contraseña incorrecta");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void getUserByUsername(String username, LoginCallback callback) {
        Call<User> call = apiService.getUserByUsername(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener usuario");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }

    public void verificarUsername(String string, UsernameCallback callback) {
        Call<Boolean> call = apiService.verificarUsername(string);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener usuario");
                }

            }


            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });

    }

    public void getAllDistributors(DistributorCallback callback) {
        Call<ArrayList<User>> call = apiService.getAllDistributors();

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener disribuidores");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });
    }
}
//
//    public ArrayList<User> getAllUsers() {
//        ArrayList<User> users = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery(
//                "SELECT u.id, u.nombre_completo, u.username, u.email, u.password, " +
//                        "u.genero, u.direccion, u.latitud, u.longitud, u.fecha_nacimiento, " +
//                        "r.id, r.nombre " +
//                        "FROM Users u " +
//                        "INNER JOIN Roles r ON u.rol_id = r.id",
//                null
//        );
//
//        while (cursor.moveToNext()) {
//            users.add(mapUser(cursor));
//        }
//
//        cursor.close();
//        return users;
//    }
//
//    public boolean verificarUsername(String username) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery(
//                "SELECT username " +
//                        "FROM Users " +
//                        "WHERE username = ? " +
//                        "LIMIT 1",
//                new String[]{username});
//        try {
//            if (cursor.moveToFirst()) {
//                return true;
//            }
//        } finally {
//            cursor.close();
//        }
//        return false;
//    }
//    public User getUserByUsername(String username) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery(
//                "SELECT u.id, u.nombre_completo, u.username, u.email, u.password, " +
//                        "u.genero, u.direccion, u.latitud, u.longitud, u.fecha_nacimiento, " +
//                        "r.id, r.nombre " +
//                        "FROM Users u " +
//                        "INNER JOIN Roles r ON u.rol_id = r.id " +
//                        "WHERE u.username = ? " +
//                        "LIMIT 1",
//                new String[]{username}
//        );
//
//        try {
//            if (cursor.moveToFirst()) {
//                return mapUser(cursor);
//            }
//        } finally {
//            cursor.close();
//        }
//
//        return null;
//    }
//    private ArrayList<User> getUsersByRol(int rolId) {
//        ArrayList<User> users = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery(
//                "SELECT u.id, u.nombre_completo, u.username, u.email, u.password, " +
//                        "u.genero, u.direccion, u.latitud, u.longitud, u.fecha_nacimiento, " +
//                        "r.id, r.nombre " +
//                        "FROM Users u " +
//                        "INNER JOIN Roles r ON u.rol_id = r.id " +
//                        "WHERE r.id = ?",
//                new String[]{String.valueOf(rolId)}
//        );
//
//        while (cursor.moveToNext()) {
//            users.add(mapUser(cursor));
//        }
//
//        cursor.close();
//        return users;
//    }
//    public ArrayList<User> getAllStations() {
//        return getUsersByRol(1);
//    }
//
//    public ArrayList<User> getAllDistributors() {
//        return getUsersByRol(5);
//    }

//
//    private User mapUser(Cursor cursor) {
//        User user = new User();
//        user.setId(cursor.getInt(0));
//        user.setNombreCompleto(cursor.getString(1));
//        user.setUsername(cursor.getString(2));
//        user.setEmail(cursor.getString(3));
//        user.setPassword(cursor.getString(4));
//        user.setGenero(cursor.getString(5));
//        user.setDireccion(cursor.getString(6));
//        user.setLatitud(cursor.getDouble(7));
//        user.setLongitud(cursor.getDouble(8));
//        user.setFechaNacimiento(cursor.getString(9));
//
//        Rol rol = new Rol();
//        rol.setId(cursor.getInt(10));
//        rol.setNombre(cursor.getString(11));
//        user.setRol(rol);
//        return user;
//    }
