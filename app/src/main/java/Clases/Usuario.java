package Clases;

public class Usuario {
    private String email;
    private String idusuario;
    private String nombre;
    private String apellidos;
    private String usuario;
    private String movil;
    private String biografia;
    private String fechanac;
    private String foto;
    private String latitud;
    private String altitud;

    public Usuario(){}

    public Usuario(String nombre, String apellidos, String email, String usuario, String biografia, String movil, String fechanac, String foto, String latitud, String altitud) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.usuario = usuario;
        this.movil = movil;
        this.biografia = biografia;
        this.fechanac = fechanac;
        this.foto = foto;
        this.latitud = latitud;
        this.altitud = altitud;
    }

    public Usuario(String idusuario, String nombre, String apellidos, String usuario, String email, String biografia, String movil, String fechanac, String foto, String latitud, String altitud) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.email = email;
        this.biografia = biografia;
        this.movil = movil;
        this.fechanac = fechanac;
        this.foto = foto;
        this.latitud = latitud;
        this.altitud = altitud;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getFechanac() {
        return fechanac;
    }

    public void setFechanac(String fechanac) {
        this.fechanac = fechanac;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }
}
