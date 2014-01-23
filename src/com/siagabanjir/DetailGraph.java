package com.siagabanjir;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class DetailGraph extends View {
	private ShapeDrawable background;
	private ShapeDrawable[] barCharts;
	private String tanggal;
	private String rentangWaktu;
	private int width;
	private int height;
	
	public DetailGraph(Context context, int[] tinggi, String[] status, String tanggal, String rentangWaktu) {
		super(context);
		// TODO Auto-generated constructor stub
		barCharts = new ShapeDrawable[tinggi.length];
		
		int x = 0;
		int y = 0;
		int width = 434;
		int height = 187;
		
		background = new ShapeDrawable(new RectShape());
		background.getPaint().setColor(0xffECECEC);
		background.setBounds(x, y, x+width, y+height);
		
		for (int i=0; i<barCharts.length; i++) {
			barCharts[barCharts.length - 1 - i] = new ShapeDrawable(new RectShape());
			int barHeight = 0;
			if (status[barCharts.length - 1 - i].equals("KRITIS")) {
				barHeight = 100;
				barCharts[barCharts.length - 1 - i].getPaint().setColor(0xffa52728);
			} else if (status[barCharts.length - 1 - i].equals("RAWAN")) {
				barHeight = 75;
				barCharts[barCharts.length - 1 - i].getPaint().setColor(0xfff25b24);
			} else if (status[barCharts.length - 1 - i].equals("WASPADA")) {
				barHeight = 50;
				barCharts[barCharts.length - 1 - i].getPaint().setColor(0xffffb031);
			} else if (status[barCharts.length - 1 - i].equals("NORMAL")) {
				barHeight = 25;
				barCharts[barCharts.length - 1 - i].getPaint().setColor(0xffb2c945);
			}
			barCharts[barCharts.length - 1 - i].setBounds((i*60 + 20), 150 - barHeight, (i*60 + 20) + 32, 150);
			
		}
		this.tanggal = tanggal;
		this.rentangWaktu = rentangWaktu;
		
	}

	protected void onDraw(Canvas canvas) {
		background.draw(canvas);
		for (int i=0; i<barCharts.length; i++) {
			barCharts[i].draw(canvas);
		}

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(20);
		canvas.drawText(tanggal + " | " + rentangWaktu, 434/2, 178, paint);
	}
	
}
