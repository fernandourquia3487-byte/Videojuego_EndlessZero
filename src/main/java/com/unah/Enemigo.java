package com.unah;

import javafx.scene.paint.Color; // ¡Importante agregar esto!

public abstract class Enemigo extends ObjetoJuego {
    protected int energia; 
    protected int puntosOtorgados; 
    protected Color colorExplosion; // NUEVO: El color de sus partículas
    private double xInicial; 
    private double tiempo = 0; // Un reloj interno para la onda senoidal
    private int temporizadorDisparo = 0;
    private int limiteDisparo = 120;

    // Modificamos el constructor para recibir el Color
    public Enemigo(double x, double y, int ancho, int alto, String indiceImagen, double velocidad, int energia, int puntosOtorgados, Color colorExplosion) {
        super(x, y, ancho, alto, indiceImagen, velocidad);
        this.energia = energia;
        this.puntosOtorgados = puntosOtorgados;
        this.colorExplosion = colorExplosion;
        this.xInicial = x;
    }

    public void recibirDano(int cantidad) {
        this.energia -= cantidad;
        if (this.energia <= 0) {
            this.activo = false; 
        }
    }

    public int getPuntosOtorgados() {
        return puntosOtorgados;
    }

    // NUEVO: Método para que App.java sepa de qué color pintar la explosión
    public Color getColorExplosion() {
        return colorExplosion;
    }
    public void setY(double y) {
        this.y = y;
    }
    @Override
    public void mover() {
        // La Y sigue bajando normalmente
        this.y += velocidad; 
        
        // La X se mueve en forma de onda (Zig-Zag)
        tiempo += 0.05; // Qué tan rápido hace el zig-zag
        int amplitud = 80; // Qué tan ancho es el zig-zag (en píxeles)
        this.x = xInicial + (Math.sin(tiempo) * amplitud); 
    }
    public Proyectil dispararEnemigo() {
        temporizadorDisparo++; // Sumamos 1 cada fotograma
        
        // Si el temporizador llega al límite, disparamos y reseteamos
        if (temporizadorDisparo >= limiteDisparo) {
            temporizadorDisparo = 0; 
            
            // Retorna un proyectil nuevo. IMPORTANTE: Su velocidad Y debe ser positiva para que baje
            // Asumiendo que tu constructor de Proyectil recibe (x, y, imagen, velocidad)
            return new Proyectil(this.x + (this.ancho / 2), this.y + this.alto, "laser_enemigo", 7);
        }
        
        return null; // Si no es tiempo, retorna nada
    }
}