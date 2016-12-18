/*************************************************
 *对录音的封装
 * 包括录音/保存/播放
 ************************************************/
package com.example.dell.jiemian;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dell on 2016/5/16.
 */
public class  SoundRecorder {

    MediaRecorder mRecorder;

    boolean isRecording;//是否正在录音 （暂无使用。留作扩展）

    public String str;
    /************************************************
     * 开始录制
     ***********************************************/
    public  void startRecording() {
        mRecorder =  new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        str=newFileName();
        mRecorder.setOutputFile(str);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();

    }
    /************************************************
     * 停止录制
     ***********************************************/
    public  void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder =  null;
    }
    /************************************************
     * 保存文件
     ***********************************************/
    public String newFileName() {
        String mFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        String s =  new SimpleDateFormat("yyyy-MM-dd hhmmss")
                .format( new Date());
        return mFileName += "/rcd_" + s + ".mp3" ;
    }
}
/************************************************
 * 播放类，调试时使用（未使用）
 ***********************************************/
class  SoundPlayer {

    MediaPlayer mPlayer;

    public  void startPlaying(String fileName) {
        mPlayer =  new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void stopPlaying() {
        mPlayer.release();
        mPlayer =  null;
    }
}
