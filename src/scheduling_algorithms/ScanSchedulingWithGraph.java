package scheduling_algorithms;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class ScanSchedulingWithGraph extends JFrame implements ActionListener {

    private JTextField requestField;
    private JTextField headPositionField;
    private JTextField directionField;
    private JTextField diskSizeField;
    private JTextArea resultArea;
    private JButton submitButton;

    private ArrayList<Integer> headMovementPath;
    private int diskSize;

    public ScanSchedulingWithGraph() {
        super("SCAN Disk Scheduling");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Requests
        JLabel requestLabel = new JLabel("Request Sequence (comma-separated):");
        requestField = new JTextField();

        // Initial Head Position
        JLabel headPositionLabel = new JLabel("Initial Head Position:");
        headPositionField = new JTextField();

        // Direction
        JLabel directionLabel = new JLabel("Direction (up/down):");
        directionField = new JTextField();

        // Disk Size
        JLabel diskSizeLabel = new JLabel("Maximum Disk Size:");
        diskSizeField = new JTextField();

        // Compute Button
        submitButton = new JButton("Compute");
        submitButton.addActionListener(this);

        // Add components to the input panel
        inputPanel.add(requestLabel);
        inputPanel.add(requestField);
        inputPanel.add(headPositionLabel);
        inputPanel.add(headPositionField);
        inputPanel.add(directionLabel);
        inputPanel.add(directionField);
        inputPanel.add(diskSizeLabel);
        inputPanel.add(diskSizeField);
        inputPanel.add(new JLabel()); // Empty cell for alignment
        inputPanel.add(submitButton);

        // Result Area
        resultArea = new JTextArea(10, 60);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Parse input
            String[] requestStrings = requestField.getText().trim().split(",");
            int[] requests = new int[requestStrings.length];
            for (int i = 0; i < requestStrings.length; i++) {
                requests[i] = Integer.parseInt(requestStrings[i].trim());
            }

            int head = Integer.parseInt(headPositionField.getText().trim());
            String direction = directionField.getText().trim().toLowerCase();
            diskSize = Integer.parseInt(diskSizeField.getText().trim());

            if (!direction.equals("up") && !direction.equals("down")) {
                resultArea.setText("Error: Direction must be 'up' or 'down'.");
                return;
            }

            // SCAN Algorithm Logic
            Arrays.sort(requests);
            headMovementPath = new ArrayList<>();
            headMovementPath.add(head);

            int totalHeadMovement = 0;
            StringBuilder processingOrder = new StringBuilder("Processing Order:\n");

            if (direction.equals("up")) {
                for (int request : requests) {
                    if (request >= head) {
                        processingOrder.append(request).append(" ");
                        totalHeadMovement += Math.abs(request - head);
                        head = request;
                        headMovementPath.add(head);
                    }
                }
                totalHeadMovement += Math.abs(diskSize - head);
                head = diskSize;
                headMovementPath.add(head);
                for (int i = requests.length - 1; i >= 0; i--) {
                    if (requests[i] < head) {
                        processingOrder.append(requests[i]).append(" ");
                        totalHeadMovement += Math.abs(requests[i] - head);
                        head = requests[i];
                        headMovementPath.add(head);
                    }
                }
            } else { // Direction is "down"
                for (int i = requests.length - 1; i >= 0; i--) {
                    if (requests[i] <= head) {
                        processingOrder.append(requests[i]).append(" ");
                        totalHeadMovement += Math.abs(requests[i] - head);
                        head = requests[i];
                        headMovementPath.add(head);
                    }
                }
                totalHeadMovement += head;
                head = 0;
                headMovementPath.add(head);
                for (int request : requests) {
                    if (request > head) {
                        processingOrder.append(request).append(" ");
                        totalHeadMovement += Math.abs(request - head);
                        head = request;
                        headMovementPath.add(head);
                    }
                }
            }

            // Display Results
            processingOrder.append("\nTotal Head Movement: ").append(totalHeadMovement);
            resultArea.setText(processingOrder.toString());

            // Show Graph in a Separate Frame
            showGraphFrame();

        } catch (Exception ex) {
            resultArea.setText("Error: Invalid input. Please ensure correct formatting.");
        }
    }

    private void showGraphFrame() {
        JFrame graphFrame = new JFrame("SCAN Algorithm Graph");
        graphFrame.setSize(600, 400);
        graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GraphPanel graphPanel = new GraphPanel();
        graphPanel.setHeadMovementPath(headMovementPath, diskSize);

        graphFrame.add(graphPanel);
        graphFrame.setVisible(true);
    }
    private class GraphPanel extends JPanel {
        private ArrayList<Integer> headMovementPath;
        private int diskSize;
    
        public void setHeadMovementPath(ArrayList<Integer> headMovementPath, int diskSize) {
            this.headMovementPath = headMovementPath;
            this.diskSize = diskSize;
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (headMovementPath == null || headMovementPath.isEmpty()) {
                return;
            }
    
            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int graphWidth = width - 2 * padding;
            int graphHeight = height - 2 * padding;
    
            // Draw axes
            g.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
            g.drawLine(padding, padding, padding, height - padding); // Y-axis
    
            // Add labels to the axes
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i <= diskSize; i += diskSize / 10) {
                int x = padding + (i * graphWidth) / diskSize;
                g.drawLine(x, height - padding, x, height - padding + 5);
                g.drawString(String.valueOf(i), x - 10, height - padding + 20); // X-axis labels
            }
    
            for (int i = 0; i < headMovementPath.size(); i++) {
                int y = height - padding - (i * graphHeight) / (headMovementPath.size() - 1);
                g.drawLine(padding - 5, y, padding, y);
                g.drawString("Req " + i, padding - 35, y + 5); // Y-axis labels
            }
    
            // Draw head movement path
            int prevX = padding + (headMovementPath.get(0) * graphWidth) / diskSize;
            int prevY = height - padding;
            g.setColor(Color.BLUE);
            for (int i = 1; i < headMovementPath.size(); i++) {
                int currentX = padding + (headMovementPath.get(i) * graphWidth) / diskSize;
                int currentY = height - padding - (i * graphHeight) / (headMovementPath.size() - 1);
                g.drawLine(prevX, prevY, currentX, currentY);
                prevX = currentX;
                prevY = currentY;
            }
    
            // Plot points on the graph
            g.setColor(Color.RED);
            for (int i = 0; i < headMovementPath.size(); i++) {
                int x = padding + (headMovementPath.get(i) * graphWidth) / diskSize;
                int y = height - padding - (i * graphHeight) / (headMovementPath.size() - 1);
                g.fillOval(x - 4, y - 4, 8, 8);
            }
        }
    }
    

    public static void main(String[] args) {
        new ScanSchedulingWithGraph();
    }
}
