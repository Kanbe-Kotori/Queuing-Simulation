package cn.nulladev.test;

import java.util.Scanner;

public class Main {
	public static final int AMOUNT = 1000;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input the total simulation time T. (Unit: seconds)");
		double t = sc.nextDouble();
		System.out.println("Please input the average number of packets arrived per second ¦Ë. (Unit: times/second)");
		double lambda = sc.nextDouble();
		System.out.println("Please input the average length of a packet in bits L. (Unit: bits)");
		double l = sc.nextDouble();
		System.out.println("Please input the average number of observer events per second ¦Á. (Unit: times/second)");
		double alpha = sc.nextDouble();
		System.out.println("Please input the transmission rate of the output link in bits per second C. (Unit: bits/second)");
		double c = sc.nextDouble();
		System.out.println("Please input the queue size k. (Unit: packets)");
		int k = sc.nextInt();
		sc.close();
		System.out.println("Input end. Parameter:");
		System.out.println("T=" + t + "s, ¦Ë=" + lambda + "times/s, L=" + l + "bits, ¦Á=" + alpha + "times/s, c=" + c + "bits/s, k=" + k + "packets."); 
		
		TimeTable table = new TimeTable(t);
		table.createObsPoints(alpha);
		table.createArrPoints(lambda, l);
		//table.calcDepPoints(c);
		//table.simulate();
		table.calcDepPointsWithLoss(c,k);
		table.simulateWithLoss(k);
		
		/*
		for (int p = 500; p<=1000; p+=40) {
			TimeTable table = new TimeTable(10000);
			table.createObsPoints(20);
			table.createArrPoints(p, 10000);
			System.out.println("p="+p+",k=5------");
			table.calcDepPointsWithLoss(1000000, 5);
			table.simulateWithLoss(5);
			table.dep_events.clear();
			System.out.println("p="+p+",k=10------");
			table.calcDepPointsWithLoss(1000000, 10);
			table.simulateWithLoss(10);
			table.dep_events.clear();
			System.out.println("p="+p+",k=40------");
			table.calcDepPointsWithLoss(1000000, 40);
			table.simulateWithLoss(40);
		}
		*/
	}
	
}
