package scheduling_algorithms;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduling extends JFrame implements ActionListener {

    private JTextField processesField;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTextField quantumField;
    private JTextArea resultArea;
    private JButton submitButton;

    public RoundRobinScheduling() {
        super("Round Robin Scheduling");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
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

        // Quantum Time
        JLabel quantumLabel = new JLabel("Quantum Time:");
        quantumField = new JTextField();

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
        inputPanel.add(quantumLabel);
        inputPanel.add(quantumField);
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
            // Read inputs
            int n = Integer.parseInt(processesField.getText().trim());
            String[] arrivalTimes = arrivalTimeField.getText().trim().split(",");
            String[] burstTimes = burstTimeField.getText().trim().split(",");
            int quantum = Integer.parseInt(quantumField.getText().trim());

            if (arrivalTimes.length != n || burstTimes.length != n) {
                resultArea.setText("Error: Number of processes must match the input sizes.");
                return;
            }

            int[] arrivalTime = new int[n];
            int[] burstTime = new int[n];
            int[] remainingTime = new int[n];
            int[] completionTime = new int[n];
            int[] waitingTime = new int[n];
            int[] turnAroundTime = new int[n];

            for (int i = 0; i < n; i++) {
                arrivalTime[i] = Integer.parseInt(arrivalTimes[i].trim());
                burstTime[i] = Integer.parseInt(burstTimes[i].trim());
                remainingTime[i] = burstTime[i];
            }

            // Scheduling Logic
            int currentTime = 0;
            Queue<Integer> queue = new LinkedList<>();
            boolean[] added = new boolean[n]; // To track processes added to the queue
            int completedCount = 0;

            while (completedCount < n) {
                // Add processes to the queue based on arrival time
                for (int i = 0; i < n; i++) {
                    if (!added[i] && arrivalTime[i] <= currentTime) {
                        queue.add(i);
                        added[i] = true;
                    }
                }

                if (queue.isEmpty()) {
                    currentTime++;
                    continue;
                }

                int process = queue.poll();

                if (remainingTime[process] > 0) {
                    if (remainingTime[process] > quantum) {
                        currentTime += quantum;
                        remainingTime[process] -= quantum;
                        queue.add(process); // Re-add process to the queue
                    } else {
                        currentTime += remainingTime[process];
                        remainingTime[process] = 0;
                        completionTime[process] = currentTime;
                        waitingTime[process] = completionTime[process] - burstTime[process] - arrivalTime[process];
                        turnAroundTime[process] = completionTime[process] - arrivalTime[process];
                        completedCount++;
                    }
                }
            }

            // Display Results
            StringBuilder result = new StringBuilder();
            result.append("Process\tArrival Time\tBurst Time\tCompletion Time\tTurnaround Time\tWaiting Time\n");
            for (int i = 0; i < n; i++) {
                result.append("P").append(i + 1).append("\t")
                        .append(arrivalTime[i]).append("\t")
                        .append(burstTime[i]).append("\t")
                        .append(completionTime[i]).append("\t\t")
                        .append(turnAroundTime[i]).append("\t\t")
                        .append(waitingTime[i]).append("\n");
            }
            resultArea.setText(result.toString());
        } catch (Exception ex) {
            resultArea.setText("Error: Invalid input. Please ensure correct formatting.");
        }
    }

    public static void main(String[] args) {
        new RoundRobinScheduling();
    }
}
