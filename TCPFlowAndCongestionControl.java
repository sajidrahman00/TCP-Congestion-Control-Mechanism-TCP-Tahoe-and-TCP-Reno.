package pkg456;

import java.util.*;

public class TCPFlowAndCongestionControl {

    private int cwnd;           // Congestion window size
    private int ssthresh;       // Slow start threshold
    private int rtt;            // Round-trip time
    private boolean congestion; // Congestion flag

    public TCPFlowAndCongestionControl(int init_ssthresh) {
        cwnd = 1;
        ssthresh = init_ssthresh;
        congestion = false;
        rtt = 0;
    }

    public void run(int dataLength) {
        System.out.println("Connected to the Server... ...");
        System.out.println("Your data is started to be sent ... ");

        int dataSeqNum = 0;

        while (dataSeqNum < dataLength) {
            this.rtt++;
            System.out.println();
            System.out.println("Data sending in RTT number " + this.rtt);
            System.out.println("−−−−−−−−−−−−−−−−−−−−−−−−−−−−");

            // Flow Control
            flowControl();

            System.out.println("Data from " + (dataSeqNum + 1) + " − " + (dataSeqNum + cwnd) + " is being sent now... ...\n\n");

            // Congestion Control
            if (!receiveAcknowledgment()) {
                congestion = true;
                System.out.println("... but wait! Congestion has been detected!");
                if (timeout()) {
                    handleTimeoutCongestion();
                } else {
                    handle3DupAckCongestion();
                }
            } else {
                congestion = false;
            }

            dataSeqNum += cwnd;
        }

        System.out.println("\n\nYour data sending is completed. No more data to send."
                + "\nCongestion Control mechanism concludes.\nIt took " + this.rtt + " transmission rounds to send the whole data.");
    }

    private void flowControl() {
        System.out.println("previous cwnd size: " + cwnd);
        System.out.println("updated ssthresh value: " + ssthresh);

        if (!congestion) {
            if (cwnd < ssthresh) {
                // Slow Start Phase
                // Exponentially increase of cwnd
                cwnd = cwnd * 2;
                System.out.println("...SS phase running...");
            } else if (cwnd >= ssthresh) {
                // Congestion Avoidance Phase
                // Linearly increase of cwnd
                cwnd = cwnd + 1;
                System.out.println("...CA phase running...");
            }
        }

        System.out.println("updated cwnd size: " + cwnd);
    }

    private boolean receiveAcknowledgment() {
        // Simulating acknowledgment reception
        // Returns true if no congestion and data is received by the receiver.
        // Returns false if congestion occurred and data is not received by the receiver.

        Random ack = new Random();
        return ack.nextBoolean();
    }

    private boolean timeout() {
        // Simulating timeout event
        // Returns true if congestion is detected using timeout.

        Random rttRandom = new Random();
        return rttRandom.nextBoolean();
    }

    private void handleTimeoutCongestion() {
        System.out.println("\n\nTimeout occurred. Handling Timeout based congestion: cwnd value will become 1.");

        ssthresh = cwnd / 2;
        if (ssthresh == 0) ssthresh = 1; // Making ssthresh 1 if it comes as zero.
        cwnd = 1;

        retransmitPacket();
    }

    private void handle3DupAckCongestion() {
        System.out.println("\n\nHandling Triple Dup Ack based congestion: cwnd value will be halved.");
        ssthresh = cwnd / 2;
        if (ssthresh == 0) ssthresh = 1; // Making ssthresh 1 if it comes as zero.
        cwnd = ssthresh;

        retransmitPacket();
    }

    private void retransmitPacket() {
        congestion = false;
        System.out.println("\nRetransmitting the lost packet now after handling.\n");
    }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Please input the initial ssthresh value: ");
        int ssthresh = scn.nextInt();

        System.out.println("Enter the length of your data: ");
        int dataLength = scn.nextInt();

        TCPFlowAndCongestionControl simulation = new TCPFlowAndCongestionControl(ssthresh);
        simulation.run(dataLength);
    }
}