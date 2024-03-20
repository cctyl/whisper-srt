package io.github.cctyl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;

public class FxWindow extends Application {


    private StopbilityThread<File> thread;

    public void start(Stage stage) {

        stage.setWidth(500);
        stage.setHeight(300);
        stage.setResizable(false);

        stage.setTitle("whisper批量生成字幕");

        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Scene scene = new Scene(vBox);

        Button btOpen = new Button("选择视频文件夹");
        btOpen.setPrefWidth(200);
        btOpen.setPrefHeight(100);


        Button stop = new Button("暂停");
        stop.setPrefWidth(100);
        stop.setPrefHeight(100);

        HBox.setMargin(stop,new Insets(0,0,0,20));

        hBox.getChildren().addAll(btOpen,stop);
        hBox.setAlignment(Pos.CENTER);

        TextArea console = new TextArea();
        LogTool.print(console);

        LogTool.log("github地址：https://github.com/cctyl/whisper-srt");

        vBox.getChildren().addAll(hBox, console);
        vBox.setAlignment(Pos.CENTER);

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("打开视频文件夹");
        btOpen.setOnMouseClicked(e -> {
            try {
                File dir = chooser.showDialog(stage);
                thread = WhisperUtil.start(dir.getAbsolutePath());
            } catch (NullPointerException ex) {
                LogTool.log("打开文件夹错误");
                ex.printStackTrace();
            }

        });
        stop.setOnMouseClicked(event -> {
            thread.stop();
            LogTool.log("解码完成");
            LogTool.log("github地址：https://github.com/cctyl/whisper-srt");
        });

        stage.setOnCloseRequest(event -> {
            LogTool.shutdown();
            System.exit(0);
        });
        stage.setScene(scene);
        stage.show();
    }





}