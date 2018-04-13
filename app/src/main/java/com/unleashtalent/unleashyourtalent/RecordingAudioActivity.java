package com.unleashtalent.unleashyourtalent;

import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.util.Log.v;

public class RecordingAudioActivity extends AppCompatActivity {

    private Button buttonRecordAudio, buttonStopRecording, buttonPlayAudio, buttonPauseAudio;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private String recordingFilePath = null;
    private static final int RequestPermissionCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_audio);

        // get reference to buttons of the UI
        buttonRecordAudio = (Button) findViewById(R.id.button_record_audio);
        buttonPlayAudio = (Button) findViewById(R.id.button_play_audio);
        buttonPauseAudio = (Button) findViewById(R.id.button_pause_audio);
        buttonStopRecording = (Button) findViewById(R.id.button_stop_recording);

        // disable stop recording, play and pause buttons
        buttonStopRecording.setEnabled(false);
        buttonPlayAudio.setEnabled(false);
        buttonPauseAudio.setEnabled(false);

        if (!checkPermission()) {
            requestPermission();
        }

        buttonRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    MediaRecorderReady();
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    releaseMediaPlayer();

                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // disable recording button
                buttonRecordAudio.setEnabled(false);
                // enable stop recording button
                buttonStopRecording.setEnabled(true);

                Toast.makeText(RecordingAudioActivity.this, "Recording started", Toast.LENGTH_SHORT).show();


            }

        });

        buttonStopRecording.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    // enable recording button
                    buttonRecordAudio.setEnabled(true);
                    // disable stop recording button
                    buttonStopRecording.setEnabled(false);
                    // enable play button
                    buttonPlayAudio.setEnabled(true);
                    // enable pause button
                    buttonPauseAudio.setEnabled(true);

                    Toast.makeText(RecordingAudioActivity.this, "Recording Stopped", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(recordingFilePath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        buttonPauseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    /**
     * this method to release media player instance
     */
    private void releaseMediaPlayer() {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    /**
     * this method initializes Media Recorder
     */
    private void MediaRecorderReady() {
        try {
            if (checkPermission()) {
                // 1- make instance of media recorder class
                mediaRecorder = new MediaRecorder();
                // 2- set audio source as MIC (MICROPHONE)
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 3- set output format
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                // 4- set encoding format
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                // 5- set output file
                recordingFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "recording.3gp";
                mediaRecorder.setOutputFile(recordingFilePath);
            } else {
                requestPermission();
            }
        } catch (Exception ex) {
            Log.v("MediaRecord Initialize", ex.getMessage());
        }

    }


    /**
     * Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordingAudioActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public void setRequestPermissionCode(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecordingAudioActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordingAudioActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}
