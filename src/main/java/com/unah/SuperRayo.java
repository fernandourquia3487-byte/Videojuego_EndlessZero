package com.unah;

public class SuperRayo extends Proyectil {
    // Si tu constructor de proyectil es (x, y, imagen, velocidad)
    public SuperRayo(double x, double y) {
        // 100 de ancho por 150 de alto.
        super(x, y - 100, "rayo_super", 15);
        
        // ancho y alto no sean fijos, o sobreescribir el obtenerRectangulo() aquí.
        this.ancho = 100;
        this.alto = 150;
    }
}