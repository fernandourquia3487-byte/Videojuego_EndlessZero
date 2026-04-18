package com.unah;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public abstract class ObjetoJuego {
    protected double x;
    protected double y;
    protected int ancho;
    protected int alto;
    protected String indiceImagen;
    protected double velocidad;
    protected boolean activo; // Para saber si debe ser eliminado del ArrayList

    public ObjetoJuego(double x, double y, int ancho, int alto, String indiceImagen, double velocidad) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.indiceImagen = indiceImagen;
        this.velocidad = velocidad;
        this.activo = true;
    }
    // Método abstracto que cada hijo implementará a su manera
    public abstract void mover();

    public void pintar(GraphicsContext graficos) {
        if (activo) {
            graficos.drawImage(App.imagenes.get(indiceImagen), x, y, ancho, alto);
        }
    }

    public Rectangle obtenerRectangulo() {
        return new Rectangle(x, y, ancho, alto);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Getters y Setters básicos...
    public double getX() { return x; }
    public double getY() { return y; }
    
}
