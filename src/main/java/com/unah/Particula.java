package com.unah;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particula {
    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;
    private double vida; // Controla la opacidad (de 1.0 a 0.0)
    private Color color;

    public Particula(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        
        // Direcciones aleatorias simulando una explosión circular
        this.velocidadX = (Math.random() - 0.5) * 10; // Valor entre -5 y 5
        this.velocidadY = (Math.random() - 0.5) * 10;
        
        this.vida = 1.0; // Inicia 100% visible
    }

    public void actualizar() {
        this.x += velocidadX;
        this.y += velocidadY;
        this.vida -= 0.03; // Se va desvaneciendo poco a poco (ajusta para que dure más o menos)
    }

    public void pintar(GraphicsContext graficos) {
        if (vida > 0) {
            graficos.setGlobalAlpha(vida); // Aplica la transparencia actual
            graficos.setFill(color);
            graficos.fillRect(x, y, 4, 4); // Dibuja un pequeño cuadrado de 4x4 píxeles
            graficos.setGlobalAlpha(1.0);  // Restaura la opacidad normal para el resto del juego
        }
    }

    public boolean estaDesvanecida() {
        return vida <= 0;
    }
    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}