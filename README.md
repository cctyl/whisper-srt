
# whisper批量生成字幕文件

## 参考
命令来自：[whisper.cpp](https://github.com/ggerganov/whisper.cpp) 

本软件只是对原命令的封装

## 硬件要求
使用的是base模型，至少需要1GB的显存

## 使用
下载压缩包，解压，点击运行，选择视频文件夹。启动。

随后会把生成的srt字幕文件放到视频文件的旁边，使用potplayer等视频播放器直接播放即可

输出日志中，可能会存在？？？等情况，但是不影响最终生成
## 环境要求
不需要jdk，但是需要安装 ffmpeg，并配置到Path环境变量中