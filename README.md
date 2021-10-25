# VideoCompress
# 一款快速、高效的视频压缩工具。A fast and efficient video compression tool.
[![](https://jitpack.io/v/ITxiaoguang/VideoCompress.svg)](https://jitpack.io/#ITxiaoguang/VideoCompress)


### 自带`VideoCompressDialog`弹窗，用法超级简单：

```java
String inputFilePath = "输入你需要压缩的视频地址";
VideoCompressDialog dialog = new VideoCompressDialog(this);
dialog.setInputPath(inputFilePath);
dialog.setCallback(new VideoCompressDialog.OnCallback() {

    @Override
    public void success(String successPath) {
        Toast.makeText(MainActivity.this, "压缩成功，path： " + successPath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(int code, String message) {
        Toast.makeText(MainActivity.this, "压缩失败，message： " + message, Toast.LENGTH_SHORT).show();
    }
});
dialog.show();
```

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

[![](https://jitpack.io/v/ITxiaoguang/VideoCompress.svg)](https://jitpack.io/#ITxiaoguang/VideoCompress)

``` gradle
dependencies {
       implementation 'com.github.ITxiaoguang:VideoCompress:xxx'
}
```

#### 3.加上Android10.0 需要在`AndroidManifest.xml`文件中`application`出加上
```xml
<application
  android:requestLegacyExternalStorage="true"
  ...
  />
```

#### 4.`AndroidManifest.xml`加上读写权限，代码里并请求读写权限
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

#### 5.请求读写权限
```java
private void initPermission() {
    //检查相机权限
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有相机权限
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_CAMERA);
        } else {
            // todo 打开文件选择器
        }
    } else {
        // todo 打开文件选择器
    }
}
```

#### 6.权限回调
```java
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
        case REQUEST_CODE_PERMISSION_CAMERA:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 授予权限
                // todo 用户同意了权限申请 打开文件选择器
            } else {
                // Permission Denied 权限被拒绝
                Toast.makeText(this, "初始化文件选择器失败,读取文件权限被拒绝", Toast.LENGTH_SHORT).show();
            }
            break;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```

自带`VideoCompressDialog`弹窗，用法超级简单：

```java
String inputFilePath = "输入你需要压缩的视频地址";
VideoCompressDialog dialog = new VideoCompressDialog(this);
dialog.setInputPath(inputFilePath);
dialog.setCallback(new VideoCompressDialog.OnCallback() {

    @Override
    public void success(String successPath) {
        Toast.makeText(MainActivity.this, "压缩成功，path： " + successPath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(int code, String message) {
        Toast.makeText(MainActivity.this, "压缩失败，message： " + message, Toast.LENGTH_SHORT).show();
    }
});
dialog.show();
```

# 选择文件

## 推荐使用文件选择器选择文件 --> [文件选择器](https://github.com/ITxiaoguang/FilePicker)

#### 1.选择文件
```java
private void filePicker() {
    String[] zips = {"zip", "rar"};
    String[] doc = {"doc", "docx"};
    String[] ppt = {"ppt", "pptx"};
    String[] pdf = {"pdf"};
    String[] txt = {"txt"};
    String[] apk = {"apk"};
    String[] xls = {"xls", "xlsx"};
    String[] music = {"m3u", "m4a", "m4b", "m4p", "ogg", "wma", "wmv", "ogg", "rmvb", "mp2", "mp3", "aac", "awb", "amr", "mka"};
    FilePickerBuilder.getInstance()
            .setMaxCount(1)
            .enableCameraSupport(false)
            .showPic(true)
            .showVideo(true)
            .enableDocSupport(false)
            .addFileSupport("Word", doc, R.drawable.ic_file_word)
            .addFileSupport("压缩包", zips, R.drawable.ic_file_zip)
            .addFileSupport("PDF", pdf, R.drawable.ic_file_pdf)
            .addFileSupport("Txt文本", txt, R.drawable.ic_file_txt)
            .addFileSupport("PPT", ppt, R.drawable.ic_file_ppt)
            .addFileSupport("安装包", apk, R.drawable.ic_file_zip)
            .addFileSupport("Excel表格", xls, R.drawable.ic_file_excel)
            .addFileSupport("音乐", music, R.drawable.ic_file_music)
            .setActivityTitle("请选择文件")
            .sortDocumentsBy(SortingTypes.name)
            .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .pickFile(this, REQUEST_CODE_FILE);
}
```

#### 2.回调
```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == REQUEST_CODE_FILE) {//选择文件
            ArrayList<String> filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
            String path = filePaths.get(0);
//                Toast.makeText(this, "地址： " + path, Toast.LENGTH_SHORT).show();
            compress(path);
        }
    }
}
```
