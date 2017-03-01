package intersection;

import java.util.HashMap;
import java.util.Map;

public class SemaphoreControler extends Thread {

	private Object check ;
	private Street[] streets;
	
	public SemaphoreControler(Object check, Street...streets){
		super("Semaphore check");
		setDaemon(false);
		this.check = check;
		this.streets = streets;
	}
	
	
	public void run(){
		
		long lastChange = System.currentTimeMillis();
		
		while(true){
			try {
				synchronized(check){
					boolean newCars = false;
					for(Street street : streets){	// check for all streets(directions) if any street contains cars
						if(!street.isCheckCompleted()){
							newCars = true;
							break;
						}
					}
					if(!newCars){
						check.wait();
					}
					
				}
				
				
				// first check if any queue contains emergency car
				for(Street street : streets){
					Map<Semaphore,Light> previousState = null;
					
					synchronized(street.getSemaphore()){
						if(street.hasEmergencyCar()){
							if(street.getSemaphore().getLight() != Light.green){
								System.out.println("Changing light for emergency car");
								previousState = turnYellow();
							}
						} else {
							continue;//else if regular car...
						}
					}
					
					if(previousState != null){
						sleep(1000);
						toggleLight(previousState);
						lastChange = System.currentTimeMillis();
					}
					
					synchronized (street.getSemaphore()) {
						while(street.hasEmergencyCar()){
							street.getSemaphore().wait();
						}

					}
						
				}
				
				// then check time lapse
				long checkTime = System.currentTimeMillis();
				if(checkTime - lastChange >= 4500){
					System.out.println("Elapsed "+(checkTime-lastChange)+" miliseconds.");
					
					lastChange = System.currentTimeMillis();
					
					Map<Semaphore,Light> previousState = turnYellow();
					
					sleep(1000);
					
					toggleLight(previousState);
					
					
					
				}
					
				
			
			} catch (InterruptedException e) {
				System.out.println("Somebody interruped me :) ");
				break;
			}
			
			
			
		}
		
		
	}
	
	
	private Map<Semaphore,Light> turnYellow() throws InterruptedException {
		
		Map<Semaphore,Light> previouState = new HashMap<Semaphore,Light>();
		for(Street street : streets){
			if(!previouState.containsKey(street.getSemaphore())){
				previouState.put(street.getSemaphore(), street.getSemaphore().getLight());
				street.getSemaphore().setLight(Light.yellow);
			}
		}
		System.out.println("Turning yellow on");
		return previouState;

	}
	
	public void toggleLight(Map<Semaphore,Light> previouState){
		
		for(Semaphore semaphore : previouState.keySet()){
			Light newLight = previouState.get(semaphore) == Light.red ? Light.green : Light.red;
			semaphore.setLight(newLight);
			for(Street street : streets){
				if(street.getSemaphore() == semaphore){
					System.out.println("Street "+street.getName()+" has "+street.getSemaphore().getLight());
				}
			}
			if(newLight == Light.green){
				synchronized (semaphore) {//////////////////////////////////////////////////////////////////
					semaphore.notifyAll();
				}
			}
		}
	
		
	}
	
	
}
