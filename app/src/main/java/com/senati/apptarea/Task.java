package com.senati.apptarea;

public class Task {

    public static final String ESTADO_PENDIENTE = "pendiente";
    public static final String ESTADO_EN_PROGRESO = "en_progreso";
    public static final String ESTADO_COMPLETADA = "completada";

    private long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String fechaVencimiento;
    private String fechaCreacion;
    private String usuarioAsignado;

    public Task() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getUsuarioAsignado() { return usuarioAsignado; }
    public void setUsuarioAsignado(String usuarioAsignado) { this.usuarioAsignado = usuarioAsignado; }

    @Override
    public String toString() {
        // Esto es lo que se muestra en el ListView primitivo
        return titulo + "  [" + estado + "]" +
                (fechaVencimiento != null ? "  vence: " + fechaVencimiento : "");
    }
}
