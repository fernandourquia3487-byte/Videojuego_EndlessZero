package com.unah;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class GestorPuntuaciones {
    private final String RUTA_ARCHIVO = "puntuaciones.txt";

    // Clase interna para manejar cada registro fácilmente
    public static class Registro implements Comparable<Registro> {
        public String nombre;
        public int puntos;

        public Registro(String nombre, int puntos) {
            this.nombre = nombre;
            this.puntos = puntos;
        }

        @Override
        public int compareTo(Registro otro) {
            // Orden descendente (de mayor a menor)
            return Integer.compare(otro.puntos, this.puntos);
        }
    }

    public ArrayList<Registro> leerPuntuaciones() {
        ArrayList<Registro> lista = new ArrayList<>();
        try {
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                return lista; // Si el archivo no existe, retorna lista vacía
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    lista.add(new Registro(partes[0], Integer.parseInt(partes[1])));
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error al leer: " + e.getMessage());
        }
        Collections.sort(lista);
        return lista;
    }

    public void guardarPuntuacion(String nombre, int puntos) {
        ArrayList<Registro> lista = leerPuntuaciones();
        lista.add(new Registro(nombre, puntos));
        Collections.sort(lista);

        // Mantener solo el Top 10
        if (lista.size() > 10) {
            lista = new ArrayList<>(lista.subList(0, 10));
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO));
            for (Registro r : lista) {
                bw.write(r.nombre + "," + r.puntos);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public boolean esTop10(int puntos) {
        // Un puntaje de 0 no debería entrar al Top 10 para evitar spam
        if (puntos <= 0) return false; 
        
        ArrayList<Registro> lista = leerPuntuaciones();
        if (lista.size() < 10) return true; // Si hay menos de 10, entra directo
        
        // Si los puntos son mayores que el último de la lista
        return puntos > lista.get(lista.size() - 1).puntos;
    }
}
