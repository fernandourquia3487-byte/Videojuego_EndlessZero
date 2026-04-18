package com.unah;
import javafx.scene.paint.Color;
import com.unah.clases.Jugador; // Necesitamos importar al jugador

public class EnemigoPesado extends Enemigo {
    private Jugador objetivo; // Aquí guardamos a quién perseguir

    public EnemigoPesado(double x, double y, Jugador jugador) {
        // Le bajamos un poco la velocidad (1.0) para que se sienta más pesado
        // y le puedes subir la energía si quieres que resista más tiros.
        super(x, y, 60, 60, "enemigo_pesado", 1.0, 11, 50, Color.RED);
        this.objetivo = jugador; 
    }

    @Override
    public void mover() {
        // 1. Movimiento Vertical: Baja de forma lenta e inexorable
        this.y += velocidad; 
        
        // 2. Movimiento Horizontal: Búsqueda pesada
        // Calcular a qué distancia en X está el jugador
        double distanciaX = objetivo.getX() - this.x;
        
        // En 0.008 es un tanque pesado
        this.x += distanciaX * 0.008; 
        
        // Desactivar si sale de la pantalla por abajo
        if (this.y > 850) {
            this.activo = false;
        }
    }
}