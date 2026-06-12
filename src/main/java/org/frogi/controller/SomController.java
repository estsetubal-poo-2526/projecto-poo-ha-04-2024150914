package org.frogi.controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Objects;

public class SomController {
    private static SomController instance;

    private MediaPlayer musicaFundo;
    private AudioClip somComerGrilo;
    private AudioClip somPowerUp;
    private AudioClip somMorte;

    private double volumeMusica = 0.5; // Começa a 50%

    private SomController() {
        try {
            // Carrega Música de Fundo
            String musicaPath = Objects.requireNonNull(getClass().getResource("/audio/musica_fundo.mp3")).toExternalForm();
            Media media = new Media(musicaPath);
            musicaFundo = new MediaPlayer(media);
            musicaFundo.setCycleCount(MediaPlayer.INDEFINITE);
            musicaFundo.setVolume(volumeMusica);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ficheiros de áudio: " + e.getMessage());
        }

        // Carrega Efeitos Sonoros
        somComerGrilo = carregarAudio("/audio/comer_grilo.wav");
        somPowerUp    = carregarAudio("/audio/powerup.wav");
        somMorte      = carregarAudio("/audio/morte.wav");
    }

    public static SomController getInstance() {
        if (instance == null) {
            instance = new SomController();
        }
        return instance;
    }

    private AudioClip carregarAudio(String caminho) {
        try {
            return new AudioClip(Objects.requireNonNull(getClass().getResource(caminho)).toExternalForm());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void tocarMusicaFundo() {
        if (musicaFundo != null) musicaFundo.play();
    }

    public void pararMusica() {
        if (musicaFundo != null) {
            musicaFundo.stop();
            musicaFundo.dispose();
        }
    }

    public void tocarComerGrilo() {
        if (somComerGrilo != null) somComerGrilo.play();
    }

    public void tocarPowerUp() {
        if (somPowerUp != null) somPowerUp.play();
    }

    public void tocarMorte() {
        if (somMorte != null) somMorte.play();
    }

    // Configura o volume (recebe um valor entre 0.0 e 1.0)
    public void setVolumeMusica(double volume) {
        this.volumeMusica = Math.max(0.0, Math.min(1.0, volume));
        if (musicaFundo != null) {
            musicaFundo.setVolume(volume);
        }
    }

    public double getVolumeMusica(){
        return volumeMusica;
    }

}