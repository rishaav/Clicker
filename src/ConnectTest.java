
public class ConnectTest {

	
	public String getVals(String className, String sessionName){ 
		
		return "Select questions FROM "  + sessionName + " WHERE " + " CLASS IS " + className; 
		
	}
	
	
	
	
}
