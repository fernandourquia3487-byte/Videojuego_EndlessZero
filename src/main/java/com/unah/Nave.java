package com.unah;

import com.unah.App;

public class Nave extends ObjetoJuego {
    private int puntuacion;
    private int vidas;
    
    // NUEVO: Variables para recordar dónde inició la nave
    private double xInicial;
    private double yInicial;

    public Nave(double x, double y, String indiceImagen, double velocidad) {
        // Recuerda que aquí pusiste tus dimensiones (ej: 128, 128)
        super(x, y, 64, 64, indiceImagen, velocidad); 
        this.puntuacion = 0;
        this.vidas = 3;
        
        // Guardamos las coordenadas originales al momento de crearla
        this.xInicial = x;
        this.yInicial = y;
    }

    // NUEVO: Método para teletransportar la nave al inicio
    public void resetearPosicion() {
        this.x = xInicial;
        this.y = yInicial;
    }

    @Override
    public void mover() {
        if (App.derecha && this.x < 600 - this.ancho) { 
            this.x += velocidad;
        }
        if (App.izquierda && this.x > 0) { 
            this.x -= velocidad;
        }
        if (App.arriba && this.y > 0) { 
            this.y -= velocidad;
        }
        if (App.abajo && this.y < 800 - this.alto) { 
            this.y += velocidad;
        }
    }

    public Proyectil disparar() {
        double xBala = this.x + (this.ancho / 2) - 2.5; 
        double yBala = this.y;
        return new Proyectil(xBala, yBala, "laser", 10); 
    }

    public void sumarPuntos(int puntos) { this.puntuacion += puntos; }
    public int getPuntuacion() { return puntuacion; }
    public void perderVida() { this.vidas--; }
    public int getVidas() { return vidas; }
}