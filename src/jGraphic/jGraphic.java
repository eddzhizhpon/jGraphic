package jGraphic;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class jGraphic extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8533524188665467498L;
	public Color backgroundColor;
	public Color lineColor;
	public Color numberColor;
	public Color gridColor;
	public Color titleColor;
	public Color labelColor;
	public boolean grid;

	public String title;
	public int titleSize;
	public String xLabel;
	public String yLabel;
	public int labelSize;

	public int numberSize;

	private int midW;
	private int midH;

	public int moveX;
	public int moveY;

	public double zoom;
	private double xInterval;
	private double yInterval;

	private double maxX;
	private double maxY;
	public int rescale;

	/* Valores para grficar el plano */
	private List<Double> xPoints;
	private List<Double> yPoints;
	private List<Double> xNPoints;
	private List<Double> yNPoints;

	/* Valores para plot */
	private List<double[]> xValues;
	private List<double[]> yValues;
	private List<Color> colors;
	private List<Integer> sizes;
	private List<Boolean> types;

	private BufferStrategy buffer;

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
		gridColor = Color.DARK_GRAY;
		titleColor = Color.WHITE;
		labelColor = Color.YELLOW;

		grid = true;

		title = "Title";
		xLabel = "X";
		yLabel = "Y";

		titleSize = 16;
		labelSize = 12;
		numberSize = 10;

		setBackground(backgroundColor);
		super.addMouseWheelListener(e -> {
			if (e.getWheelRotation() > 0) {
				zoom += 10;
				int w = (getWidth() / 2) - 100 + rescale;
				int h = (getHeight() / 2) - 100 + rescale;
				if (zoom > w || zoom > h) {
					if (w < h) {
						zoom = (double) w;
					} else {
						zoom = (double) h;
					}
				}
			} else if (e.getWheelRotation() < 0) {
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
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					moveX -= 10;
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
					moveY -= 10;
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					moveX += 10;
				if (e.getKeyCode() == KeyEvent.VK_UP)
					moveY += 10;
				repaint();
			}
		});
		repaint();
	}

	public void addGraphic(double[] x, double[] y, Color c, int size, boolean isPlot) {
		if (xValues == null) {
			xValues = new ArrayList<>();
			yValues = new ArrayList<>();
			colors = new ArrayList<>();
			sizes = new ArrayList<>();
			types = new ArrayList<>();
		}
		xValues.add(x);
		yValues.add(y);
		colors.add(c);
		sizes.add(size);
		types.add(isPlot);
		maxX = calcMax(xValues);
		maxY = calcMax(yValues);
		repaint();
	}

	public void clean() {
		xValues = null;
		yValues = null;
		colors = null;
		sizes = null;
		types = null;
		moveX = 0;
		moveY = 0;
		maxX = 0.0;
		maxY = 0.0;
		rescale = -50;
		zoom = 0.0;
		xInterval = 50;
		yInterval = 50;
		repaint();
	}

	private double calcMax(List<double[]> a) {
		double hightCurrent = 0.0;
		double hight = 0.0;
		for (double[] v : a) {
			int n = v.length - 1;
			double[] vAux = v.clone();
			Arrays.sort(vAux);
			hightCurrent = vAux[n];
			if (hightCurrent > hight)
				hight = hightCurrent;
		}
		return hight;
	}

	private void drawLines(Graphics2D g) {
		g.setColor(lineColor);
		g.drawLine(-(super.getWidth() + moveX), 0, super.getWidth() - moveX, 0);
		g.drawLine(0, midH - moveY, 0, -(midH + moveY));
	}

	private double rule3(double v, double l, int t) {
		t = t + rescale;
		return (v * t) / l;
	}

	private void createPoints() {
		xPoints = new ArrayList<>();
		yPoints = new ArrayList<>();
		xNPoints = new ArrayList<>();
		yNPoints = new ArrayList<>();
		int div = 5;
		try {
			if (zoom <= -100) {
				div = 7;
			}
			if (zoom <= -350) {
				div = 9;
			}
			if (zoom <= -1000) {
				div = 11;
			}
			xInterval = maxX / div;
			yInterval = maxY / div;

			double inter = rule3(xInterval, maxX, midW - (int) zoom);
			for (double i = inter; i < -(-midW + moveX); i = i + (inter)) {
				xPoints.add(i);
			}
			for (double i = inter; i < midW + moveX; i = i + inter) {
				xNPoints.add(-i);
			}

			inter = rule3(yInterval, maxY, midH - (int) zoom);

			for (double i = inter; i < midH + moveY; i = i + inter) {
				yPoints.add(-i);
			}
			for (double i = inter; i < -1 * (-midH + moveY); i = i + inter) {
				yNPoints.add(i);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void drawGrid(Graphics2D g) {
		if (grid) {
			g.setColor(gridColor);
			drawXGrid(g, xPoints);
			drawXGrid(g, xNPoints);
			drawYGrid(g, yPoints);
			drawYGrid(g, yNPoints);
		}
	}

	private void drawXGrid(Graphics2D g, List<Double> xPoints) {
		int p;
		int n = xPoints.size();
		for (int i = 0; i < n; i++) {
			p = xPoints.get(i).intValue();
			g.drawLine(p, midH - moveY, p, -midH - moveY);
		}
	}

	private void drawYGrid(Graphics2D g, List<Double> yPoints) {
		int p;
		int n = yPoints.size();
		for (int i = 0; i < n; i++) {
			p = yPoints.get(i).intValue();
			g.drawLine(midW - moveX, p, -midW - moveX, p);
		}
	}

	private void drawPoints(Graphics2D g) {
		drawXYPoints(g, xPoints, xInterval, 'x', false);
		drawXYPoints(g, xNPoints, xInterval, 'x', true);
		drawXYPoints(g, yPoints, yInterval, 'y', false);
		drawXYPoints(g, yNPoints, yInterval, 'y', true);
	}

	private void drawXYPoints(Graphics2D g, List<Double> points, double interval, char key, boolean isNegative) {
		double val = 0.0;
		int p;
		int n = points.size();
		String str;
		FontRenderContext frc;
		Rectangle2D bounds;
		LineMetrics metrics;
		float w;
		float h;
		g.setFont(new Font("Arial", 0, numberSize));
		for (int i = 0; i < n; i++) {
			val = val + interval;
			p = points.get(i).intValue();

			if (isNegative)
				str = String.format("%.2f", -val);
			else
				str = String.format("%.2f", val);

			frc = g.getFontRenderContext();
			bounds = g.getFont().getStringBounds(str, frc);
			metrics = g.getFont().getLineMetrics(str, frc);

			w = (float) bounds.getWidth();
			h = (float) metrics.getHeight();

			if (key == 'x') {
				drawXPoint(g, str, p, w, h);
			} else {
				drawYPoint(g, str, p, w, h);
			}
		}
	}

	private void drawXPoint(Graphics2D g, String str, int p, float w, float h) {
		g.setColor(lineColor);
		g.drawLine(p, 5, p, -5);
		g.setColor(numberColor);
		g.drawString(str, p - w / 2, h + 10);
	}

	private void drawYPoint(Graphics2D g, String str, int p, float w, float h) {
		g.setColor(lineColor);
		g.drawLine(5, p, -5, p);
		g.setColor(numberColor);
		g.drawString(str, -w - 10, p + h / 2);
	}

	private Graphics2D drawPlotGraphic(Graphics2D g, double[] xValues, double[] yValues, int j) {
		int pointY1;
		int pointX1;
		int pointY2;
		int pointX2;
		int n;
		int i;
		g.setColor(colors.get(j));
		g.setStroke(new BasicStroke(sizes.get(j)));
		i = 0;
		n = xValues.length;
		pointX1 = (int) rule3(xValues[i], maxX, midW - (int) zoom);
		pointY1 = (int) rule3(yValues[i], maxY, midH - (int) zoom);
		i++;
		while (i < n) {
			pointX2 = (int) rule3(xValues[i], maxX, midW - (int) zoom);
			pointY2 = (int) rule3(yValues[i], maxY, midH - (int) zoom);
			g.drawLine(pointX1, -pointY1, pointX2, -pointY2);
			pointX1 = pointX2;
			pointY1 = pointY2;
			i++;
		}
		return g;
	}

	private Graphics2D drawStemGraphic(Graphics2D g, double[] xValues, double[] yValues, int j) {
		int pointY1;
		int pointX1;
		int n;
		int i;
		g.setColor(colors.get(j));
		i = 0;
		n = xValues.length;
		while (i < n) {
			pointX1 = (int) rule3(xValues[i], maxX, midW - (int) zoom);
			pointY1 = (int) rule3(yValues[i], maxY, midH - (int) zoom);
			g.fillOval(pointX1 - (int) this.sizes.get(j) / 2, -(pointY1 + (int) this.sizes.get(j) / 2),
					this.sizes.get(j), this.sizes.get(j));
			i++;
		}
		return g;
	}

	private Graphics2D drawGraphics(Graphics2D g) {
		int j;
		int n = xValues.size();
		for (j = 0; j < n; j++) {
			if (types.get(j)) {
				drawPlotGraphic(g, xValues.get(j), yValues.get(j), j);
			} else {
				drawStemGraphic(g, xValues.get(j), yValues.get(j), j);
			}
		}
		return g;
	}

	private void drawLabel(Graphics2D g, String str, char key, float x0, float y0, double angle, int size) {
		g.setFont(new Font("Arial", Font.BOLD, size));
		FontRenderContext frc = g.getFontRenderContext();
		Rectangle2D bounds = g.getFont().getStringBounds(str, frc);
		LineMetrics metrics = g.getFont().getLineMetrics(str, frc);
		float w = (float) bounds.getWidth();
		float h = (float) metrics.getHeight();
		g.setColor(backgroundColor);
		switch (key) {
		case 't':
			g.fill(new Rectangle2D.Double(x0 - 5 - w / 2, y0 - 5, w + 10, h + 13));
			g.setColor(titleColor);
			g.drawString(str, x0 - w / 2, y0 + h);
			break;
		case 'x':
			g.fill(new Rectangle2D.Double(x0 - 5 - w / 2, y0 - 10 - h, w + 10, h + 15));
			g.setColor(labelColor);
			g.drawString(str, x0 - w / 2, y0 - 6);
			break;
		case 'y':
			g.rotate(angle, x0, y0);
			g.fill(new Rectangle2D.Double(x0 - 5 - w / 2, y0, w + 10, h + 15));
			g.setColor(labelColor);
			g.drawString(str, x0 - w / 2, y0 + h + 5);
			g.rotate(-angle, x0, y0);
			break;
		default:
			break;
		}
	}

	private void drawLabels(Graphics2D g) {
		float x0 = (float) -moveX;
		float y0 = (float) -moveY - midH + 5;
		drawLabel(g, title, 't', x0, y0, 0, titleSize);
		x0 = (float) -moveX;
		y0 = (float) -(moveY - midH);
		drawLabel(g, xLabel, 'x', x0, y0, 0, labelSize);
		x0 = (float) -moveX - midW;
		y0 = (float) -moveY;
		drawLabel(g, yLabel, 'y', x0, y0, (float) -Math.PI / 2, labelSize);
	}

	public void paint(Graphics g) {
		buffer = getBufferStrategy();
		if (buffer == null) {
			createBufferStrategy(4);
			repaint();
			return;
		}
		Graphics gb = buffer.getDrawGraphics();
		this.midW = (int) ((super.getWidth()) / 2);
		this.midH = (int) ((super.getHeight()) / 2);
		int w = (midW) - 100 + rescale;
		int h = (midH) - 100 + rescale;
		if (zoom > w || zoom > h) {
			if (w < h) {
				zoom = (double) w;
			} else {
				zoom = (double) h;
			}
		}
		super.paint(gb);
		Graphics2D g2d = (Graphics2D) gb;
		g2d.translate(midW + moveX, midH + moveY);
		drawLines(g2d);
		createPoints();
		try {
			drawGrid(g2d);
			drawPoints(g2d);
			drawGraphics(g2d);
			drawLabels(g2d);
		} catch (NullPointerException ex) {
			// ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		g2d.dispose();
		gb.dispose();
		buffer.show();
	}
}
