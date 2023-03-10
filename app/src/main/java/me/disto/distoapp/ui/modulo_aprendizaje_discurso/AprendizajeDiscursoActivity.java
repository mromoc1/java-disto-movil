package me.disto.distoapp.ui.modulo_aprendizaje_discurso;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class AprendizajeDiscursoActivity extends BaseActivity {

    //  UI
    ImageView botonIniciarDetener;
    TextView textoTiempoGrabacion;
    TextView textoEstado;
    TextView textoPregunta;
    //  UI

    private MediaRecorder mediaRecorder;
    private AudioRecord audioRecord;
    private Thread recordingThread;
    private String audioFilePath;
    private Boolean estaGrabando = false;
    private String usuario = "useridx";
    private String contrasena = "test123";

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
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


        if (!tienePermisos()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        }

        botonIniciarDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!estaGrabando) {
                    iniciarGrabacion();
                } else {
                    detenerGrabacion();
                }
            }
        });
    }

    private void iniciarGrabacion(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/record.wav");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            textoEstado.setText("Grabando...");
            estaGrabando = true;
            textoPregunta.setText(preguntas[(int) (Math.random() * preguntas.length)]);
        } catch (IOException e) {
            textoEstado.setText("Hubo un problema al iniciar la grabación");
            throw new RuntimeException(e);
        }
    }
    private void detenerGrabacion() {
        mediaRecorder.stop();
        mediaRecorder.release();
        textoEstado.setText("Detenido");
        textoPregunta.setText("");
        estaGrabando = false;
        File audio = new File(getExternalCacheDir().getAbsolutePath() + "/record.wav");
        hiloSubirArchivo hilo = new hiloSubirArchivo(audio,usuario,contrasena);
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