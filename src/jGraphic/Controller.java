package jGraphic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	
	private double[] x;
    private double[] y;
    
    private String xName;
    private String yName;

	public void readFile(String ruta) throws IOException{
		
        String linea = "";
        List<Double> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        FileReader file = null;
        BufferedReader read = null;
	    try {
	        
	        file = new FileReader(ruta);
	        read = new BufferedReader(file);
	        boolean con = false;
	        while(linea != null){
	            linea = read.readLine();
	            
	            if (linea!=null && con) {
	                String [] a = linea.split(",");
	                xData.add(Double.parseDouble(a[0]));
	                yData.add(Double.parseDouble(a[1]));
	            }else{
	                String [] n = linea.split(",");
	                xName = n[0];
	                yName = n[1];
	                con = true;
	            }
	            
	        }
	        
	    } catch (NullPointerException ex) {
	        
	    }catch (ArrayIndexOutOfBoundsException e){
	        this.xName = "X";
	        this.yName = "Y";
	    }finally{
	    	read.close();
	        file.close();
	    }
	    
        this.x = new double[xData.size()];
        this.y = new double[yData.size()];
        convDat(xData, this.x);
        convDat(yData, this.y);
	}
	
    private void convDat(List<Double> l, double[] d){
        int i = 0;
        for (double e: l) {
            d[i] = e;
            i++;
        }
        
    }

	public double[] getX() {
		return x;
	}

	public void setX(double[] x) {
		this.x = x;
	}

	public double[] getY() {
		return y;
	}

	public void setY(double[] y) {
		this.y = y;
	}

	public String getxName() {
		return xName;
	}

	public void setxName(String xName) {
		this.xName = xName;
	}

	public String getyName() {
		return yName;
	}

	public void setyName(String yName) {
		this.yName = yName;
	}
}
