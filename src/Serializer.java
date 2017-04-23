import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.lang.reflect.Field;;

public class Serializer {
	private PrintWriter printWriter;
	private HashSet<Integer> objectCodes = new HashSet<>();
	public Serializer(String outputFile, Object toSerialize) throws FileNotFoundException, IllegalArgumentException, IllegalAccessException{	
		printWriter = new PrintWriter(outputFile);
		serialize(toSerialize);
		printWriter.close();
	}
	
	private void serialize(Object toSerialize) throws IllegalArgumentException, IllegalAccessException{
		if(objectCodes.contains(toSerialize.hashCode()))
			return;
		objectCodes.add(toSerialize.hashCode());
		
		String objectInfo = " " + toSerialize.hashCode() + " " +
				toSerialize.getClass().getName() + "{\n";
		if(isPrimitive(toSerialize)){
			objectInfo += "<" + "value" + ">:" + 
				String.valueOf(toSerialize) + " ";
		}
		else{
			Class<?> classObject = toSerialize.getClass();
			
			for(;classObject != Object.class;classObject = classObject.getSuperclass()){

				Field[] classFields = classObject.getDeclaredFields();
				for(Field currentField : classFields){
					currentField.setAccessible(true);
					
					String name = currentField.getName();
					Object objectValue = currentField.get(toSerialize);
					String value = String.valueOf(objectValue.hashCode());
					
					Class<?> fieldType = currentField.getType();
					if(!fieldType.isPrimitive()){
						value = "#" + value;
						serialize(currentField.get(toSerialize));
					}
					objectInfo += "<" + name + ">:" + value + "\n";
					currentField.setAccessible(false);
				}
			}
		}
		objectInfo = objectInfo.substring(0,objectInfo.length()-1) + "\n}\n";
		printWriter.println(objectInfo);
		System.out.println(objectInfo);
	}

	private boolean isPrimitive(Object toCheck){
		if(toCheck.getClass() == Boolean.class)return true;
		if(toCheck.getClass() == Byte.class)return true;
		if(toCheck.getClass() == Short.class)return true;
		if(toCheck.getClass() == Character.class)return true;
		if(toCheck.getClass() == Integer.class)return true;
		if(toCheck.getClass() == Long.class)return true;
		if(toCheck.getClass() == Float.class)return true;
		if(toCheck.getClass() == Double.class)return true;
		if(toCheck.getClass() == String.class)return true;
		return false;
	}
}
