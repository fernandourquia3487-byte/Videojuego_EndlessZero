package com.unah;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class EstrellaParry extends ObjetoJuego {
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private final int TOTAL_FRAMES = 4; // Los 4 fotogramas que mencionaste

    public EstrellaParry(double x, double y) {
        // Tamaño mediano (40x40) y una velocidad tranquila (1.5)
        super(x, y, 40, 40, "estrella_parry", 1.5);
    }

    public double getY() { return this.y; }
    public void setY(double y) { this.y = y; }

    @Override
    public void mover() {
        this.y += velocidad;
        if (this.y > 850) {
            this.activo = false;
        }
    }

    @Override
    public void pintar(GraphicsContext graficos) {
        Image spriteSheet = App.imagenes.get(indiceImagen);
        double anchoFrameOriginal = spriteSheet.getWidth() / TOTAL_FRAMES;
        double altoFrameOriginal = spriteSheet.getHeight();

        tiempoAnimacion += 0.016; 
        if (tiempoAnimacion >= 0.15) { // Un poco más lento para que brille bonito
            frameActual = (frameActual + 1) % TOTAL_FRAMES;
            tiempoAnimacion = 0;
        }

        double xRecorte = frameActual * anchoFrameOriginal;

        graficos.drawImage(spriteSheet,
            xRecorte, 0, anchoFrameOriginal, altoFrameOriginal, 
            this.x, this.y, this.ancho, this.alto             
        );
    }
}