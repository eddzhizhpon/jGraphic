package jGraphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Gui extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Controller controller;
	private jGraphic jgraphic;

	private JTextField gradoTxt;
	private JCheckBox plotCB;

	private String file;

	public Gui() {
		initComponent();
		controller = new Controller();
	}

	private void initComponent() {
		setTitle("Test Gui");
		setSize(800, 500);
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
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

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(upPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(downPanel, BorderLayout.SOUTH);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(mainPanel, BorderLayout.CENTER);
	}

	private void plot() {
		try {
			if (!file.isEmpty())
				jgraphic.addGraphic(controller.getX(), controller.getY(), Color.cyan, 10, plotCB.isSelected());
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

			for (int i = 0; i < n; i++) {
				x[i] = i;
				y[i] = Math.random() * 10;
			}
			jgraphic.addGraphic(x, y, new Color((int) (Math.random() * (255)), (int) (Math.random() * (255)),
					(int) (Math.random() * (255))), 10, plotCB.isSelected());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Type a value");
		}

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
		case "upfile":
			upFile();
			break;
		case "clean":
			clean();
			break;
		default:
			return;
		}

	}

}
