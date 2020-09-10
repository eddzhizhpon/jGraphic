package jGraphic;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;


public class jGraphic extends Canvas{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8533524188665467498L;
	public Color backgroundColor;
	public Color lineColor;
	public Color numberColor;
	
	private int midW;
	private int midH;
	
	private int moveX;
	private int moveY;
	
	private double zoom;
	private double xInterval;
	private double yInterval;
	
	private double maxX;
	private double maxY;
	private int rescale;
	
	/*Valores para grficar el plano*/
	private List<Double> xPoints;
    private List<Double> yPoints;
    private List<Double> xNPoints;
    private List<Double> yNPoints;
    
    /*Valores para plot*/
    private List<double[]> xValues;
    private List<double[]> yValues;
    private List<String[]> sValues;
    private List<Color> colors;
    private List<Integer> sizes;
    private List<Boolean> types;
    
	public jGraphic() {
		super();
		
		moveX = 0;
		moveY = 0;
		
		maxX = 0;
		maxY = 0;
		
		rescale = -50;
		
		zoom = 0.0;
		xInterval = 50;
		yInterval = 50;
		
		backgroundColor = Color.BLACK;
		lineColor = Color.WHITE;
		numberColor = Color.WHITE;
		
		setBackground(backgroundColor);
		
		super.addMouseWheelListener(e -> {
			if(e.getWheelRotation() > 0) {
				zoom += 10;
				int w = (getWidth()/2) - 100 + rescale;
				int h = (getHeight()/2) - 100 + rescale;
				if (zoom > w || zoom > h) {
					if(w < h) {
						zoom = (double) w;
					} else {
						zoom = (double) h;
					}
				}
			}else if (e.getWheelRotation() < 0) {
				zoom -= 10;
				if (zoom < -5000) {
					zoom = -5000.0;
				}
				
			}
			repaint();
		});
		
		super.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    moveX-=10;
                else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                    moveY-=10;
                else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                    moveX+=10;
                else if(e.getKeyCode() == KeyEvent.VK_UP)
                    moveY+=10;
                repaint();
			}
		});
	}
	
	public void addGraphic(double[] x, double[] y, String[] s, Color c, int size, boolean isPlot) {
		if (xValues == null) {
			xValues = new ArrayList<>();
		    yValues = new ArrayList<>();
		    sValues = new ArrayList<>();
		    colors = new ArrayList<>();
		    sizes = new ArrayList<>();
		    types = new ArrayList<>();
		}
		xValues.add(x);
		yValues.add(y);
		sValues.add(s);
		colors.add(c);
		sizes.add(size);
		types.add(isPlot);
		
		maxX = calcMax(xValues);
		maxY = calcMax(yValues);
		
		repaint();
		//System.out.println("MAX_X:" + maxX + " | MAX_Y:" + maxY);
	}

	private double calcMax(List<double[]> a) {
		
		double hightCurrent = 0.0;
		double hight = 0.0;
		
		for(double[] v: a) {
			int n = v.length - 1;
			double[] vAux = v.clone();
			sort(vAux, 0, n);
			hightCurrent = vAux[n];
			if (hightCurrent > hight)
				hight = hightCurrent;
		}
		return hight;
	}
	
	private void merge (double[] a, int l, int m, int r) {
		double[] aAux = new double[a.length];
		
		int i;
		int j;
		int k;
		
		for (i = l; i <= r; i++) {
			aAux[i] = Math.abs(a[i]);
		}
		
		i = l;
        j = m + 1;
        k = l;
        
        while((i <= m) && (j <= r)){
            if(aAux[i] <= aAux[j]){
                a[k++] = aAux[i++];   
            }else{
                a[k++] = aAux[j++];
            }
        }
        
        while(i <= m){
            a[k++] = aAux[i++];
        }
	}
	
	private void sort(double[] a, int l, int r) {
		if(l < r){
            int m;
            m = (l + r)/2;
            sort(a, l, m);
            sort(a, m + 1, r);
            merge(a, l, m, r);
        }
	}
	
    private void drawLines(Graphics2D g){
        
        g.setColor(lineColor);
         
        g.drawLine(-(super.getWidth() + moveX), 0, super.getWidth() - moveX, 0);
        g.drawLine(0, midH - moveY, 0, - (midH + moveY));
    }
    
    private double rule3(double v, double l, int t) {
    	t = t + rescale;
        return (v * t) / l;
    }
    
    private void createPoints(){
    	
    	xPoints = new ArrayList<>();
    	yPoints = new ArrayList<>();
        xNPoints = new ArrayList<>();
        yNPoints = new ArrayList<>();
        
        try{
            
            xInterval = maxX/5;
            yInterval = maxY/5;

            double inter = rule3(xInterval, maxX, midW - (int)zoom);
            for (double i = inter; i < -( -midW + moveX); i = i + (inter)) {
            	xPoints.add(i);
            }
            for (double i = inter; i < midW + moveX; i = i + inter) {
            	xNPoints.add(-i);
            }

            inter = rule3(yInterval, maxY, midH - (int)zoom);

            for (double i = inter; i < midH  + moveY; i = i + inter) {
            	yPoints.add(-i);
            }
            for (double i = inter; i < -1 * (-midH + moveY); i = i + inter) {
            	yNPoints.add(i);
            }
       }catch(Exception ex){
           ex.printStackTrace();
       }
    }
    
    private void drawPoints(Graphics2D g, List<Double> xPoints,
            List<Double> xNPoints, List<Double> yPoints,
            List<Double> yNPoints){
        
        double val = 0.0;
        int p;
        int n = xPoints.size();
        for(int i = 0; i < n; i++){
            g.setColor(lineColor);
            val = val + xInterval;
            p = xPoints.get(i).intValue();
            g.drawLine(p, 5, 
                    p,- 5);
            g.setColor(numberColor);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", val), p - 10, 30);
        }
        val = 0.0;
        n = xNPoints.size();
        for(int i = 0; i < n ; i++){
            g.setColor(lineColor);
            val = val + xInterval;
            p = xNPoints.get(i).intValue();
            g.drawLine(p, 5, 
                    p, -5);
            g.setColor(numberColor);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", -val), p - 10, 30);
        }
        n = yPoints.size();
        val = 0.0;
        for (int i = 0; i < n; i++) {
            g.setColor(lineColor);
            val = val + yInterval;
            p = yPoints.get(i).intValue();
            g.drawLine(5, p, 
                    -5, p);
            
            g.setColor(numberColor);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", val), -50, p + 5);
            
        }
        n = yNPoints.size();
        val = 0.0;
        for (int i = 0; i < n; i++) {
            g.setColor(lineColor);
            val = val + yInterval;
            p = yNPoints.get(i).intValue();
            g.drawLine(5, p, 
                    -5, p);
            
            g.setColor(numberColor);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", -val), -50, p + 5);
            
        }
    }
    
    private Graphics2D drawPlotGraphic(Graphics2D g, double[] xValues, double[] yValues, int j){
        int puntoY1;
        int puntoX1;
        int puntoY2;
        int puntoX2;
        int n;
        int i;
        
        g.setColor(colors.get(j));
        g.setStroke(new BasicStroke(sizes.get(j)));
        i = 0;
        n = xValues.length;
        puntoX1 = (int)rule3(xValues[i],maxX, midW - (int)zoom);
        puntoY1 = (int)rule3(yValues[i],maxY, midH - (int)zoom);
        i++;
        while(i < n){

            puntoX2 = (int)rule3(xValues[i],maxX, midW - (int)zoom);
            puntoY2 = (int)rule3(yValues[i],maxY, midH - (int)zoom);

            g.drawLine(puntoX1, -puntoY1, puntoX2, -puntoY2);

            puntoX1 = puntoX2;
            puntoY1 = puntoY2;
            i++;
        }
        return g;
    }
    
    private Graphics2D drawStemGraphic(Graphics2D g, double[] xValues, double[] yValues, int j){
        int puntoY1;
        int puntoX1;
        int n;
        
        g.setColor(Color.blue);
        int i;
        g.setColor(colors.get(j));
        i=0;
        n = xValues.length;
        while(i<n){

            puntoX1 = (int)rule3(xValues[i],maxX, midW - (int)zoom);
            puntoY1 = (int)rule3(yValues[i],maxY, midH - (int)zoom);

            g.fillOval(puntoX1 - (int)this.sizes.get(j)/2, 
                    -(puntoY1 + (int)this.sizes.get(j)/2), 
                    this.sizes.get(j), this.sizes.get(j));
            i++;
        }
        //stem = false;
        return g;
    }
    
    private Graphics2D drawGraphics(Graphics2D g) {
    	int j;
    	int n = xValues.size();
    	
    	for (j = 0; j < n; j++) {
    		if(types.get(j)) {
    			drawPlotGraphic(g, xValues.get(j), yValues.get(j), j);
    		}else {
    			drawStemGraphic(g, xValues.get(j), yValues.get(j), j);
    		}
    	}
    	
    	
    	return g;
    }
    
    public void paint(Graphics g) {
        
//        BufferStrategy buffer = getBufferStrategy();
//        if (buffer == null) {
//        	createBufferStrategy(3);
//        	return;
//        }
        //Graphics gb = buffer.getDrawGraphics();
        Graphics gb = g;
        this.midW = (int)((super.getWidth())/2);
        this.midH = (int)((super.getHeight())/2);
        
        
        int w = (midW)-100 + rescale;
        int h = (midH)-100 + rescale;
        
        if (zoom > w||zoom > h) {
            if (w<h) {
                zoom = (double)w;
            }else{
                zoom = (double)h;
            }
        }
        
        super.paint(gb);
       
        Graphics2D g2d = (Graphics2D) gb;
       
        g2d.translate(midW + moveX, midH + moveY);
        drawLines(g2d);
       
        createPoints();
        try {
        	drawPoints(g2d, xPoints, xNPoints, yPoints, 
                    yNPoints);
        	drawGraphics(g2d);
        } catch (Exception ex) {
        	//ex.printStackTrace();
        }
        gb.dispose();
        //buffer.show();
    }
}
