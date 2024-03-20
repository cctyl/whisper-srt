package io.github.cctyl;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class WhisperUtilTest {

    @Test
    void runCmd() throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder("./cli/main.exe", "-h");

        Process process = processBuilder.start();

        // 获取进程的输入流
        InputStream inputStream = process.getInputStream();

        // 获取进程的错误输出流
        InputStream errorStream = process.getErrorStream();

        // 创建输入流的读取器
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));

        // 创建错误输出流的读取器
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

        // 读取输入流的输出
        String line;
        while ((line = inputReader.readLine()) != null) {
            System.out.println(line);
        }

        // 读取错误输出流的输出
        while ((line = errorReader.readLine()) != null) {
            System.err.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();

        // 输出命令执行结果
        System.out.println("命令执行完成，退出码：" + exitCode);


    }


}