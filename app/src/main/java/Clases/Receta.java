package Clases;

public class Receta {

    private int idreceta;
    private String nombre;
    private String descripcion;
    private byte[] foto;
    private String pdf;
    private String autor;
    private String categoria;

    public Receta(int idreceta, String nombre, String descripcion, byte[] foto, String pdf, String autor, String categoria) {
        this.idreceta = idreceta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.pdf = pdf;
        this.autor = autor;
        this.categoria = categoria;
    }

    public Receta(String nombre, String descripcion, byte[] foto, String pdf, String autor, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.pdf = pdf;
        this.autor = autor;
        this.categoria = categoria;
    }

    public int getIdreceta() {
        return idreceta;
    }

    public void setIdreceta(int idreceta) {
        this.idreceta = idreceta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
