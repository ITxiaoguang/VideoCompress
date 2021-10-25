# VideoCompress
# 一款快速、高效的视频压缩工具。A fast and efficient video compression tool.

### 用FFmpeg实现转码。

### 支持音频格式转换成mp3格式的工具（m3u8除外）。

## 如何添加
### Gradle添加：
#### 1.在Project的`build.gradle`中添加仓库地址

``` gradle
allprojects {
  repositories {
     ...
     maven { url "https://jitpack.io" }
  }
}
```

#### 2.在Module目录下的`build.gradle`中添加依赖


[![](https://www.jitpack.io/v/ITxiaoguang/TranscodeMP3.svg)](https://www.jitpack.io/#ITxiaoguang/TranscodeMP3)

``` gradle
dependencies {
       implementation 'com.github.ITxiaoguang:TranscodeMP3:xxx'
}
```

自带TranscodeMp3Dialog弹窗，用法超级简单：

```java
String inputFilePath = "输入你需要转码的音频地址";
String[] ignores = {"mp3"};// mp3 不转码
TranscodeMp3Dialog dialog = new TranscodeMp3Dialog(this);
dialog.setInputPath(inputFilePath);// 输入的文件地址
dialog.setIgnores(ignores);// 忽略的格式
dialog.setCallback(new TranscodeMp3Dialog.OnCallback() {
    // 转码成功  successPath：转码成功后地址
    @Override
    public void success(String successPath) {
        Toast.makeText(MainActivity.this, "成功，path： " + successPath, Toast.LENGTH_SHORT).show();
    }

    // 转码失败  code：失败代码 message：失败描述
    @Override
    public void fail(int code, String message) {
        Toast.makeText(MainActivity.this, "失败，message： " + message, Toast.LENGTH_SHORT).show();
    }
});
dialog.show();
```


更多ffmpeg命令转码，请看：[RxFFmpeg](https://github.com/microshow/RxFFmpeg)

