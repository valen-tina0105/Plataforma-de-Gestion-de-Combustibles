package co.edu.unipiloto.pgc.dto;

public class UpdatePriceRequestDTO {

    private Double precio;

    public UpdatePriceRequestDTO(Double precio) {
        this.precio = precio;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
