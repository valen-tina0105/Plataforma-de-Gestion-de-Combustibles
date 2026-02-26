package co.edu.unipiloto.pgc;

import java.io.Serializable;

public class Rule implements Serializable {
    private String tipo;
    private int precio;

    public Rule(String tipo, int precio){
        this.tipo = tipo;
        this.precio = precio;
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public int getPrecio(){
        return precio;
    }

    public void setPrecio(int precio){
        this.precio = precio;
    }

}
