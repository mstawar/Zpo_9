package ib.edu.czujniki;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager; // mozliwosc wybierania czujnikow i ustawiania ich wlasnosci
    private Sensor accelerometer;
    private boolean isRunning = false; // wlaczanie i wylaczanie pomiaru
    Button btnMeasurementStart;
    TextView tvAx;
    private PowerManager.WakeLock wakeLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // inicjalizacja przez rzutowanie
        // czemu ? bo polimorfizm. w momencie gdy tworzy sie system serwisow, nie wiesz z jakimi obiektami uzytkownik bedzie pracowal
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // przygotowujemy do dzialania accelerometr
        // sensorManager moze sluzyc do obslugi takze innych czujnikow
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        // system android monitoruje czujniki, ale kiedy to zrobi, nie wiadomo, mozem okreslic tylko intencje co to szybkosci zbierania
        // danych
        // wbudowane czujniki maja wysoka czestotliwosc probkowania, 500 Hz

txtAx = (TextView) findViewById(R.id,tvAx);




        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Test czujnikow:Info");
        wakeLock.acquire();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        // ustalenie z jakim czujnikiem mamy do czynienia

        if (isRunning) {
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                float ax = event.values[0];
                Log.d("onSensorChanged", Float.toString(ax));
           tvAx.setText(Float.toString(ax));

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onMeasurementClick(View view) {


        if (!isRunning) wakeLock.release();
        else
            wakeLock.acquire();

        Log.d("onMeasurementClick", "ButtonPressed");
        isRunning = !isRunning;


    }
}


