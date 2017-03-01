package intersection;

import java.util.Timer;
import java.util.TimerTask;

public class TraficControl {

	public static void main(String[] args) {

		
		Semaphore s1 = new Semaphore(Light.green);
		Semaphore s2 = new Semaphore(Light.red);
		
		Street ns = new Street("North Sauth", s1);
		Street sn = new Street("South North", s1);
		Street ew = new Street("East West", s2);
		Street we = new Street("West East",s2);
		
		Object check = new Object();
		
		new CarGenerator(check,ns,sn,ew,we).start();
		new CarTransporter(ns).start();
		new CarTransporter(sn).start();
		new CarTransporter(ew).start();
		new CarTransporter(we).start();
		
		new SemaphoreControler(check,ns,sn,ew,we).start();
		
		new Timer(false).scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				synchronized (check) {
					check.notify();
				}
			}
		}, 5000, 5000);
		
	}

}
