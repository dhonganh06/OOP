package arkanoid;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
    public class Sound {
        // --- Singleton Pattern ---
        // 1. Tạo một thể hiện tĩnh (static) duy nhất của chính nó
        private static final arkanoid.Sound instance = new arkanoid.Sound();
        // 2. Tạo một constructor "private" để không ai khác tạo được
        private Sound() {
            // Tải nhạc nền ngay khi game khởi động
            loadBackgroundMusic("/audio/Sound.mp3");
            // Tải trước các hiệu ứng âm thanh (SFX)
            loadSoundEffect("bounce", "/audio/ball_sound.wav");
            loadSoundEffect("powerup", "/audio/powerup.wav");
            loadSoundEffect("gameover", "/audio/game-over.mp3");
            loadSoundEffect("gamewin", "/audio/win_sound.wav");

        }
        // 3. Tạo một phương thức "public" để các lớp khác có thể truy cập
        public static arkanoid.Sound getInstance() {
            return instance;
        }
        // --- End Singleton Pattern ---
        private MediaPlayer backgroundMusicPlayer;
        // Dùng HashMap để lưu nhiều hiệu ứng âm thanh
        private HashMap<String, AudioClip> soundEffects = new HashMap<>();

        // --- Các phương thức public để điều khiển ---

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
        // --- Các phương thức private để tải file ---
        private void loadBackgroundMusic(String filePath) {
            try {
                URL resourceUrl = getClass().getResource(filePath);
                if (resourceUrl == null) {
                    System.err.println("Không tìm thấy nhạc nền: " + filePath);
                    return;
                }
                Media sound = new Media(resourceUrl.toExternalForm());
                backgroundMusicPlayer = new MediaPlayer(sound);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô tận
                backgroundMusicPlayer.setVolume(0.5); // 50% âm lượng

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
                soundEffects.put(name, sound); // Lưu lại để dùng sau
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



