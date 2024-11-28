package scheduling_algorithms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class ScanScheduling2 extends JFrame implements ActionListener {

    private JTextField requestField;
    private JTextField headPositionField;
    private JTextField directionField;
    private JTextField diskSizeField;
    private JTextArea resultArea;
    private JButton submitButton;
    private GraphPanel graphPanel;

    public ScanScheduling() {
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

        // Graph Panel
        graphPanel = new GraphPanel();

        // Add components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(graphPanel, BorderLayout.SOUTH);

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
            int diskSize = Integer.parseInt(diskSizeField.getText().trim());

            if (!direction.equals("up") && !direction.equals("down")) {
                resultArea.setText("Error: Direction must be 'up' or 'down'.");
                return;
            }

            // SCAN Algorithm Logic
            Arrays.sort(requests);
            int totalHeadMovement = 0;
            StringBuilder processingOrder = new StringBuilder("Processing Order:\n");
            java.util.List<Integer> headMovements = new java.util.ArrayList<>();
            headMovements.add(head);

            if (direction.equals("up")) {
                for (int request : requests) {
                    if (request >= head) {
                        processingOrder.append(request).append(" ");
                        totalHeadMovement += Math.abs(request - head);
                        head = request;
                        headMovements.add(head);
                    }
                }

                totalHeadMovement += Math.abs(diskSize - head);
                head = diskSize;
                headMovements.add(head);

                for (int i = requests.length - 1; i >= 0; i--) {
                    if (requests[i] < head) {
                        processingOrder.append(requests[i]).append(" ");
                        totalHeadMovement += Math.abs(requests[i] - head);
                        head = requests[i];
                        headMovements.add(head);
                    }
                }
            } else {
                for (int i = requests.length - 1; i >= 0; i--) {
                    if (requests[i] <= head) {
                        processingOrder.append(requests[i]).append(" ");
                        totalHeadMovement += Math.abs(requests[i] - head);
                        head = requests[i];
                        headMovements.add(head);
                    }
                }

                totalHeadMovement += head;
                head = 0;
                headMovements.add(head);

                for (int request : requests) {
                    if (request > head) {
                        processingOrder.append(request).append(" ");
                        totalHeadMovement += Math.abs(request - head);
                        head = request;
                        headMovements.add(head);
                    }
                }
            }

            // Display Results
            processingOrder.append("\nTotal Head Movement: ").append(totalHeadMovement);
            resultArea.setText(processingOrder.toString());

            // Update Graph
            graphPanel.setHeadMovements(headMovements, diskSize);
        } catch (Exception ex) {
            resultArea.setText("Error: Invalid input. Please ensure correct formatting.");
        }
    }

    private class GraphPanel extends JPanel {
        private java.util.List<Integer> headMovements = new java.util.ArrayList<>();
        private int diskSize;

        public void setHeadMovements(java.util.List<Integer> headMovements, int diskSize) {
            this.headMovements = headMovements;
            this.diskSize = diskSize;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (headMovements.isEmpty()) {
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int graphHeight = height - 2 * padding;
            int graphWidth = width - 2 * padding;

            int yScale = graphHeight / diskSize;
            int xSpacing = graphWidth / Math.max(1, headMovements.size() - 1);

            g.setColor(Color.BLACK);
            g.drawLine(padding, padding, padding, height - padding);
            g.drawLine(padding, height - padding, width - padding, height - padding);

            int prevX = padding;
            int prevY = height - padding - (headMovements.get(0) * yScale);

            g.setColor(Color.BLUE);
            for (int i = 1; i < headMovements.size(); i++) {
                int currX = padding + i * xSpacing;
                int currY = height - padding - (headMovements.get(i) * yScale);

                g.drawLine(prevX, prevY, currX, currY);
                prevX = currX;
                prevY = currY;
            }
        }
    }

    public static void main(String[] args) {
        new ScanScheduling2();
    }
}
