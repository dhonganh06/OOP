package arkanoid;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
    public class Sound {
        private static final arkanoid.Sound instance = new arkanoid.Sound();
        private Sound() {
            loadBackgroundMusic("/audio/Sound.mp3");
            loadSoundEffect("bounce", "/audio/ball_sound.wav");
            loadSoundEffect("powerup", "/audio/powerup.wav");
            loadSoundEffect("gameover", "/audio/game-over.mp3");
            loadSoundEffect("gamewin", "/audio/win_sound.wav");

        }
        public static arkanoid.Sound getInstance() {
            return instance;
        }
        private MediaPlayer backgroundMusicPlayer;
        private HashMap<String, AudioClip> soundEffects = new HashMap<>();


        public void playMusic() {
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.play();
            }
        }

        public void stopMusic() {
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.stop();
            }
        }

        public void playSound(String name) {
            AudioClip sound = soundEffects.get(name);
            if (sound != null) {
                sound.play();
            } else {
                System.err.println("Không tìm thấy SFX tên: " + name);
            }
        }
        private void loadBackgroundMusic(String filePath) {
            try {
                URL resourceUrl = getClass().getResource(filePath);
                if (resourceUrl == null) {
                    System.err.println("Không tìm thấy nhạc nền: " + filePath);
                    return;
                }
                Media sound = new Media(resourceUrl.toExternalForm());
                backgroundMusicPlayer = new MediaPlayer(sound);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusicPlayer.setVolume(0.5);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void loadSoundEffect(String name, String filePath) {
            try {
                URL resourceUrl = getClass().getResource(filePath);
                if (resourceUrl == null) {
                    System.err.println("Không tìm thấy SFX: " + filePath);
                    return;
                }
                AudioClip sound = new AudioClip(resourceUrl.toExternalForm());
                soundEffects.put(name, sound);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



