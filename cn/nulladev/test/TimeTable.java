package cn.nulladev.test;

import java.util.ArrayList;

public class TimeTable {
	
	public final ArrayList<Integer> obs_events;
	public final ArrayList<Integer> arr_events;
	public final ArrayList<Integer> dep_events;
	public final ArrayList<Integer> packet_size;
	public final double maxT;
	
	public TimeTable(double t) {
		obs_events = new ArrayList<Integer>();
		arr_events = new ArrayList<Integer>();
		dep_events = new ArrayList<Integer>();
		packet_size = new ArrayList<Integer>();
		this.maxT = t * 10000; //second to 0.1millisecond
	}
	
	public void createObsPoints(double alpha) {
		alpha /= 1000; //second to millisecond
		for (int time = 0; time < maxT; time++) {
			if (MyUtils.randomU() < alpha) {
				obs_events.add(time);
			}
		}
		obs_events.add(Integer.MAX_VALUE);
	}
	
	public void createArrPoints(double lambda, double length) {
		lambda /= 10000; //second to 0.1millisecond
		for (int time = 0; time < maxT; time++) {
			if (MyUtils.randomU() < lambda) {
				arr_events.add(time);
				packet_size.add((int)Math.ceil(MyUtils.randomE(1D/length)));
			}
		}
		arr_events.add(Integer.MAX_VALUE);
	}
	
	public void calcDepPoints(double speed) {
		speed /= 10000; //second to 0.1millisecond
		int time = 0;
		for (int i = 0; i < arr_events.size() - 1; i++) {
			time = Math.max(arr_events.get(i), time);
			time += Math.ceil(packet_size.get(i)/speed);
			dep_events.add(time);
		}
		dep_events.add(Integer.MAX_VALUE);
	}
	
	public void calcDepPointsWithLoss(double speed, int k) {
		speed /= 10000; //second to 0.1millisecond
		int time = 0;
		int loss = 0;
		for (int i = 0; i < arr_events.size() - 1; i++) {
			if (i>=k && arr_events.get(i) <= dep_events.get(i-k-loss)) {
				loss++;
				continue; //Packet loss
			}
			time = Math.max(arr_events.get(i), time);
			time += Math.ceil(packet_size.get(i)/speed);
			dep_events.add(time);
		}
		dep_events.add(Integer.MAX_VALUE);
	}
	
	public void simulate() {
		int o = 0;
		int a = 0;
		int d = 0;
		double total_num = 0;
		double idle_num = 0;
		while (o < obs_events.size() || a < arr_events.size() || d < dep_events.size()) {
			if (obs_events.get(o) == Integer.MAX_VALUE && arr_events.get(a) == Integer.MAX_VALUE && dep_events.get(d) == Integer.MAX_VALUE) {
				break; //sorting complete
			}
			if (arr_events.get(a) <= dep_events.get(d) && arr_events.get(a) <= obs_events.get(o)) {
				//System.out.print("(" + arr_events.get(a) + ")");
				//System.out.println("Event A" + a + ": Receive packet P" + a + ", size:" + packet_size.get(a) + "bits");
				a++;
				continue;
			}
			if (dep_events.get(d) <= obs_events.get(o)) {
				//System.out.print("(" + dep_events.get(d) + ")");
				//System.out.println("Event D" + d + ": Packet P" + d + " departed.");
				d++;
				continue;
			}
			//System.out.print("(" + obs_events.get(o) + ")");
			//System.out.println("Event O" + o + ": Observing current queue number is:" + (a-d));
			if (a-d > 0) {
				total_num += (a-d);
			} else if (a-d == 0){
				idle_num++;
			} else {
				System.out.println("ERROR!!");
			}
			o++;
		}
		System.out.println("arrive amount:" + (arr_events.size() - 1));
		System.out.println("idle rate:" + idle_num / (obs_events.size() - 1));
		System.out.println("aver size:" + total_num / (obs_events.size() - 1));
	}
	
	public void simulateWithLoss(int k) {
		int o = 0;
		int a = 0;
		int d = 0;
		double total_num = 0;
		double idle_num = 0;
		int loss_num = 0;
		while (o < obs_events.size() || a < arr_events.size() || d < dep_events.size()) {
			if (obs_events.get(o) == Integer.MAX_VALUE && arr_events.get(a) == Integer.MAX_VALUE && dep_events.get(d) == Integer.MAX_VALUE) {
				break; //sorting complete
			}
			if (arr_events.get(a) <= dep_events.get(d) && arr_events.get(a) <= obs_events.get(o)) {
				if (a - d - loss_num >= k) {
					loss_num++;
					a++;
					continue;
				}
				a++;
				continue;
			}
			if (dep_events.get(d) <= obs_events.get(o)) {
				d++;
				continue;
			}
			if (a-d-loss_num > 0) {
				total_num += (a-d-loss_num);
			} else if (a-d-loss_num == 0){
				idle_num++;
			} else {
				System.out.println("ERROR!!");
			}
			o++;
		}
		System.out.println("idle rate:" + idle_num / (obs_events.size() - 1));
		System.out.println("aver size:" + total_num / (obs_events.size() - 1));
		System.out.println("loss rate:" + (double)loss_num / (arr_events.size() - 1));
	}

}
