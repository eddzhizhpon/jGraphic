package jGraphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

public class Gui extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller controller;
	private jGraphic jgraphic;

	private JTextField gradoTxt;
	private JTextField xTxt;
	private JTextField yTxt;
	private JSpinner sizeSpn;
	private JCheckBox plotCB;
	private Color graphicColor;
	private JPanel colorPanel;

	private String file;

	public Gui() {
		initComponent();
		controller = new Controller();
	}

	private void initComponent() {
		setTitle("Test Gui");
		setSize(800, 700);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		GridBagConstraints gbc = new GridBagConstraints();

		/* Up Panel */
		JPanel upPanel = new JPanel(new GridBagLayout());
		JPanel btnPanel = new JPanel(new FlowLayout());

		JButton upFileBtn = new JButton("Open File");
		upFileBtn.addActionListener(this);
		upFileBtn.setActionCommand("upfile");
		btnPanel.add(upFileBtn);

		JButton plotBtn = new JButton("Plot");
		plotBtn.addActionListener(this);
		plotBtn.setActionCommand("plot");
		btnPanel.add(plotBtn);

		JButton cleanBtn = new JButton("Clean");
		cleanBtn.addActionListener(this);
		cleanBtn.setActionCommand("clean");
		btnPanel.add(cleanBtn);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		upPanel.add(btnPanel, gbc);

		JPanel plotPanel = new JPanel(new FlowLayout());

		plotCB = new JCheckBox("Plot/Stem");
		plotCB.setSelected(true);
		plotPanel.add(plotCB);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1;
		upPanel.add(plotPanel, gbc);

		/* Center Panel */
		JPanel centerPanel = new JPanel(new BorderLayout());

		jgraphic = new jGraphic();
		centerPanel.add(jgraphic, BorderLayout.CENTER);

		/* Down Panel */
		JPanel downPanel = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel gradoLb = new JLabel("Length:", 11);
		gbc.gridx = 0;
		gbc.gridy = 0;
		downPanel.add(gradoLb, gbc);

		gradoTxt = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		downPanel.add(gradoTxt, gbc);

		JButton evalBtn = new JButton("Show");
		evalBtn.addActionListener(this);
		evalBtn.setActionCommand("show");
		gbc.gridx = 2;
		gbc.gridy = 0;
		downPanel.add(evalBtn, gbc);

		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		Dimension d = sep.getPreferredSize();
		d.height = evalBtn.getPreferredSize().height;
		sep.setPreferredSize(d);
		gbc.gridx = 3;
		gbc.gridy = 0;
		downPanel.add(sep, gbc);

		JButton gaussBtn = new JButton("Gauss Example");
		gaussBtn.addActionListener(this);
		gaussBtn.setActionCommand("gauss");
		gbc.gridx = 4;
		gbc.gridy = 0;
		downPanel.add(gaussBtn, gbc);

		JSeparator sep1 = new JSeparator(JSeparator.HORIZONTAL);
		d = sep1.getPreferredSize();
		d.width = downPanel.getPreferredSize().width;
		sep1.setPreferredSize(d);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 5;
		downPanel.add(sep1, gbc);

		JLabel xLb = new JLabel("X:", 11);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		downPanel.add(xLb, gbc);

		xTxt = new JTextField(20);
		xTxt.setToolTipText("1, 2, 3, n, ...");
		gbc.gridx = 1;
		gbc.gridy = 2;
		downPanel.add(xTxt, gbc);

		JLabel yLb = new JLabel("Y:", 11);
		gbc.gridx = 0;
		gbc.gridy = 3;
		downPanel.add(yLb, gbc);

		yTxt = new JTextField(20);
		yTxt.setToolTipText("1, 2, 3, n, ...");
		gbc.gridx = 1;
		gbc.gridy = 3;
		downPanel.add(yTxt, gbc);

		JLabel sizeLb = new JLabel("Size:", 11);
		gbc.gridx = 0;
		gbc.gridy = 4;
		downPanel.add(sizeLb, gbc);

		sizeSpn = new JSpinner();
		SpinnerNumberModel spnModel = new SpinnerNumberModel();
		spnModel.setMaximum(50);
		spnModel.setMinimum(1);
		spnModel.setValue(5);
		sizeSpn.setModel(spnModel);
		gbc.gridx = 1;
		gbc.gridy = 4;
		((DefaultEditor) sizeSpn.getEditor()).getTextField().setEditable(false);
		downPanel.add(sizeSpn, gbc);

		JButton colorBtn = new JButton("Color");
		colorBtn.addActionListener(this);
		colorBtn.setActionCommand("choseColor");
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		downPanel.add(colorBtn, gbc);

		colorPanel = new JPanel(new FlowLayout());
		graphicColor = Color.white;
		colorPanel.setBackground(graphicColor);
		colorPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		colorPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					choseColor();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		downPanel.add(colorPanel, gbc);

		JButton addGraphicBtn = new JButton("Add");
		addGraphicBtn.addActionListener(this);
		addGraphicBtn.setActionCommand("add");
		addGraphicBtn.setToolTipText("Use \",\" to separate values.");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.BOTH;
		downPanel.add(addGraphicBtn, gbc);

		JScrollPane upScroll = new JScrollPane(upPanel);
		JScrollPane downScroll = new JScrollPane(downPanel);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(upScroll, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(downScroll, BorderLayout.SOUTH);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(mainPanel, BorderLayout.CENTER);
	}

	private void plot() {
		try {
			if (!file.isEmpty()) {
				jgraphic.addGraphic(controller.getX(), controller.getY(), Color.cyan, 10, plotCB.isSelected());
				jgraphic.xLabel = controller.getxName();
				jgraphic.yLabel = controller.getyName();
				jgraphic.title = controller.getTitle();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Load file");
		}
	}

	private void upFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(fileChooser);
		try {
			this.file = fileChooser.getSelectedFile().getAbsolutePath();
			controller.readFile(file);
		} catch (NullPointerException e) {
			System.out.println("No selected file");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}

	private void clean() {
		jgraphic.clean();
	}

	private void showExample() {
		try {
			int n = Integer.parseInt(this.gradoTxt.getText()) + 1;
			double[] x = new double[n];
			double[] y = new double[n];
			int size = 10;

			for (int i = 0; i < n; i++) {
				x[i] = i;
				y[i] = Math.random() * 10;
			}
			if (plotCB.isSelected()) {
				size = 5;
			}
			jgraphic.addGraphic(x, y, new Color((int) (Math.random() * (255)), (int) (Math.random() * (255)),
					(int) (Math.random() * (255))), size, plotCB.isSelected());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Type a value");
		}

	}

	private void gaussExample() {
		try {
			double[] x = new double[800];
			double[] y = new double[800];
			double xI = 0.0;
			int size = (int) ((Math.random() * (15.0 - 1.0)) + 1.0);
			for (int i = 0; i < 800; i++) {
				x[i] = xI;
				xI += 0.01;
			}
			y = controller.getGaussianFunction(x);
			if (!plotCB.isSelected()) {
				double[] xAux = new double[80];
				double[] yAux = new double[80];
				int c = 0;
				for (int i = 0; i < 800; i = i + 10) {
					xAux[c] = x[i];
					yAux[c] = y[i];
					c++;
				}
				x = xAux;
				y = yAux;
			}
			jgraphic.addGraphic(x, y, new Color((int) (Math.random() * (255)), (int) (Math.random() * (255)),
					(int) (Math.random() * (255))), size, plotCB.isSelected());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void cleanFields() {
		xTxt.setText("");
		yTxt.setText("");
	}

	private void addGraphic() {
		try {
			String[] xS = xTxt.getText().split(",");
			String[] yS = yTxt.getText().split(",");
			int xL = xS.length;
			double[] x = new double[xS.length];
			double[] y = new double[yS.length];
			for (int i = 0; i < xL; i++) {
				x[i] = Double.parseDouble(xS[i]);
				y[i] = Double.parseDouble(yS[i]);
			}
			jgraphic.addGraphic(x, y, graphicColor, Integer.parseInt(sizeSpn.getValue().toString()),
					plotCB.isSelected());
			cleanFields();
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void choseColor() {
		Color graphicColorAux = graphicColor;
		graphicColor = JColorChooser.showDialog(null, "Choose a Color", Color.WHITE);
		graphicColor = graphicColor == null ? graphicColorAux : graphicColor;
		colorPanel.setBackground(graphicColor);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		switch (c) {
		case "plot":
			plot();
			break;
		case "show":
			showExample();
			break;
		case "gauss":
			gaussExample();
			break;
		case "upfile":
			upFile();
			break;
		case "clean":
			clean();
			break;
		case "add":
			addGraphic();
			break;
		case "choseColor":
			choseColor();
			break;
		default:
			return;
		}

	}
}
