package co.edu.unipiloto.pgc;

import java.io.Serializable;

public class User implements Serializable {
    private String user;
    private String rol;
    private String contrasenia;

    public User(String user, String rol, String contrasenia) {
        this.user = user;
        this.rol = rol;
        this.contrasenia = contrasenia;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContraseña() {
        return contrasenia;
    }

    public void setContraseña(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
