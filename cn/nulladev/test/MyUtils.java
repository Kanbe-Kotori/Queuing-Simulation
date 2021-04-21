package cn.nulladev.test;

import java.util.Random;

public class MyUtils {
	
	public static final Random random = new Random();
	
	public static double randomU() {
		return random.nextDouble();
	}
	
	public static double randomE(double lambda) {
		double ran = randomU();
		return - 1/lambda * Math.log(ran);
	}

}
