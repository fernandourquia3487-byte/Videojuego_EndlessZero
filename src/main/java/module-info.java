module com.unah {

    requires transitive javafx.graphics;
    
    // --- ¡AGREGA ESTAS DOS LÍNEAS PARA SOLUCIONAR LOS ERRORES! ---
    requires javafx.media;    // Permiso para reproducir AudioClip y MediaPlayer
    requires javafx.controls;

    opens com.unah to javafx.fxml;
    exports com.unah;
}
