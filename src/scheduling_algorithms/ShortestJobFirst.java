package scheduling_algorithms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ShortestJobFirst extends JFrame implements ActionListener {

    private JTextField processesField;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTextArea resultArea;
    private JButton submitButton;

    public ShortestJobFirst() {
        super("Shortest Job First Scheduling");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel with GridLayout
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Number of Processes
        JLabel processesLabel = new JLabel("Number of Processes:");
        processesField = new JTextField();

        // Arrival Time
        JLabel arrivalTimeLabel = new JLabel("Arrival Times (comma-separated):");
        arrivalTimeField = new JTextField();

        // Burst Time
        JLabel burstTimeLabel = new JLabel("Burst Times (comma-separated):");
        burstTimeField = new JTextField();

        // Compute Button
        submitButton = new JButton("Compute");
        submitButton.addActionListener(this);

        // Add components to the input panel
        inputPanel.add(processesLabel);
        inputPanel.add(processesField);
        inputPanel.add(arrivalTimeLabel);
        inputPanel.add(arrivalTimeField);
        inputPanel.add(burstTimeLabel);
        inputPanel.add(burstTimeField);
        inputPanel.add(new JLabel()); // Empty cell for alignment
        inputPanel.add(submitButton);

        // Result Area
        resultArea = new JTextArea(15, 50);
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
            // Read input
            int n = Integer.parseInt(processesField.getText().trim());
            String[] arrivalTimes = arrivalTimeField.getText().trim().split(",");
            String[] burstTimes = burstTimeField.getText().trim().split(",");

            if (arrivalTimes.length != n || burstTimes.length != n) {
                resultArea.setText("Error: Number of processes must match the input sizes.");
                return;
            }

            int[] arrivalTime = new int[n];
            int[] burstTime = new int[n];
            int[] waitingTime = new int[n];
            int[] turnAroundTime = new int[n];
            boolean[] completed = new boolean[n];

            for (int i = 0; i < n; i++) {
                arrivalTime[i] = Integer.parseInt(arrivalTimes[i].trim());
                burstTime[i] = Integer.parseInt(burstTimes[i].trim());
            }

            // Scheduling Logic
            int currentTime = 0, completedCount = 0;

            while (completedCount < n) {
                int minBurst = Integer.MAX_VALUE;
                int selectedProcess = -1;

                for (int i = 0; i < n; i++) {
                    if (!completed[i] && arrivalTime[i] <= currentTime && burstTime[i] < minBurst) {
                        minBurst = burstTime[i];
                        selectedProcess = i;
                    }
                }

                if (selectedProcess != -1) {
                    currentTime += burstTime[selectedProcess];
                    turnAroundTime[selectedProcess] = currentTime - arrivalTime[selectedProcess];
                    waitingTime[selectedProcess] = turnAroundTime[selectedProcess] - burstTime[selectedProcess];
                    completed[selectedProcess] = true;
                    completedCount++;
                } else {
                    currentTime++;
                }
            }

            // Display 
            StringBuilder result = new StringBuilder();
            result.append("Process\tArrival Time\tBurst Time\tTurnaround Time\tWaiting Time\n");
            for (int i = 0; i < n; i++) {
                result.append("P").append(i + 1).append("\t")
                        .append(arrivalTime[i]).append("\t")
                        .append(burstTime[i]).append("\t")
                        .append(turnAroundTime[i]).append("\t\t")
                        .append(waitingTime[i]).append("\n");
            }
            resultArea.setText(result.toString());
        } catch (Exception ex) {
            resultArea.setText("Error: Invalid input. Please ensure correct formatting.");
        }
    }

    public static void main(String[] args) {
        new ShortestJobFirst();
    }
}
