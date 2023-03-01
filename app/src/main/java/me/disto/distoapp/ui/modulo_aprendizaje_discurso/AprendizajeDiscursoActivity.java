package me.disto.distoapp.ui.modulo_aprendizaje_discurso;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import me.disto.distoapp.R;
import me.disto.distoapp.ui.utils.BaseActivity;

public class AprendizajeDiscursoActivity extends BaseActivity {

    //  UI
    ImageView botonIniciarDetener;
    TextView textoTiempoGrabacion;
    TextView textoEstado;
    //  UI

    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private Boolean estaGrabando = false;
    private String usuario = "useridx";

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprendizaje_discurso);

        setupBottomNavigation();
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menu_button_discurso).setChecked(true);

        textoTiempoGrabacion = findViewById(R.id.timer_textview);
        botonIniciarDetener = findViewById(R.id.boton_iniciar_detener);
        textoEstado = findViewById(R.id.status_textview);

        if(!tienePermisos()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        }

        botonIniciarDetener.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!estaGrabando){
                    iniciarGrabacion();
                }else{
                    detenerGrabacion();
                }
            }
        });
    }
    private void iniciarGrabacion() {
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String audioFileName = "record.wav";
        File audioFile = new File(directorio, audioFileName);
        String audioFilePath = audioFile.getAbsolutePath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        textoEstado.setText("Grabando...");
        estaGrabando = true;
    }
    private void detenerGrabacion() {
        mediaRecorder.stop();
        mediaRecorder.release();
        textoEstado.setText("Detenido");
        estaGrabando = false;
        File audio = new File("/storage/emulated/0/download/record.wav");
        hiloSubirArchivo hilo = new hiloSubirArchivo(audio,usuario);
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