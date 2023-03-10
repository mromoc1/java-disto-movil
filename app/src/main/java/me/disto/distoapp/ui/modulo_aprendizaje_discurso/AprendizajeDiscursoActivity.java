package me.disto.distoapp.ui.modulo_aprendizaje_discurso;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.modulo_aprendizaje_lectura.AprendizajeLecturaTask;
import me.disto.distoapp.ui.utils.BaseActivity;

public class AprendizajeDiscursoActivity extends BaseActivity {

    //  UI
    ImageView botonIniciarDetener;
    TextView textoTiempoGrabacion;
    TextView textoEstado;
    TextView textoPregunta;
    //  UI

    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private Boolean estaGrabando = false;
    private String usuario = "useridx";
    private String contrasena = "test123";

    private byte[] audioBuffer;

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String[] preguntas = {"¿Qué te gusta hacer en tu tiempo libre?",
            "¿Cuál es tu opinión sobre la educación en línea?",
            "¿Qué planes tienes para tu futuro profesional?",
            "¿Qué te gustaría ser cuando seas mayor?",
            "¿Cuál es el lugar más interesante que has visitado y por qué?",
            "¿Qué aspectos te gustan de tu trabajo actual y cuáles te gustaría cambiar?",
            "¿¿Qué habilidades te gustaría aprender o mejorar?",
            "¿Como te ves en 5 años?",
            "¿Qué cambios positivos te gustaría ver en tu comunidad o en el mundo en general?"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendizaje_discurso);

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_discurso).setChecked(true);

        textoTiempoGrabacion = findViewById(R.id.timer_textview);
        botonIniciarDetener = findViewById(R.id.boton_iniciar_detener);
        textoEstado = findViewById(R.id.status_textview);
        textoPregunta = findViewById(R.id.texto_pregunta);

        int frequency = 44100; // Frecuencia de muestreo en Hz
        int channelConfiguration = AudioFormat.CHANNEL_IN_MONO; // Canal mono
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT; // Formato de codificación de audio
        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding); // Tamaño del búfer

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);


        if (!tienePermisos()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        }

        botonIniciarDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!estaGrabando) {
//                    iniciarGrabacion();
                    estaGrabando = true;
                    audioRecord.startRecording();
                    audioBuffer = new byte[bufferSize];
                    audioRecord.read(audioBuffer, 0, bufferSize);

                    System.out.println("Grabando...");
                } else {
                    //crea un archivo wav con el audio grabado
                    System.out.println("Detener grabación");
                    audioRecord.stop();
                    audioRecord.release();

                    // guardar audio en el servidor
                    estaGrabando = false;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            sampleRate, channelConfig, audioFormat, bufferSize);
                    audioRecord.startRecording();
// Detenga la grabación de audio y libere los recursos de AudioRecord cuando sea apropiado
                    audioRecord.stop();
                    audioRecord.release();

                    int audioSessionId = audioRecord.getAudioSessionId();

                    MediaRecorder mediaRecorder = new MediaRecorder();
                    try {
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                        mediaRecorder.setAudioSessionId(audioSessionId);
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        Log.e(TAG, "Error al reproducir el audio grabado", e);
                    } finally {
                        mediaRecorder.release();
                    }

//
//                    AprendizajeLecturaTask hilo = new AprendizajeLecturaTask(url, audioBuffer);
//                    hilo.execute();
//                    detenerGrabacion();
                }
            }
        });
    }
    private void iniciarGrabacion() {
//        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        String audioFileName = "record.wav";
//        File audioFile = new File(directorio, audioFileName);
//        String audioFilePath = audioFile.getAbsolutePath();
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mediaRecorder.setOutputFile(audioFilePath);
//        try {
//            mediaRecorder.prepare();
//            mediaRecorder.start();
//            textoEstado.setText("Grabando...");
//            estaGrabando = true;
//            textoPregunta.setText(preguntas[(int) (Math.random() * preguntas.length)]);
//        } catch (IOException e) {
//            textoEstado.setText("Hubo un problema al iniciar la grabación");
//            throw new RuntimeException(e);
//        }

    }
    private void detenerGrabacion() {
//        mediaRecorder.stop();
//        mediaRecorder.release();
//        textoEstado.setText("Detenido");
//        textoPregunta.setText("");
//        estaGrabando = false;
//        File audio = new File("/storage/emulated/0/download/record.wav");
////        hiloSubirArchivo hilo = new hiloSubirArchivo(audio,usuario,contrasena);
        String url = "http://35.199.96.85/sendAudio";
        hiloSubirArchivo hilo = new hiloSubirArchivo(audioBuffer);
        hilo.start();
    }
    private boolean tienePermisos() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}