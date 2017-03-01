package intersection;

public class CarTransporter extends Thread {

	private Street street;
	
	public CarTransporter(Street street){
		super(street.getName()+" transporter");
		setDaemon(false);
		this.street = street;
		
	}
	
	
	public void run(){
		
		while(true){
			try {
				// wait until there is something to transport or
				
				// wait until semaphore is green
				synchronized(street.getSemaphore()){//////////////////////////////////////////////////////////////////
					while(street.getSemaphore().getLight() != Light.green || street.isEmpty()){
						street.getSemaphore().wait();
					}
					// remove car from the queue
					Car car = street.poll();
					
					System.out.println((car.isEmergency()?"Emergency ":"Normal")+" passed semaphore in the street "+street.getName());
					
					if(car.isEmergency()){
						street.getSemaphore().notifyAll();
					}
					
				}
				// average passing time of car through intersection
				sleep(1000);
				
				
			} catch (InterruptedException e) {
				System.out.println("Somebody interruped me... :) ");
				break;
			}
			
		}
		
		System.out.println("Stop transporting cars in the street "+street.getName());
		
	}
	
}
