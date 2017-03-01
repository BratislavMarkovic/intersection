package intersection;

import java.util.Random;

public class CarGenerator extends Thread {

	private Street [] streets;
	private Object check;
	private Random genTime;
	private Random genStreet;
	private Random genEmergency;
	
	public CarGenerator(Object check,Street... streets){
		super("CarGenerator");
		setDaemon(false);
		this.genTime = new Random(System.currentTimeMillis());
		this.genStreet = new Random(System.currentTimeMillis());
		this.genEmergency = new Random(System.currentTimeMillis());
		this.streets = streets;
		this.check = check;
	}
	
	@Override
	public void run() {
		
		while(true){
			
			try {
				
				int waitFor = (genTime.nextInt(10)+1) * 1000;
				sleep(waitFor);
				
				
				boolean emergency = (genEmergency.nextInt(5) > 3);
				
				Car car = new Car(emergency);
				
				int street = genStreet.nextInt(streets.length);///chooses one street randomly
			
				synchronized(streets[street].getSemaphore()){/////////////////////////////////
					
					streets[street].add(car);
					streets[street].getSemaphore().notify();/////
					
					System.out.println((car.isEmergency()?"Emergency ":"Normal")+" car are comming from street "+streets[street].getName());
					
				}
				
				synchronized(check){

					check.notify();
					
				}
				
				
			} catch (InterruptedException e) {
				System.out.println("Somebody interruped me :) ");
				break;
			}
			
		}
		
		System.out.println("Stop generating cars...");
		
	}
	
}
