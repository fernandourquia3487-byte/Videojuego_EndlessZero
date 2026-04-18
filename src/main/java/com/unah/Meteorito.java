package com.unah;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Meteorito extends ObjetoJuego {
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private final int TOTAL_FRAMES = 8;
    
    // NUEVO: Variables para controlar la trayectoria
    private int patron;
    private double xInicial;

    public Meteorito(double x, double y, int patron) {
        super(x, y, 80, 80, "meteorito_animado", 1);
        this.patron = patron;
        this.xInicial = x; // Guardamos dónde nació para los cálculos de curvas
    }

    public double getY() { return this.y; }
    public void setY(double y) { this.y = y; }

    @Override
    public void mover() {
        // Asignar la trayectoria dependiendo del patrón
        switch (patron) {
            case 0: 
                // Patrón 0: Forma de 'S' (Suave y pesada)
                this.y += velocidad;
                // Reducir el multiplicador (70) para que no barra toda la pantalla
                this.x = xInicial + Math.sin(this.y / 100.0) * 70.0;
                break;
            case 1: 
                // Patrón 1: Kamikaze (Línea recta muy rápida)
                this.y += velocidad * 2.5; 
                break;
            case 2: 
                // Patrón 2: Diagonal cruzada hacia la derecha
                this.y += velocidad;
                this.x += velocidad;
                break;
            case 3: 
                // Patrón 3: Diagonal cruzada hacia la izquierda
                this.y += velocidad;
                this.x -= velocidad;
                break;
        }

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
        if (tiempoAnimacion >= 0.3) {
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