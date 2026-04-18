package com.unah;

import com.unah.clases.Jugador;
import com.unah.clases.PuntosFlotantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javafx.scene.media.AudioClip; // Agrega esto junto a tus otros imports
import javafx.scene.media.MediaPlayer;

import com.unah.Enemigo;
import com.unah.EnemigoCazador;
import com.unah.EnemigoKamikaze;
import com.unah.EnemigoPesado;
import com.unah.GestorPuntuaciones;
import com.unah.Nave;
import com.unah.Proyectil;
import com.unah.clases.Tile;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.unah.Particula; // Importación nueva
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;




public class App extends Application {
    private Scene escena;
    private Group root;
    private Canvas canvas;
    private GraphicsContext graficos;
    private final int ANCHO_VENTANA = 600;
    private final int ALTO_VENTANA = 800;
    public static HashMap<String, Image> imagenes; 
    private Jugador jugador;
    private ArrayList<Proyectil> proyectiles;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Particula> particulas;
    private ArrayList<Tile> fondo;
    private ArrayList<PuntosFlotantes> listaPuntosFlotantes;
    public static boolean derecha = false;
    public static boolean izquierda = false;
    public static boolean arriba = false;
    public static boolean abajo = false;
    public static boolean empujandoMapa = false;
    // Controladores de meteoritos
    private double tiempoUltimoMeteorito = 0;
    private int contadorMeteoritos = 0;
    
