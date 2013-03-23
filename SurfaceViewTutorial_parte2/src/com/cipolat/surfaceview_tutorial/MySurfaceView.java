package com.cipolat.surfaceview_tutorial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
  
	//Referencia a un thread usaremos para dibujar
	public MySurfaceThread thread;
	//variables q indican las coord en donde se toco la pantalla
	public int touched_x, touched_y;
	// variable q indica si se esta tocando la pantalla o no
	public boolean touched;

	public MySurfaceView(Context context) {
		super(context);
		//usaremos esta clase como manejador
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		thread = new MySurfaceThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.e("surfaceDestroyed ", "Hilo detenido ");

		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		// mitad de ancho
		int ancho = canvas.getWidth();
		int alto = canvas.getHeight();

		int mitadW = ancho / 2;
		// mitad de largo
		int mitadH = alto / 2;

		// Imagen del boton
		Bitmap imgBoton_libre = BitmapFactory.decodeResource(getResources(),
				R.drawable.botonlibre);
		Bitmap imgBoton_push = BitmapFactory.decodeResource(getResources(),
				R.drawable.botonpress);

		int anchobitmap = imgBoton_libre.getWidth();
		int altobitmap = imgBoton_libre.getHeight();

		// pinto fondo de negro
		canvas.drawColor(Color.GRAY);

		// circulo externo
		Paint pcirculo = new Paint();
		pcirculo.setColor(Color.BLACK);
		pcirculo.setStyle(Paint.Style.FILL);
		canvas.drawCircle(mitadW, mitadH, 60, pcirculo);

		// circulo interno
		Paint pcirculointerno = new Paint();
		pcirculointerno.setColor(Color.RED);
		pcirculointerno.setStyle(Paint.Style.FILL);

		// circulo interno
		Paint imgboton = new Paint();
		imgboton.setAntiAlias(true);
		imgboton.setFilterBitmap(true);
		imgboton.setDither(true);

		/*
		 * Validaciones sobre el movimiento de la palanca. Si estamos tocando la
		 * pantalla y en donde tocamos esta dentro del sector de la palanca la
		 * dibujaremos en donde toco dara la sensacion de q la movemo
		 */
		if (touched && // si tocamos
				// ejeY
				((touched_x > mitadW / 2 && touched_x < ancho - mitadW / 2)
				// ejeX
						&& (touched_y > mitadH / 2) && (touched_y < (mitadH + (mitadH / 2))))) {

			canvas.drawCircle(touched_x, touched_y, 35, pcirculointerno);
		} else {
			// si no dibujarla en el centro
			canvas.drawCircle(mitadW, mitadH, 35, pcirculointerno);
		}

		// Dibujamos Boton

		/* Validamos si donde estamos tocando esta dentro del sector del boton */

		// if (touched && touched_x>ancho*2/3 && touched_x >alto*3/4){

		if (touched
				&& // si tocamos
					// ejeY
				(touched_y > mitadH + (mitadH / 2) && (touched_y < mitadH
						+ (mitadH / 2) + altobitmap))
				&&
				// ejeX
				(touched_x > mitadW + (mitadW / 2))
				&& (touched_x < mitadW + (mitadW / 2) + anchobitmap)) {
			canvas.drawBitmap(imgBoton_push, ancho * 2 / 3, alto * 3 / 4, null);
		} else {
			canvas.drawBitmap(imgBoton_libre, ancho * 2 / 3, alto * 3 / 4, null);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		touched_x = (int) event.getX();
		touched_y = (int) event.getY();

		// leemos codigo de accion
		int action = event.getAction();
		Log.e("touched (X,Y)", "(" + touched_x + "," + touched_y + ")");

		switch (action) {
		case MotionEvent.ACTION_DOWN:// Cuando se toca la pantalla
			Log.e("TouchEven ACTION_DOWN", "Se toco la pantalla ");
			touched = true;
			break;
		case MotionEvent.ACTION_MOVE:// Cuando se desplaza el dedo por la pantalla
			touched = true;
			Log.e("TouchEven ACTION_MOVE", "Nos desplazamos por la pantalla ");
			break;

		case MotionEvent.ACTION_UP:// Cuando levantamos el dedo de la pantalla
									// que estabamos tocando
			touched = false;
			Log.e("TouchEven ACTION_UP", "Ya no tocamos la pantalla");

			break;

		case MotionEvent.ACTION_CANCEL:
			touched = false;
			Log.e("TouchEven ACTION_CANCEL", " ");

			break;

		case MotionEvent.ACTION_OUTSIDE:
			Log.e("TouchEven ACTION_OUTSIDE", " ");
			touched = false;
			break;

		default:
		}
		return true;

	}
}