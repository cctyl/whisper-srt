package io.github.cctyl;

import cn.hutool.core.io.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WhisperUtil {

    private static final List<String> ALLOW_SUFFIX = List.of(
            ".mp3", ".aac", ".mp4", ".flac", ".wav", ".mpg", ".flv", ".f4v", ".avi", ".wmv", ".rmvb", ".mov"
    );

    public static StopbilityThread<File> start(String path) {
        List<File> files = scanFiles(path);
        return new StopbilityThread<>(files, f -> {
            transcrib(f);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, (i) -> {
            LogTool.log("执行完毕,共转述" + i + "条数据");
        });
    }

    private static void transcrib(File videoFile) {
        String absolutePath = videoFile.getAbsolutePath();
        runCmd(absolutePath);
    }


    public static void runCmd(String videoFilePath) {

        try {

            // 构建命令
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "./cli/main.exe",
                    "-m", "./ggml-base.bin",
                    "-gpu",
                    "-f", videoFilePath,
                    "--language",  "zh",
                    "-osrt",
                    "--prompt",
                    "Whisper, when transcribing speech to text, please prioritize accurate punctuation. Ensure that each sentence is contained within a single subtitle segment for clarity. If a sentence is too long, break it at natural pauses in the speaker’s delivery to keep subtitles concise and easy for viewers to follow,以下是简体中文句子"


            );



            // 启动进程
            Process process = processBuilder.start();

            // 获取进程的输入流
            InputStream inputStream = process.getInputStream();

            // 获取进程的错误输出流
            InputStream errorStream = process.getErrorStream();

            printStream(inputStream);
            printStream(errorStream);


            // 等待命令执行完成
            int exitCode = 0;
            try {
                exitCode = process.waitFor();
            } catch (InterruptedException e) {
                process.destroyForcibly();
                e.printStackTrace();
            }


            // 输出命令执行结果
            LogTool.log("命令执行完成，退出码：" + exitCode);

        } catch (IOException  e) {
            e.printStackTrace();
        }

    }

    private static void printStream(InputStream inputStream) {
        CompletableFuture.runAsync(() -> {
            // 创建输入流的读取器
            BufferedReader inputReader = null;
            try {
                inputReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            // 读取输入流的输出
            String line;
            while (true) {
                try {
                    if ((line = inputReader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                LogTool.log(line);
            }

        });
    }


    private static List<File> scanFiles(String path) {
        return FileUtil.loopFiles(path, pathname -> {

            for (String allowSuffix : ALLOW_SUFFIX) {
                if (pathname.getName().endsWith(allowSuffix)) {
                    return true;
                }
            }
            return false;
        });
    }


}
