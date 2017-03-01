package intersection;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Street {

	private Queue<Car> cars;
	private String name;
	private Semaphore semaphore;
	private boolean checkCompleted;
	
	public Street(String name,Semaphore semaphore){
		this.name = name;
		this.semaphore = semaphore;
		this.cars = new ConcurrentLinkedQueue<Car>();
		checkCompleted = false;
	}

	public boolean isEmpty(){
		return cars.isEmpty();
	}
	
	public Car poll(){
		return cars.poll();
	}
	
	public void add(Car car){
		cars.add(car);
		checkCompleted = false;
	}
	
	public String getName() {
		return name;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}
	
	public boolean hasEmergencyCar(){
		checkCompleted = true;
		for(Car car : cars){
			if(car.isEmergency()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isCheckCompleted(){
		return checkCompleted;
	}
	
}
