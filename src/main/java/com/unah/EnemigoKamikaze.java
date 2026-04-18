package com.unah;
import javafx.scene.paint.Color;

public class EnemigoKamikaze extends Enemigo {
    public EnemigoKamikaze(double x, double y) {
        // Al final agregamos Color.PURPLE (o el que coincida con tu imagen)
        super(x, y, 40, 40, "enemigo_kamikaze", 5, 1, 10, Color.PURPLE);
    }
    // ... tu código de mover() intacto ...

    @Override
    public void mover() {
        // Simplemente baja muy rápido hacia el jugador [cite: 60]
        this.y += velocidad;
        
        // Si sale de la pantalla por abajo, se desactiva
        if (this.y > 850) {
            this.activo = false;
        }
    }
    
}
