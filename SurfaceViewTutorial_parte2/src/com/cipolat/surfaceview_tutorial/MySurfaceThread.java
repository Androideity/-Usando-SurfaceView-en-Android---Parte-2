package com.cipolat.surfaceview_tutorial;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MySurfaceThread extends Thread {

	private SurfaceHolder sh;
	private MySurfaceView view;
	private boolean run;
    //Nuestro Constructor recibe como parametros la referencia a SurfaceHolder y nuestra SurfaceView
	public MySurfaceThread(SurfaceHolder sh, MySurfaceView view) {
		this.sh = sh;
		this.view = view;
		run = false;
	}
    //Lo utilizaremos para establecer cuando el hilo corra o no.
	public void setRunning(boolean run) {
		this.run = run;
	}

	public void run() {
		//Instancia a canvas
		Canvas canvas;
		//Mientras la variable run sea true va a pintar contínuamente.
		while(run) {
			canvas = null;
			try {
				//definimos nulo el area en donde pintar
				canvas = sh.lockCanvas(null);
				//usamos synchronized para asegurarnos que no halla ningun otro thread usando ese objeto 
				synchronized(sh) {
					//Le decimos al surface view que ejecute el metodo onDraw y el cavas para dibujar
					view.onDraw(canvas);
				}
			} finally {
				/*En caso de que halla algun error liberamos el canvas 
				 * para no dejar el surfaceview en un estado inconsistente
				 */				
				if(canvas != null)
					//liberamos el canvas
					sh.unlockCanvasAndPost(canvas);
			}
		}
	}
}

