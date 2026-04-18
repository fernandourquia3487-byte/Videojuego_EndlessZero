package com.unah;
import javafx.scene.paint.Color;

public class EnemigoCazador extends Enemigo {
    private Nave objetivo;

    public EnemigoCazador(double x, double y, Nave objetivo) {
        // Al final agregamos Color.GREEN
        super(x, y, 40, 40, "enemigo_cazador", 2.0, 1, 20, Color.GREEN);
        this.objetivo = objetivo;
    }
    // ... tu código de mover() intacto ...

    @Override
    public void mover() {
        this.y += velocidad; // Baja lentamente
        
        // Movimiento inteligente: persigue la posición X del jugador
        if (objetivo.getX() > this.x) {
            this.x += (velocidad / 1.5); // Se mueve a la derecha
        } else if (objetivo.getX() < this.x) {
            this.x -= (velocidad / 1.5); // Se mueve a la izquierda
        }
        
        // Si sale de la pantalla por abajo, se desactiva
        if (this.y > 850) {
            this.activo = false;
        }
    }

    
}
