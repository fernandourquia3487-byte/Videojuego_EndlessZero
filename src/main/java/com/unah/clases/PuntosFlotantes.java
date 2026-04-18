package com.unah.clases;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PuntosFlotantes {
    private String texto;
    private double x;
    private double y;
    
    // VARIABLES PARA LA ANIMACIÓN
    private double velocidadSubida = 1.0; // Píxeles por fotograma
    private int contadorVida = 0; // Reloj de vida
    private int maxVida = 60; // Duración (1 segundo a 60 fps)
    private boolean activo = true;

    public PuntosFlotantes(String texto, double x, double y) {
        this.texto = texto;
        // Ajustamos la posición inicial un poco para que salga del centro del enemigo
        this.x = x - (texto.length() * 4); 
        this.y = y;
    }

    public void actualizar() {
        // 1. Movemos el texto hacia arriba
        y -= velocidadSubida;
        
        // 2. Contamos la vida
        contadorVida++;
        if (contadorVida >= maxVida) {
            activo = false; // El texto debe desaparecer
        }
    }

    public void pintar(GraphicsContext graficos) {
        if (activo) {
            // Guardamos el color actual para no afectar a los demás textos
            graficos.save(); 
            
            // Estilo del texto retro (blanco con borde o amarillo)
            graficos.setFill(Color.YELLOW); // Color amarillo retro
            graficos.setFont(new Font("Arial", 16)); // Puedes usar una fuente pixelada si tienes

            // Calculamos la transparencia (opacidad) para que se desvanezca
            double opacidad = 1.0 - ((double)contadorVida / maxVida);
            graficos.setGlobalAlpha(opacidad);

            // Dibujamos el texto
            graficos.fillText(texto, x, y);
            
            // Devolvemos el estado del pincel a la normalidad
            graficos.restore(); 
        }
    }

    public boolean isActivo() {
        return activo;
    }
}