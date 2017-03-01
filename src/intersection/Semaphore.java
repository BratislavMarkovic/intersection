package intersection;

public class Semaphore {

	private Light light;
	
	public Semaphore(Light light){
		this.light = light;
	}
	
	public Light getLight(){
		return light;
	}
	
	public void setLight(Light light){
		this.light = light;
	}
	
	
}
