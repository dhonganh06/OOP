package arkanoid;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

// Lớp chính chạy game
public class ArkanoidGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Tạo canvas 800x600 để vẽ game
        Canvas canvas = new Canvas(800, 600);
        // Lấy GraphicsContext để vẽ lên canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Khởi tạo GameManager để quản lý logic game
        GameManager gameManager = new GameManager();

        // AnimationTimer: Tạo vòng lặp game, gọi update và render liên tục
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameManager.updateGame();
                gameManager.render(gc);
            }
        };

        // Tạo Scene chứa canvas và đặt kích thước 800x600
        Scene scene = new Scene(new Pane(canvas), 800, 600);
        // scene.setOnKeyPressed và setOnKeyReleased: Xử lý sự kiện bàn phím
        scene.setOnKeyPressed(e -> gameManager.handleInput(e.getCode(), true));
        scene.setOnKeyReleased(e -> gameManager.handleInput(e.getCode(), false));

        // primaryStage.setTitle: Đặt tiêu đề cửa sổ
        primaryStage.setTitle("Arkanoid Game");
        // primaryStage.setScene: Gán Scene cho Stage
        primaryStage.setScene(scene);
        // primaryStage.show: Hiển thị cửa sổ game
        primaryStage.show();
        // timer.start: Bắt đầu vòng lặp game
        timer.start();
    }

    // Hàm main để chạy ứng dụng JavaFX
    public static void main(String[] args) {
        // launch: Khởi chạy ứng dụng JavaFX
        launch(args);
    }
}
