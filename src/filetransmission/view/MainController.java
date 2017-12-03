package filetransmission.view;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

public class MainController implements Initializable {

    @FXML
    Button fileSelectBtn;

    @FXML
    TextField pathField;

    @FXML
    Button sendBtn;

    @FXML
    ProgressBar progressBar;

    @FXML
    Label stateLabel;

    private String currentFilePath = null;
    private String currentFileExtension = null;
    private long currentFileSize = 0;

    private ServerSocket serverSocket = null;

    private SimpleDoubleProperty progressPercent;

    public void initControllers() {

        fileSelectBtn.setOnAction((e) -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Дамжуулах файлаа сонгох...");
            File file = chooser.showOpenDialog(new Stage());
            pathField.setText(file.getAbsolutePath());
            this.currentFilePath = file.getAbsolutePath();
            this.currentFileExtension = FilenameUtils.getExtension(file.getAbsolutePath());
            this.currentFileSize = file.length();
        });

        sendBtn.setOnAction((e) -> {
            stateLabel.setText("Процесс: ");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bufferFile();
                }
            }).start();
        });

        progressBar.progressProperty().bind(progressPercent);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.progressPercent = new SimpleDoubleProperty(0);
        stateLabel.setText("Төлөв: ");
        initSocket();
        initControllers();
    }

    public void bufferFile() {
        try {
            InputStream is = new DataInputStream(new FileInputStream(this.currentFilePath));
            BufferedInputStream bufferedStream = new BufferedInputStream(is);
            double percent = 0;

            Socket server = serverSocket.accept();
            DataOutputStream out = new DataOutputStream(server.getOutputStream());

            out.writeUTF(FilenameUtils.getBaseName(currentFilePath));
            out.writeUTF(currentFileExtension);
            out.writeLong(currentFileSize);

            while (is.available() > 0) {
                out.write(is.read());
                percent = 1 - (double) is.available() / currentFileSize;

                this.progressPercent.set(percent);
            }

            out.close();
            server.close();

            bufferedStream.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found exception: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Found IO exception: " + ex.getMessage());
        }
    }

    public void initSocket() {
        try {
            serverSocket = new ServerSocket(8001);
        } catch (IOException ex) {
            System.out.println("IOException : " + ex.getMessage());
        }
    }

}
