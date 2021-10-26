package com.xiaoguang.widget.videocompress.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xiaoguang.widget.videocompress.R;
import com.xiaoguang.widget.videocompress.core.VideoCompress;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 视频压缩工具
 * use：
 * VideoCompressDialog dialog = new VideoCompressDialog(getActivity());
 * dialog.setInputPath(filePaths.get(0));
 * dialog.setCallback(new VideoCompressDialog.OnCallback() {
 *
 * @Override public void success(String successPath) {
 * }
 * @Override public void fail(int code, String message) {
 * }
 * });
 * dialog.show();
 * hxg 2021/3/31 17:22 qq:929842234
 */
public class VideoCompressDialog extends Dialog {

    private OnCallback callback;

    public interface OnCallback {
        void success(String successPath);

        void fail(int code, String message);
    }

    private Context context;

    private TextView tv_des;
    private TextView tv_progress;
    private TextView tv_cancel;

    private String inputPath;
    private String outputPath;
    private VideoCompress.VideoCompressTask videoCompressTask;

    public VideoCompressDialog(@NonNull Context context) {
        super(context, R.style.dialog_default_style);
        this.context = context;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setCallback(OnCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_media_loading);

        tv_des = findViewById(R.id.tv_des);
        tv_progress = findViewById(R.id.tv_progress);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> progressDialog(0, "compression cancel"));

        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawableResource(R.color.transparent);

        initOutputPath();

        compress();
    }

    /**
     * 默认输出路径  包名/cache/videoCompress/compressedMp4.mp4
     */
    private void initOutputPath() {
        if (TextUtils.isEmpty(outputPath)) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + context.getPackageName();
            mkdir(path);
            mkdir(path + "/cache");
            mkdir(path + "/cache/videoCompress");
            outputPath = path + "/cache/videoCompress" + "/compressedMp4.mp4";
        }
    }

    public static void mkdir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 压缩
     */
    private void compress() {
        videoCompressTask = VideoCompress.compressVideoLow(inputPath, outputPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                progressDialog(1, "compression success");
            }

            @Override
            public void onFail() {
                progressDialog(-1, "compression error");
            }

            @Override
            public void onProgress(float percent) {
                Log.i("VideoCompressDialog", "percent " + percent);
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String percentString = decimalFormat.format(percent);
                setProgressDialog(percentString);
            }
        });
    }

    @Override
    public void cancel() {
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != videoCompressTask) {
            videoCompressTask.cancel(true);
        }
    }

    private void progressDialog(int code, String s) {
        Log.i("VideoCompressDialog", "code " + code + "  ,msg " + s);
        if (null != callback) {
            switch (code) {
                case -1:
                    callback.fail(-1, this.getContext().getString(R.string.compress_error));
                    break;
                case 0:
                    callback.fail(0, this.getContext().getString(R.string.compress_cancel));
                    break;
                case 1:
                    callback.success(outputPath);
                    break;
            }
        }
        dismiss();
    }

    private void setProgressDialog(String percentString) {
        tv_progress.setText(percentString);
    }
}