    private double tiempoUltimoEnemigo = 0;
    private int oleada = 0;
    // Control del fin del juego
    private boolean juegoTerminado = false;
    private AnimationTimer animationTimer;
    private VBox menuGameOver; // Contenedor para los botones
    private VBox menuInicio; // Menú principal
    private AudioClip sonidoDisparo;
    private AudioClip sonidoDanio;
    private AudioClip sonidoExplosion;
    private MediaPlayer musicaMenu;
    private String[] playlist = {"MegadethCancion.mp3"}; 
    private int indiceCancionActual = 0;
    private MediaPlayer musicaJuego; // El reproductor para la acción
    private ArrayList<Proyectil> proyectilesEnemigos;
    private ArrayList<Meteorito> obstaculos;
    public static boolean teclaParry = false; // La tecla 'A'
    public static boolean teclaSuper = false; // La tecla para usar el poder (ej. 'S' o 'C')
    private ArrayList<EstrellaParry> estrellasParry = new ArrayList<>();
    private double tiempoUltimaEstrellaParry = 0;
    private AudioClip sonidoParryNormal;
    private AudioClip sonidoParryLleno;
    private AudioClip sonidoSuperRayo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ventana) throws Exception {
        inicializarComponentes();
        graficos = canvas.getGraphicsContext2D();
        ventana.setScene(escena);
        ventana.setTitle("Endless Zero");
        gestionarEventos();
        pintar();
        if (musicaMenu != null) {
            musicaMenu.play();
        }
        ventana.show();
        //cicloJuego();       
    }
    
    public void inicializarComponentes() {
        root = new Group();
        escena = new Scene(root, ANCHO_VENTANA, ALTO_VENTANA);
        canvas  = new Canvas(ANCHO_VENTANA, ALTO_VENTANA);
        imagenes = new HashMap<String,Image>();
        
        cargarImagenes();
        listaPuntosFlotantes = new ArrayList<>();

        imagenes.put("meteorito_animado", new Image(getClass().getResource("meteorito_animado.png").toExternalForm()));
        obstaculos = new ArrayList<Meteorito>();

        sonidoDisparo = new AudioClip(getClass().getResource("SonidoDisparoJugador.mp3").toExternalForm());
        sonidoDanio = new AudioClip(getClass().getResource("SonidoExplosion.mp3").toExternalForm());
        sonidoExplosion = new AudioClip(getClass().getResource("ExplosionEnemigos.mp3").toExternalForm());
        // ... justo donde cargas sonidoDisparo y sonidoDanio ...
        sonidoParryNormal = new AudioClip(getClass().getResource("ParryNormal.mp3").toExternalForm());
        sonidoParryLleno = new AudioClip(getClass().getResource("ParryUlti.mp3").toExternalForm()); // El épico
        sonidoSuperRayo = new AudioClip(getClass().getResource("LaserUlti.mp3").toExternalForm());
        //Cargar y configurar la música del menú
        try {
            Media mediaMenu = new Media(getClass().getResource("Muse - Hysteria.mp3").toExternalForm());
            musicaMenu = new MediaPlayer(mediaMenu);
            musicaMenu.setCycleCount(MediaPlayer.INDEFINITE); // Para que se repita en bucle
            // Opcional: ajustar el volumen si está muy alta (0.0 a 1.0)
            // musicaMenu.setVolume(0.5); 
        } catch (Exception e) {
            System.out.println("Error al cargar música del menú: " + e.getMessage());
        }

        jugador = new Jugador(ANCHO_VENTANA / 2, ALTO_VENTANA - 100, "NaveEstatica1", 5);
        proyectiles = new ArrayList<Proyectil>();
        proyectilesEnemigos = new ArrayList<Proyectil>();
        enemigos = new ArrayList<Enemigo>();
        fondo = new ArrayList<Tile>();
        particulas = new ArrayList<Particula>(); // NUEVO
        
        cargarFondo();
        // --- CONFIGURACIÓN DEL MENÚ DE GAME OVER ---
    menuGameOver = new VBox(30); // 30 píxeles de separación
    menuGameOver.setAlignment(Pos.CENTER);
    // Hacer que ocupe toda la pantalla para que el centrado sea perfecto
    menuGameOver.setPrefSize(ANCHO_VENTANA, ALTO_VENTANA);
    // Un fondo semitransparente rojizo oscuro para darle peso a la derrota
    menuGameOver.setStyle("-fx-background-color: rgba(40, 0, 0, 0.75);");
    menuGameOver.setVisible(false);

    // Título dramático de Game Over
    Label lblGameOver = new Label("GAME OVER");
    lblGameOver.setTextFill(Color.web("#ff0000")); // Rojo puro
    lblGameOver.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 70));
    
    // Efecto de neón para GAME OVER
    javafx.scene.effect.DropShadow glowGameOver = new javafx.scene.effect.DropShadow();
    glowGameOver.setColor(Color.web("#ff5555"));
    glowGameOver.setRadius(25);
    glowGameOver.setSpread(0.6);
    lblGameOver.setEffect(glowGameOver);

    // Botón de Reiniciar (Estilo "CONTINUE")
    Button btnReiniciar = new Button("> CONTINUE <");
    btnReiniciar.setStyle(
        "-fx-background-color: transparent; " +
        "-fx-text-fill: #00ffff; " + // Cyan neón
        "-fx-font-family: 'Courier New'; " +
        "-fx-font-size: 32px; " +
        "-fx-font-weight: bold; " +
        "-fx-cursor: hand;"
    );

    // Botón de Top 10 (Estilo "HIGHSCORES")
    Button btnTop10 = new Button("HIGHSCORES");
    btnTop10.setStyle(
        "-fx-background-color: transparent; " +
        "-fx-text-fill: #ffff00; " + // Amarillo arcade
        "-fx-font-family: 'Courier New'; " +
        "-fx-font-size: 22px; " +
        "-fx-font-weight: bold; " +
        "-fx-cursor: hand;"
    );

    // Efecto de parpadeo para el botón de continuar
    javafx.animation.FadeTransition parpadeoContinue = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(0.6), btnReiniciar);
    parpadeoContinue.setFromValue(1.0);
    parpadeoContinue.setToValue(0.1);
    parpadeoContinue.setCycleCount(javafx.animation.Animation.INDEFINITE);
    parpadeoContinue.setAutoReverse(true);
    parpadeoContinue.play();

    btnReiniciar.setOnAction(e -> reiniciarJuego());
    btnTop10.setOnAction(e -> mostrarTop10(new GestorPuntuaciones()));

    menuGameOver.getChildren().addAll(lblGameOver, btnReiniciar, btnTop10); 
        
        // --- CREACIÓN DEL MENÚ DE INICIO ---
        menuInicio = new VBox(40);
        menuInicio.setAlignment(Pos.CENTER);
        menuInicio.setPrefSize(ANCHO_VENTANA, ALTO_VENTANA);
        // Opcional: un fondo oscuro semi-transparente para que el título resalte sobre las estrellas
        menuInicio.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        
        // 1. Obtener el High Score actual
        GestorPuntuaciones gestor = new GestorPuntuaciones();
        ArrayList<GestorPuntuaciones.Registro> tops = gestor.leerPuntuaciones();
        int highScore = tops.isEmpty() ? 0 : tops.get(0).puntos;
        
        // Formato arcade: 1UP arriba y el score relleno con ceros (ej: 0090000)
        Label lblHighScore = new Label("HIGH SCORE\n00     " + String.format("%06d", highScore));
        lblHighScore.setTextFill(Color.web("#ff0000")); // Rojo neón clásico
        lblHighScore.setFont(Font.font("Courier New", FontWeight.BOLD, 26));
        
        // 2. Título del juego
        Label lblTitulo = new Label("ENDLESS ZERO");
        lblTitulo.setTextFill(Color.web("#00ffff")); // Cyan neón
        lblTitulo.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 75));
        
        // Añadimos un efecto de brillo (sombra exterior) al título
        javafx.scene.effect.DropShadow sombra = new javafx.scene.effect.DropShadow();
        sombra.setColor(Color.web("#00aaaa"));
        sombra.setRadius(15);
        sombra.setSpread(0.5);
        lblTitulo.setEffect(sombra);

        // 3. Botón Jugar (Transformado en "PRESS START")
        Button btnJugar = new Button("- INICIAR JUEGO -");
        // CSS para quitar el fondo del botón y dejar solo texto amarillo arcade
        btnJugar.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #ffff00; " +
            "-fx-font-family: 'Courier New'; " +
            "-fx-font-size: 28px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-alignment: center; " +
            "-fx-cursor: hand;"
        );

        // Animación de parpadeo para el botón
        javafx.animation.FadeTransition parpadeo = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(0.8), btnJugar);
        parpadeo.setFromValue(1.0);
        parpadeo.setToValue(0.1);
        parpadeo.setCycleCount(javafx.animation.Animation.INDEFINITE);
        parpadeo.setAutoReverse(true);
        parpadeo.play();

        btnJugar.setOnAction(e -> {
            menuInicio.setVisible(false); // Ocultar el menú

            if (musicaMenu != null) {
                musicaMenu.stop();
            }
            indiceCancionActual = 0;
            reproducirSiguienteCancion();
            
            cicloJuego(); // ¡Arrancar el motor del juego!
        });
        
        menuInicio.getChildren().addAll(lblHighScore, lblTitulo, btnJugar);
        root.getChildren().addAll(canvas, menuInicio, menuGameOver);
    }

        
        
    
    
    public void cargarImagenes() {
        imagenes.put("nave", new Image(getClass().getResource("NaveJugador.png").toExternalForm())); 
        imagenes.put("enemigo_kamikaze", new Image(getClass().getResource("EnemigoKamikaze.png").toExternalForm()));
        //imagenes.put("laser", new Image(getClass().getResource("item.png").toExternalForm()));
        imagenes.put("estrella", new Image(getClass().getResource("tilemap.png").toExternalForm()));
        imagenes.put("enemigo_cazador", new Image(getClass().getResource("Enemigo_Cazador.png").toExternalForm()));
        imagenes.put("enemigo_pesado", new Image(getClass().getResource("EnemigoPesado.png").toExternalForm()));
        imagenes.put("estrella_blanca", new Image(getClass().getResource("EstrellaPequeña.png").toExternalForm()));
        imagenes.put("estrella_azul", new Image(getClass().getResource("EstrellaPequeña.png").toExternalForm()));
        imagenes.put("planeta", new Image(getClass().getResource("PlanetaLejano.png").toExternalForm()));
        imagenes.put("planeta1", new Image(getClass().getResource("PlanetaRojo.png").toExternalForm()));
        imagenes.put("luna", new Image(getClass().getResource("AsteroidePequeno.png").toExternalForm()));
        imagenes.put("planeta_anillos", new Image(getClass().getResource("PlanetaSaturno.png").toExternalForm()));
        imagenes.put("propulsor_sprites", new Image(getClass().getResource("PropulsoresNave.png").toExternalForm()));
        imagenes.put("nave_estatica", new Image(getClass().getResource("NaveEstatica1.png").toExternalForm()));
        imagenes.put("nave_full", new Image(getClass().getResource("NavePotenciaMaxima2.png").toExternalForm()));
        imagenes.put("nave_intermedia", new Image(getClass().getResource("NaveIntermedia3.png").toExternalForm()));
        imagenes.put("nave_diagonal_derecha", new Image(getClass().getResource("NaveDiagonal4.png").toExternalForm()));
        imagenes.put("nave_diagonal_izquierda", new Image(getClass().getResource("NaveDiagonal5.png").toExternalForm()));
        imagenes.put("estrella_parry", new Image(getClass().getResource("EstrellasFrames.png").toExternalForm()));
    }
    public void cargarFondo() {
        for (int i = 0; i < 40; i++) {
            fondo.add(new Tile((int)(Math.random() * ANCHO_VENTANA), (int)(Math.random() * ALTO_VENTANA), 5, 5, "estrella_blanca", 1));
        }
        for (int i = 0; i < 15; i++) {
            fondo.add(new Tile((int)(Math.random() * ANCHO_VENTANA), (int)(Math.random() * ALTO_VENTANA), 8, 8, "estrella_azul", 3));
        }
    }

    public void pintar() {
        graficos.setFill(Color.BLACK);
        graficos.fillRect(0, 0, ANCHO_VENTANA, ALTO_VENTANA);
        
        for (Tile estrella : fondo){
            estrella.pintar(graficos);
        }

        for (Proyectil p : proyectiles) p.pintar(graficos);
        for (Enemigo e : enemigos) e.pintar(graficos);

        // Pintar las partículas de la explosión
        for (Particula p : particulas) p.pintar(graficos);
        
        if (!juegoTerminado) jugador.pintar(graficos);
        for (PuntosFlotantes p : listaPuntosFlotantes) {
            p.pintar(graficos);
        }
        graficos.setFill(Color.WHITE);
        graficos.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        graficos.fillText("Puntos: " + jugador.getPuntuacion(), 20, 30);
        graficos.fillText("Vidas: " + jugador.getVidas(), 20, 50);

        for (Meteorito m : obstaculos) {
        m.pintar(graficos);
        }
        // Dibuja las estrellas de Parry
        for (EstrellaParry ep : estrellasParry) {
            ep.pintar(graficos);
        }

        // Dibuja al jugador encima de todo
        if (!juegoTerminado) jugador.pintar(graficos);
        // Dibujar UI de Batería Super
        graficos.setFill(Color.WHITE);
        graficos.fillText("SUPER:", 20, 80);
        
        // Dibujamos 3 bloquecitos para la batería
        for (int i = 0; i < 3; i++) {
            if (i < jugador.getCargasSuper()) {
                // Bloque lleno (Rosado chillón estilo Cuphead o Cyan retro)
                graficos.setFill(Color.web("#0055ff")); // Cyan neón
                graficos.fillRect(105 + (i * 25), 62, 20, 20);
            } else {
                // Bloque vacío (Solo el borde)
                graficos.setStroke(Color.WHITE);
                graficos.strokeRect(105 + (i * 25), 62, 20, 20);
            }
        }
        if (jugador.getCargasSuper() == 3) {
            graficos.setFill(Color.YELLOW);
            graficos.fillText("ULTI LISTA", 240, 80);
        }
        
        
    }
    
    public void gestionarEventos() {
        escena.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evento) {
                switch (evento.getCode().toString()) {
                    case "RIGHT": derecha = true; break;
                    case "LEFT": izquierda = true; break;
                    case "UP": arriba = true; break;
                    case "DOWN": abajo = true; break;
                    // En KeyPressed
                    case "A": teclaParry = true; break;
                    case "C": 
                        teclaSuper = true; // Solo activamos la bandera
                        break;
                   
                }
            }			
        });
        
        escena.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evento) {
                switch (evento.getCode().toString()) {
                    case "RIGHT": derecha = false; break;
                    case "LEFT": izquierda = false; break;
                    case "UP": arriba = false; break;
                    case "DOWN": abajo = false; break;
                    case "X": // El disparo se registra al soltar o presionar x (evita ráfagas incontrolables)
                        if (!juegoTerminado) {
                            proyectiles.add(jugador.disparar());
                            sonidoDisparo.play(); // Reproducir el sonido de disparo
                        }
                        break;
                    // En KeyReleased
                    case "A": teclaParry = false; break;
                    case "C": 
    // Ahora sí, si la bandera estaba activa y tienes las 5 cargas:
    if (teclaSuper && jugador.getCargasSuper() == 3 && !juegoTerminado) {
        proyectiles.add(jugador.dispararSuper());
        jugador.resetearSuper();
        sonidoSuperRayo.play(); // El sonido del rayo gigante
    }
    teclaSuper = false; // Reseteamos la bandera siempre al soltar
    break;
                    
        
                }
            }
        });
    }
    
    public void cicloJuego() {
        long tiempoInicial = System.nanoTime();
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long tiempoActualNanoSegundos) {
                double t = (tiempoActualNanoSegundos - tiempoInicial) / 1000000000.0;
                
                if (!juegoTerminado) {
                    actualizar(t);
                }
                pintar();
                
                // Evaluar Fin del Juego
                if (jugador.getVidas() <= 0 && !juegoTerminado) {
                    juegoTerminado = true;
                    animationTimer.stop(); // Detenemos el loop
                    procesarFinDeJuego();  // Llamamos a la lógica de puntos
                }
            }
        };
        animationTimer.start();
    }
    
    
    public void actualizar(double t) {
        jugador.mover();
        jugador.actualizarAnimacion(); // Actualizar animación del propulsor
        
        // ==========================================
        // 1. APARICIÓN DE ENEMIGOS
        // ==========================================
        if (t - tiempoUltimoEnemigo > 2.0) {
            oleada++;
            if (oleada % 5 == 0) enemigos.add(new EnemigoPesado(ANCHO_VENTANA / 2 - 30, -60, jugador));
            else if (oleada % 3 == 0) enemigos.add(new EnemigoCazador(Math.random() * (ANCHO_VENTANA - 40), -40, jugador));
            else enemigos.add(new EnemigoKamikaze(Math.random() * (ANCHO_VENTANA - 40), -40));
            tiempoUltimoEnemigo = t;
        }

        // ==========================================
        // 2. MOVIMIENTO DEL FONDO (SCROLL)
        // ==========================================
        if (empujandoMapa) {
            int velocidadScroll = 5;
            for (Tile estrella : fondo) {
                estrella.setY(estrella.getY() + velocidadScroll);
                if (estrella.getY() > ALTO_VENTANA) {
                    estrella.setY(-5);
                    estrella.setX((int)(Math.random() * ANCHO_VENTANA));
                }
            }
            for (Enemigo e : enemigos) {
                e.setY(e.getY() + velocidadScroll);
            }
            for (Particula p : particulas) {
                p.setY(p.getY() + velocidadScroll);
            }
        }

        // ==========================================
        // 3. MOVER PROYECTILES DEL JUGADOR
        // ==========================================
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            Proyectil p = proyectiles.get(i);
            p.mover();
            if (!p.isActivo()) proyectiles.remove(i);
        }

        // ==========================================
        // 4. BUCLE PRINCIPAL DE ENEMIGOS
        // ==========================================
        for (int i = enemigos.size() - 1; i >= 0; i--) {
            Enemigo e = enemigos.get(i);
            e.mover();
            
            Proyectil laserMalo = e.dispararEnemigo();
            if (laserMalo != null) proyectilesEnemigos.add(laserMalo);
            
            // Colisión Enemigo vs Jugador
            if (e.obtenerRectangulo().intersects(jugador.obtenerRectangulo().getBoundsInLocal())) {
                e.setActivo(false);
                jugador.perderVida();
                sonidoDanio.play();
                generarExplosion(jugador.getX(), jugador.getY(), Color.CYAN);
                jugador.resetearPosicion();
            }

            // Colisión Balas del Jugador vs Enemigo
            for (int j = proyectiles.size() - 1; j >= 0; j--) {
                Proyectil p = proyectiles.get(j);
                if (p.obtenerRectangulo().intersects(e.obtenerRectangulo().getBoundsInLocal())) {
                    
                    // ¡AQUÍ ESTÁ LA MAGIA DEL SÚPER RAYO QUE ATRAVIESA!
                    if (p instanceof SuperRayo) {
                        e.recibirDano(100); // Daño brutal
                        // No lo desactivamos para que siga cruzando la pantalla
                    } else {
                        e.recibirDano(1);   // Daño normal
                        p.setActivo(false); // La bala normal sí se destruye
                    }
                    
                    if (!e.isActivo()) {
                        jugador.sumarPuntos(e.getPuntosOtorgados());
                        listaPuntosFlotantes.add(new PuntosFlotantes("+" + e.getPuntosOtorgados(), e.getX(), e.getY()));
                        generarExplosion(e.getX(), e.getY(), e.getColorExplosion());
                        sonidoExplosion.play();
                    }
                }
            }
            if (!e.isActivo()) enemigos.remove(i);
        } // <-- AQUÍ SE CIERRA CORRECTAMENTE EL BUCLE DE ENEMIGOS

        // ==========================================
        // 5. LÓGICA DE ESTRELLAS PARRY
        // ==========================================
        if (t - tiempoUltimaEstrellaParry >= 6.0 && jugador.getCargasSuper() < 5) {
            estrellasParry.add(new EstrellaParry(Math.random() * (ANCHO_VENTANA - 40), -40));
            tiempoUltimaEstrellaParry = t;
        }

        for (int k = estrellasParry.size() - 1; k >= 0; k--) {
            EstrellaParry ep = estrellasParry.get(k); 
            
            if (empujandoMapa) ep.setY(ep.getY() + 5);
            ep.mover();

            // Sweet Spot: Centro exacto
            if (ep.obtenerRectangulo().intersects(jugador.obtenerRectanguloParry().getBoundsInLocal())) {
                if (teclaParry) {
                    jugador.sumarCargaSuper();
                    ep.setActivo(false);
                    teclaParry = false; 
                    listaPuntosFlotantes.add(new PuntosFlotantes("¡PERFECTO!", jugador.getX(), jugador.getY()));
                    if (jugador.getCargasSuper() == 3) sonidoParryLleno.play(); 
                    else sonidoParryNormal.play(); 
                } else {
                    jugador.perderVida();
                    sonidoDanio.play();
                    generarExplosion(jugador.getX(), jugador.getY(), Color.MAGENTA);
                    jugador.resetearPosicion();
                    ep.setActivo(false);
                }
            } 
            // Borde Exterior (Micro-escudo)
            else if (ep.obtenerRectangulo().intersects(jugador.obtenerRectangulo().getBoundsInLocal())) {
                if (!teclaParry) {
                    jugador.perderVida();
                    sonidoDanio.play();
                    generarExplosion(jugador.getX(), jugador.getY(), Color.MAGENTA);
                    jugador.resetearPosicion();
                    ep.setActivo(false);
                }
            }

            if (!ep.isActivo()) estrellasParry.remove(k);
        }
        
        // ==========================================
        // 6. METEORITOS INDESTRUCTIBLES
        // ==========================================
        if (t - tiempoUltimoMeteorito >= 7.0) {
            int patronActual = contadorMeteoritos % 4;
            double xAparicion = 0;
            switch (patronActual) {
                case 0: xAparicion = ANCHO_VENTANA / 2; break; 
                case 1: xAparicion = Math.random() * (ANCHO_VENTANA - 50); break; 
                case 2: xAparicion = Math.random() * (ANCHO_VENTANA / 2); break; 
                case 3: xAparicion = (ANCHO_VENTANA / 2) + Math.random() * (ANCHO_VENTANA / 2 - 50); break; 
            }
            obstaculos.add(new Meteorito(xAparicion, -60, patronActual));
            tiempoUltimoMeteorito = t; 
            contadorMeteoritos++;
        }

        for (int i = obstaculos.size() - 1; i >= 0; i--) {
            Meteorito m = obstaculos.get(i);
            if (empujandoMapa) m.setY(m.getY() + 5);
            m.mover();
            
            if (m.obtenerRectangulo().intersects(jugador.obtenerRectangulo().getBoundsInLocal())) {
                jugador.perderVida();
                sonidoDanio.play();
                generarExplosion(jugador.getX(), jugador.getY(), Color.ORANGE);
                jugador.resetearPosicion();
            }
            if (!m.isActivo()) obstaculos.remove(i);
        }

        // ==========================================
        // 7. LIMPIEZA DE EFECTOS VISUALES
        // ==========================================
        for (int i = particulas.size() - 1; i >= 0; i--) {
            Particula p = particulas.get(i);
            p.actualizar();
            if (p.estaDesvanecida()) particulas.remove(i);
        }
        
        for (int i = listaPuntosFlotantes.size() - 1; i >= 0; i--) {
            PuntosFlotantes p = listaPuntosFlotantes.get(i);
            p.actualizar();
            if (!p.isActivo()) listaPuntosFlotantes.remove(i);
        }
    }
    // Método que cumple el requerimiento del Cuadro de Diálogo y Archivo de Texto
    private void procesarFinDeJuego() {
        if (musicaJuego != null) {
            musicaJuego.stop();
        }
        GestorPuntuaciones gestor = new GestorPuntuaciones();
        int puntosActuales = jugador.getPuntuacion();
        
        // Ejecutamos la ventana de diálogo en el hilo principal de JavaFX
        Platform.runLater(() -> {
            if (gestor.esTop10(puntosActuales)) {
                TextInputDialog dialogo = new TextInputDialog("Jugador");
                dialogo.setTitle("¡Nuevo Récord!");
                dialogo.setHeaderText("¡Has entrado al Top 10 con " + puntosActuales + " puntos!");
                dialogo.setContentText("Ingresa tu nombre:");

                Optional<String> resultado = dialogo.showAndWait();
                if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
                    gestor.guardarPuntuacion(resultado.get().trim(), puntosActuales);
                }
            }
            
            // Mostrar la tabla de posiciones
            mostrarTop10(gestor);
            // NUEVO: Mostrar los botones después de cerrar la alerta
            menuGameOver.setVisible(true);
        });
    }

    private void mostrarTop10(GestorPuntuaciones gestor) {
        ArrayList<GestorPuntuaciones.Registro> top10 = gestor.leerPuntuaciones();
        
        // 1. Crear una nueva ventana personalizada
        javafx.stage.Stage retroStage = new javafx.stage.Stage();
        // Le quitamos los típicos bordes y botones de cerrar de Windows
        retroStage.initStyle(javafx.stage.StageStyle.UNDECORATED); 
        // Obligamos al jugador a interactuar con esta ventana antes de seguir
        retroStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); 
        
        // 2. Contenedor principal con fondo negro y borde Cyan
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black; -fx-border-color: #00ffff; -fx-border-width: 4px; -fx-padding: 40;");
        
        // 3. Título Brillante
        Label titulo = new Label("HIGH SCORES");
        titulo.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 45));
        titulo.setTextFill(Color.web("#ff0000")); // Rojo neón
        
        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setColor(Color.web("#ff5555"));
        glow.setRadius(15);
        titulo.setEffect(glow);
        
        // 4. Lista de puntuaciones
        VBox listaScores = new VBox(10);
        listaScores.setAlignment(Pos.CENTER);
        
        int posicion = 1;
        for (GestorPuntuaciones.Registro r : top10) {
            // Formato Arcade: " 1. Fer             009000"
            // %2d = número de 2 dígitos, %-15s = texto alineado a la izquierda con 15 espacios, %06d = número relleno con 6 ceros
            String textoFila = String.format("%2d. %-15s %06d", posicion, r.nombre, r.puntos);
            Label fila = new Label(textoFila);
            fila.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
            
            // Coloreamos el podio para que resalte
            if (posicion == 1) fila.setTextFill(Color.web("#00ffff")); // 1ro Cyan
            else if (posicion <= 3) fila.setTextFill(Color.web("#ffff00")); // 2do y 3ro Amarillo
            else fila.setTextFill(Color.web("#ffffff")); // Demás en Blanco
            
            listaScores.getChildren().add(fila);
            posicion++;
        }
        
        // 5. Botón para salir de la tabla
        Button btnCerrar = new Button("> CLOSE <");
        btnCerrar.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #ff0000; " +
            "-fx-font-family: 'Courier New'; " +
            "-fx-font-size: 26px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
        btnCerrar.setOnAction(e -> retroStage.close()); // Cierra esta ventanita
        
        layout.getChildren().addAll(titulo, listaScores, btnCerrar);
        
        Scene scene = new Scene(layout);
        retroStage.setScene(scene);
        
        // Mostrar la ventana y esperar
        retroStage.showAndWait();
    }

    

    // Modifica este método para que reciba el parámetro 'color'
    private void generarExplosion(double x, double y, Color color) {
        for (int i = 0; i < 20; i++) {
            // Ahora usamos la variable 'color' que nos envían
            particulas.add(new Particula(x + 20, y + 20, color));
        }
    }

    // Método para resetear todo y volver a jugar
    private void reiniciarJuego() {
        tiempoUltimoMeteorito = 0;
        contadorMeteoritos = 0;
        obstaculos.clear();
        // 1. Ocultar el menú de botones
        menuGameOver.setVisible(false);

        // 2. Resetear al jugador (le damos una nueva nave intacta)
        
        jugador = new Jugador(ANCHO_VENTANA / 2, ALTO_VENTANA - 100, "NaveEstatica1", 5);
        

        // 3. Limpiar la pantalla de enemigos, balas y explosiones anteriores
        proyectiles.clear();
        enemigos.clear();
        particulas.clear();

        // 4. Resetear los contadores de oleadas y tiempo
        oleada = 0;
        tiempoUltimoEnemigo = 0;
        // ¡Eliminamos tiempoUltimoDisparo de aquí porque ya no existe!

        // --- NUEVO: APAGAR TODAS LAS TECLAS FANTASMA ---
        arriba = false;
        abajo = false;
        izquierda = false;
        derecha = false;
        
        // 5. Quitar la bandera de Game Over
        juegoTerminado = false;

        // 6. ¡Volver a arrancar el Game Loop!
        // ¡NUEVO! Arrancar la música de nuevo
        indiceCancionActual = 0;
        reproducirSiguienteCancion();
        cicloJuego();
    }
    private void reproducirSiguienteCancion() {
        // 1. Si ya había una canción sonando, se detiene y se libera para evitar problemas de memoria
        if (musicaJuego != null) {
            musicaJuego.stop();
            musicaJuego.dispose(); 
        }

        try {
            // 2. Cargamos la canción según el número de índice en el que vamos
            Media media = new Media(getClass().getResource(playlist[indiceCancionActual]).toExternalForm());
            musicaJuego = new MediaPlayer(media);

            //esto era una implementación anterior para cambiar la cancion, pero solo dejé una al final. en todo caso lo dejo por si acaso.

            musicaJuego.setOnEndOfMedia(() -> {
                indiceCancionActual++; // Avanzamos al siguiente número
                
                // Si llegamos al final de la lista, volvemos a empezar desde la cero
                if (indiceCancionActual >= playlist.length) {
                    indiceCancionActual = 0;
                }
                
                // Nos volvemos a llamar a nosotros mismos para tocar la nueva canción
                reproducirSiguienteCancion(); 
            });

            // 4. ¡Play!
            musicaJuego.play();
            
        } catch (Exception e) {
            System.out.println("No se encontró la pista: " + playlist[indiceCancionActual]);
        }
    }
}