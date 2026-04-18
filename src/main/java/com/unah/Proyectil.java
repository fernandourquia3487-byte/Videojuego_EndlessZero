package com.unah;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Proyectil extends ObjetoJuego {

    public Proyectil(double x, double y, String indiceImagen, double velocidad) {
        // Hacemos el láser un poco más delgado y largo (ej: 4 de ancho, 20 de alto)
        super(x, y, 4, 20, indiceImagen, velocidad); 
    }

    @Override
    public void mover() {
        this.y -= velocidad;
        if (this.y < -20) {
            this.activo = false;
        }
    }

    // ¡NUEVO! Sobreescribimos el método pintar para no usar la imagen
    @Override
    public void pintar(GraphicsContext graficos) {
        if (activo) {
            // Dibujamos un láser de color Cyan (puedes cambiarlo a Color.YELLOW o Color.RED)
            graficos.setFill(Color.CYAN);
            graficos.fillRect(x, y, ancho, alto);
        }
    }
}