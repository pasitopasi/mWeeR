package Entidades;

/**
 * Created by Alejandro on 10/11/2017.
 */

public class Usuario {

    private String correo;
    private String ciudad;

    public Usuario(String correo, String password) {
        this.correo = correo;
        this.ciudad = password;
    }

    public Usuario(){

    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCiudad() { return ciudad; }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "correo='" + correo + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
