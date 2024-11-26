package scheduling_algorithms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class ScanScheduling extends JFrame implements ActionListener {

    private JTextField requestField;
    private JTextField headPositionField;
    private JTextField directionField;
    private JTextField diskSizeField;
    private JTextArea resultArea;
    private JButton submitButton;

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
        resultArea = new JTextArea(15, 60);
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
            int diskSize = Integer.parseInt(diskSizeField.getText().trim());

            if (!direction.equals("up") && !direction.equals("down")) {
                resultArea.setText("Error: Direction must be 'up' or 'down'.");
                return;
            }

            // SCAN Algorithm Logic
            Arrays.sort(requests);
            int totalHeadMovement = 0;
            StringBuilder processingOrder = new StringBuilder("Processing Order:\n");

            if (direction.equals("up")) {
                // Process requests greater than or equal to the head
                for (int request : requests) {
                    if (request >= head) {
                        processingOrder.append(request).append(" ");
                        totalHeadMovement += Math.abs(request - head);
                        head = request;
                    }
                }

                // Go to the disk's maximum position
                totalHeadMovement += Math.abs(diskSize - head);
                head = diskSize;

                // Process remaining requests in reverse
                for (int i = requests.length - 1; i >= 0; i--) {
                    if (requests[i] < head) {
                        processingOrder.append(requests[i]).append(" ");
                        totalHeadMovement += Math.abs(requests[i] - head);
                        head = requests[i];
                    }
                }
            } else { // Direction is "down"
                // Process requests less than or equal to the head
                for (int i = requests.length - 1; i >= 0; i--) {
                    if (requests[i] <= head) {
                        processingOrder.append(requests[i]).append(" ");
                        totalHeadMovement += Math.abs(requests[i] - head);
                        head = requests[i];
                    }
                }

                // Go to the disk's minimum position (0)
                totalHeadMovement += head;
                head = 0;

                // Process remaining requests in ascending order
                for (int request : requests) {
                    if (request > head) {
                        processingOrder.append(request).append(" ");
                        totalHeadMovement += Math.abs(request - head);
                        head = request;
                    }
                }
            }

            // Display Results
            processingOrder.append("\nTotal Head Movement: ").append(totalHeadMovement);
            resultArea.setText(processingOrder.toString());
        } catch (Exception ex) {
            resultArea.setText("Error: Invalid input. Please ensure correct formatting.");
        }
    }

    public static void main(String[] args) {
        new ScanScheduling();
    }
}
