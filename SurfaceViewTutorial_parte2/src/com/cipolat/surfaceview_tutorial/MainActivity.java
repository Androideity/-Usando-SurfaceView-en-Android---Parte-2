package com.cipolat.surfaceview_tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //oculto titulo 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //ocultamos barras de notifiaciones       
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //defino que en lugar de usar una UI de /res/layout utilizo la vista q brinda la clase MySurfaceView
        setContentView(new MySurfaceView(this));

    }

}
