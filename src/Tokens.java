import java.util.HashMap;

public class Tokens {
	private String objectClass;
	private HashMap<String, String> objectValues = new HashMap<String, String>();
	
	public Tokens(String name){
		this.objectClass = name;
	}
	public void add(String key, String value){
		objectValues.put(key, value);
	}
	public String getObjectClass(){
		return objectClass;
	}
	public String getValueOf(String key) {
		return objectValues.get(key);
	}
	public HashMap<String, String> getMap(){
		return objectValues;
	}
}
