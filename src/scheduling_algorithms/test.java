package scheduling_algorithms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*; 

public class test extends JFrame implements ActionListener {

		private Panel mainPanel;
	    private Panel colorPanel;
	    private JTextField redField;
	    private JTextField greenField;
	    private JTextField blueField;
	    private JTextField alphaField;
	    private JButton computeButton;
	    private JButton clearButton;

	    public test() {
	        super("Color Calculator");
	        setSize(400, 320);
	        setLayout(new FlowLayout());

	        mainPanel = new Panel();
	        mainPanel.setLayout(new GridLayout(5, 2, 200, 10)); 

	        //Color Panel
	        colorPanel = new Panel();
	        colorPanel.setPreferredSize(new Dimension(400, 100));
	        colorPanel.setBackground(Color.WHITE);

	        //Red
	        JLabel redLabel = new JLabel("Red:");
	        redField = new JTextField("0", 3);
	        
	        //Green
	        JLabel greenLabel = new JLabel("Green:");
	        greenField = new JTextField("0", 3);
	        
	        //Blue
	        JLabel blueLabel = new JLabel("Blue:");
	        blueField = new JTextField("0", 3);
	        
	        //Alpha
	        JLabel alphaLabel = new JLabel("Alpha:");
	        alphaField = new JTextField("255", 3);

	        //Buttons
	        computeButton = new JButton("Compute");
	        computeButton.addActionListener(this);
	        clearButton = new JButton("Clear");
	        clearButton.addActionListener(this);

	        //
	        mainPanel.add(redLabel);
	        mainPanel.add(redField);
	        
	        mainPanel.add(greenLabel);
	        mainPanel.add(greenField);
	        
	        mainPanel.add(blueLabel);
	        mainPanel.add(blueField);
	        
	        mainPanel.add(alphaLabel);
	        mainPanel.add(alphaField);
	        
	        mainPanel.add(computeButton);
	        mainPanel.add(clearButton);

	        add(mainPanel);
	        add(colorPanel);

	        setVisible(true);
	    }

	    public void actionPerformed(ActionEvent e) {
	        if (e.getSource() == computeButton) {
	            int red = Integer.parseInt(redField.getText());
	            int green = Integer.parseInt(greenField.getText());
	            int blue = Integer.parseInt(blueField.getText());
	            int alpha = Integer.parseInt(alphaField.getText());
	            
	            Color color = new Color(red, green, blue, alpha);
	            colorPanel.setBackground(color);
	       
	            
	        } else if (e.getSource() == clearButton) {
	            colorPanel.setBackground(Color.WHITE);
	            redField.setText("0");
	            greenField.setText("0");
	            blueField.setText("0");
	            alphaField.setText("255");
	        }
	    }

	    public static void main(String[] args) {
	        new test();
	    }
	}
