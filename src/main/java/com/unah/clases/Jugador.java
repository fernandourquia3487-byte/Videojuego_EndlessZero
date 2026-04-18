package com.unah.clases;

import com.unah.App;
import com.unah.Nave;
import com.unah.SuperRayo;

import javafx.scene.canvas.GraphicsContext;

public class Jugador extends Nave {

    // --- VARIABLES DE LA MÁQUINA DE ESTADOS ---
    private boolean estabaAvanzando = false; 
    private int temporizadorIntermedio = 0; // Reloj para la desaceleración
    private int cargasSuper = 0;
    

    public Jugador(double x, double y, String indiceImagen, double velocidad) {
        // Le quitamos el 128, 128 de aquí
        super(x, y, indiceImagen, velocidad); 
    }
    // --- LA LÓGICA DE ANIMACIÓN ---
    public void actualizarAnimacion() {
        // ESTADO 5: Diagonal IZQUIERDA (¡NUEVO!)
    if (App.arriba && App.izquierda) {
        this.indiceImagen = "nave_diagonal_izquierda"; // Asegúrate de cargar esta imagen en App.java
        estabaAvanzando = true;
        temporizadorIntermedio = 0; 
    }
    
        // ESTADO 4: Diagonal DERECHA (Original, Prioridad 2)
    else if (App.arriba && App.derecha) {
        this.indiceImagen = "nave_diagonal_derecha"; // Cambié el nombre para ser claro
        estabaAvanzando = true;
        temporizadorIntermedio = 0;
    }
        // ESTADO 2: Avanzando Recto (Potencia Máxima)
        else if (App.arriba) {
            this.indiceImagen = "nave_full";
            estabaAvanzando = true;
            temporizadorIntermedio = 0;
        }
        // ESTADO 3: Fase Intermedia (Acaba de soltar la tecla "Arriba")
        else if (estabaAvanzando && !App.arriba) {
            this.indiceImagen = "nave_intermedia";
            temporizadorIntermedio++; // Empezamos a contar fotogramas

            // ¿Cuánto dura la flama intermedia? (Ej: 15 fotogramas = un cuarto de segundo)
            if (temporizadorIntermedio > 15) {
                estabaAvanzando = false; // Terminamos la transición
                temporizadorIntermedio = 0;
            }
        }
        // ESTADO 1: Quieta totalmente (Estática)
        else {
            this.indiceImagen = "nave_estatica";
            estabaAvanzando = false;
            temporizadorIntermedio = 0;
        }
    }
    @Override
    public void mover() {
        App.empujandoMapa = false; // Por defecto asumimos que no está empujando

        // 1. MOVIMIENTO VERTICAL (Con ancla a la mitad)
        if (App.arriba) {
            // El 400 es la mitad de tu pantalla (800 / 2)
            if (this.y > 400) {
                this.y -= velocidad; // Sube normalmente
            } else {
                App.empujandoMapa = true; // ¡Llegó a la mitad! Ancla la nave y avisa que empuje
            }
        }
        if (App.abajo) {
            // Límite inferior (no puede retroceder ni salirse de la pantalla)
            if (this.y < 800 - this.alto) {
                this.y += velocidad;
            }
        }

        // 2. MOVIMIENTO HORIZONTAL (Límites normales)
        if (App.izquierda) {
            if (this.x > 0) this.x -= velocidad;
        }
        if (App.derecha) {
            // El 600 es el ancho de tu ventana
            if (this.x < 600 - this.ancho) this.x += velocidad;
        }
    }

    // --- PINTADO LIMPIO ---
    @Override
    public void pintar(GraphicsContext graficos) {
        // Dibuja la imagen que la máquina de estados haya elegido
        graficos.drawImage(App.imagenes.get(indiceImagen), x, y, ancho, alto);
    }
    @Override
    public javafx.scene.shape.Rectangle obtenerRectangulo() {
        // Si ya recortaste tu imagen a ~64x64, la hitbox física puede ser un poco menor (ej. 50x60)
        // para que las balas que rozan las alas no te maten injustamente.
        int anchoLider = 50; 
        int altoLider = 60;  

        double xColision = this.x + (this.ancho / 2) - (anchoLider / 2);
        double yColision = this.y + (this.alto / 2) - (altoLider / 2);

        return new javafx.scene.shape.Rectangle(xColision, yColision, anchoLider, altoLider);
    }

    // Métodos para manejar la batería
    public int getCargasSuper() { return cargasSuper; }
    public void sumarCargaSuper() {
        if (cargasSuper < 3) cargasSuper++;
    }
    public void resetearSuper() {
        cargasSuper = 0;
    }
    // Método para disparar el ataque masivo
    public SuperRayo dispararSuper() {
        // Centramos el super rayo respecto a la nave
        double xRayo = this.x + (this.ancho / 2) - 50; 
        return new SuperRayo(xRayo, this.y);
    }
    // El punto dulce para el parry (el corazón de la nave)
    public javafx.scene.shape.Rectangle obtenerRectanguloParry() {
        // Una cajita de solo 15x15 píxeles
        int anchoSweetSpot = 85;
        int altoSweetSpot = 85;
        
        // Calculamos para que quede exactamente en el centro de tu dibujo
        double xCentro = this.x + (this.ancho / 2) - (anchoSweetSpot / 2);
        double yCentro = this.y + (this.alto / 2) - (altoSweetSpot / 2);
        
        return new javafx.scene.shape.Rectangle(xCentro, yCentro, anchoSweetSpot, altoSweetSpot);
    }
}