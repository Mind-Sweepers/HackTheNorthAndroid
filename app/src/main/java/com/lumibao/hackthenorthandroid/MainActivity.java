package com.lumibao.hackthenorthandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button btnVoice;
    private TextView txtTest;
    private TextView txtCode;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    private DatabaseReference mDatabaseReference;
    boolean toggle = false;
    private String uuid;

    private SpeechRecognizer getSpeechRecognizer() {
        if (mSpeechRecognizer == null) {
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

            mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d("onReadyForSpeech", "on READY");
                }

                @Override
                public void onBeginningOfSpeech() {
                    Log.d("onBeginningOfSpeech", "onBeginningOfSpeech");
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    Log.d("onBufferReceived", "onBufferReceived");
                }

                @Override
                public void onEndOfSpeech() {
                    Log.d("onEndOfSpeech", "onEndOfSpeech");
                }

                @Override
                public void onError(int error) {
                    String message;
                    boolean restart = true;
                    switch (error) {
                        case SpeechRecognizer.ERROR_AUDIO:
                            message = "Audio recording error";
                            break;
                        case SpeechRecognizer.ERROR_CLIENT:
                            message = "Client side error";
                            restart = false;
                            break;
                        case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                            message = "Insufficient permissions";
                            restart = false;
                            break;
                        case SpeechRecognizer.ERROR_NETWORK:
                            message = "Network error";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                            message = "Network timeout";
                            break;
                        case SpeechRecognizer.ERROR_NO_MATCH:
                            message = "No match";
                            break;
                        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                            message = "RecognitionService busy";
                            break;
                        case SpeechRecognizer.ERROR_SERVER:
                            message = "error from server";
                            break;
                        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                            message = "No speech input";
                            break;
                        default:
                            message = "Not recognised";
                            break;
                    }
                    Log.d("onError code:", error + " message: " + message);

                    startVoiceRead();
                }

                @Override
                public void onResults(Bundle results) {
                    Log.d("onResults", "onResults");
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                    if (matches != null) {
                        txtTest.setText(matches.get(0));
                        mDatabaseReference.child(uuid).push().setValue(matches.get(0));
                    }


                    startVoiceRead();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    Log.d("onPartialResults", "onPartialResults");
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    Log.d("onEvent", "onEvent");
                }
            });

        }
        return mSpeechRecognizer;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        txtTest = findViewById(R.id.txtTest);
        txtCode = findViewById(R.id.txtCode);
        generateString();

        btnVoice = findViewById(R.id.btnVoice);
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle = !toggle;

                if (toggle) {
                    btnVoice.setBackgroundColor(Color.RED);
                    startVoiceRead();
                } else {
                    btnVoice.setBackgroundColor(Color.GREEN);
                    stopVoiceRead();
                }
            }
        });
    }

    public void startVoiceRead() {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        getSpeechRecognizer().startListening(speechIntent);
    }

    public void stopVoiceRead() {
        btnVoice.setBackgroundColor(Color.GREEN);

        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) ||
                    !(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    public String generateString() {
        uuid = UUID.randomUUID().toString().substring(0,5);
        txtCode.setText(uuid);
        return uuid;
    }
//    void voicePrompt() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, 10);
//        } else {
//            Toast.makeText(this, "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch(requestCode) {
//            case 10:
//                if (resultCode == RESULT_OK && data != null) {
//                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                }
//                break;
//        }
//    }
}
