package com.unah.clases;

import com.unah.App;

import javafx.scene.canvas.GraphicsContext;

public class Tile {
	private int x;
	private int y;
	//Parametros dentro de la imagen principal
	private int altoImagen;
	private int anchoImagen;
	private int xImagen;
	private int yImagen;
	private String indiceImagen;
	private int velocidad;
	private int invertir=1;
	private boolean esSpriteSheet = true;

	public Tile(int x, int y, int anchoImagen, int altoImagen, int xImagen, int yImagen, String indiceImagen,
			int velocidad) {
		super();
		this.x = x;
		this.y = y;
		this.altoImagen = altoImagen;
		this.anchoImagen = anchoImagen;
		this.xImagen = xImagen;
		this.yImagen = yImagen;
		this.indiceImagen = indiceImagen;
		this.velocidad = velocidad;
	}
	// 2. NUEVO CONSTRUCTOR: Exclusivo para imágenes sueltas (como los planetas)
    public Tile(int x, int y, int anchoDibujo, int altoDibujo, String indiceImagen, int velocidad) {
        this.x = x;
        this.y = y;
        this.anchoImagen = anchoDibujo;
        this.altoImagen = altoDibujo;
        this.indiceImagen = indiceImagen;
        this.velocidad = velocidad;
        this.esSpriteSheet = false; // Le indicamos que NO recorte esta imagen
    }
	
	public Tile(int tipoTile,int x, int y, String indiceImagen, int velocidad){
		this.x = x;
		this.y = y;
		this.indiceImagen = indiceImagen;
		this.velocidad = velocidad;
		//this.invertir = invertir;
		switch(tipoTile){
			case 1:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 0;
				this.yImagen = 0;
			break;
			case 2:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 0;
				this.yImagen = 70;
			break;
			case 3:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 0;
				this.yImagen = 140;
			break;
			case 4:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 490;
				this.yImagen = 558;
			break;
			case 5:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 560;
				this.yImagen = 558;
			break;
			case 6:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 560;
				this.yImagen = 698;
			break;
			case 666:
				this.altoImagen = 70;
				this.anchoImagen = 70;
				this.xImagen = 70;
				this.yImagen = 558;
			break;
		}
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getAltoImagen() {
		return altoImagen;
	}
	public void setAltoImagen(int altoImagen) {
		this.altoImagen = altoImagen;
	}
	public int getAnchoImagen() {
		return anchoImagen;
	}
	public void setAnchoImagen(int anchoImagen) {
		this.anchoImagen = anchoImagen;
	}
	public int getxImagen() {
		return xImagen;
	}
	public void setxImagen(int xImagen) {
		this.xImagen = xImagen;
	}
	public int getyImagen() {
		return yImagen;
	}
	public void setyImagen(int yImagen) {
		this.yImagen = yImagen;
	}
	public String getIndiceImagen() {
		return indiceImagen;
	}
	public void setIndiceImagen(String indiceImagen) {
		this.indiceImagen = indiceImagen;
	}
	public int getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	public void pintar(GraphicsContext graficos) {
        if (esSpriteSheet) {
            // El código original del ingeniero que recorta el Tilemap
            graficos.drawImage(
                App.imagenes.get(this.indiceImagen), 
                this.xImagen, this.yImagen, 
                this.anchoImagen, this.altoImagen, 
                this.x, this.y,
                this.anchoImagen, this.altoImagen
            );
        } else {
            // NUEVO: El código que dibuja la imagen completa adaptándola al tamaño que pidas
            graficos.drawImage(
                App.imagenes.get(this.indiceImagen), 
                this.x, this.y, 
                this.anchoImagen, this.altoImagen
            );
        }
    }
	
	
	
}

/*
if (condicion)
	verdadero
else 
	falso
	
	
condicion?verdadero:falso;*/
