package scheduling_algorithms;
import java.util.Scanner;
public class test2 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
    
            System.out.print("Enter the number of processes: ");
            int n = sc.nextInt();
    
            int[] burstTime = new int[n];
            int[] arrivalTime = new int[n];
            int[] waitingTime = new int[n];
            int[] turnAroundTime = new int[n];
            boolean[] completed = new boolean[n];
    
            System.out.println("Enter the arrival times for each process:");
            for (int i = 0; i < n; i++) {
                System.out.print("Process " + (i + 1) + " Arrival Time: ");
                arrivalTime[i] = sc.nextInt();
            }
    
            System.out.println("Enter the burst times for each process:");
            for (int i = 0; i < n; i++) {
                System.out.print("Process " + (i + 1) + " Burst Time: ");
                burstTime[i] = sc.nextInt();
            }
    
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
    
            System.out.println("\nProcess\tBurst Time\tArrival Time\tWaiting Time\tTurnaround Time");
            for (int i = 0; i < n; i++) {
                System.out.println("P" + (i + 1) + "\t" + burstTime[i] + "\t\t" + arrivalTime[i] + "\t\t" + waitingTime[i] + "\t\t" + turnAroundTime[i]);
            }
            sc.close();
        }
    }

