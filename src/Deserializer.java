import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Deserializer {
	private HashMap<Integer, Object> createdObjects= new HashMap<Integer, Object>();
	private HashMap<Integer, Tokens> tokens = new HashMap<Integer, Tokens>();
	private BufferedReader reader;
	public Object resultingObject;
	
	public Deserializer(String inputFile) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		reader = new BufferedReader(new FileReader(inputFile));
		Integer resultingInteger = GenerateTokens();		
		resultingObject = deSerialize(resultingInteger);
	}
	
	private Integer GenerateTokens() throws IOException{
		String line = "";
		String brokenLine[] = null;
		Integer resultingHashCode = null;
		
		while((line = reader.readLine()) != null){
			/*if(line.startsWith(" ")||line.startsWith("\n")||line.startsWith("\t"))
				break;*/
			if(line.startsWith("#")){
				brokenLine = line.split(" ");
				Integer objectHash = Integer.parseInt(brokenLine[0].substring(1,brokenLine[0].length()));
				Tokens value = new Tokens(brokenLine[1].substring(0, brokenLine[1].length()-1));
				tokens.put(objectHash, value);
				resultingHashCode = objectHash;
				//System.out.println(resultingHashCode);
				
				while((line = reader.readLine())!=null){
					if(line.startsWith("}"))
						break;
					brokenLine = line.split(":");
					String key = brokenLine[0].substring(1, brokenLine[0].length()-1);
					String value1 = brokenLine[1];
					value.add(key,value1); 
				}
			}
		}
		reader.close();
		//System.out.println(resultingHashCode);
		return resultingHashCode;
	}
	
	private Object deSerialize(Integer objectKey) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException{
		if(createdObjects.containsKey(objectKey)==true)
			return createdObjects.get(objectKey);
		
		Tokens ObjectTokens = tokens.get(objectKey);
		Object object = generatePrimitiveObject(objectKey);
		if(object!=null){
			tokens.remove(objectKey);
			createdObjects.put(objectKey,object);
			return object;
		}
		object = Class.forName(ObjectTokens.getObjectClass()).newInstance();
		
		for(Map.Entry<String, String> entry : ObjectTokens.getMap().entrySet()){
			Class<?> ObjectClass = Class.forName(ObjectTokens.getObjectClass());
			Field field;
			try{
				field = ObjectClass.getDeclaredField(entry.getKey());
			}
			catch(NoSuchFieldException e){
				field = trySuperclass(ObjectClass,entry);
			}
			if(field == null){
				System.out.println("Inexisting field" + entry.getKey());
				System.exit(1);
			}
			
			field.setAccessible(true);
			
			Object fieldContent = null;
			
			if(entry.getValue().startsWith("#")){
				Integer ObjectCode = Integer.parseInt(entry.getValue().substring(1,entry.getValue().length()));
				fieldContent = deSerialize(ObjectCode);
				field.set(object, fieldContent);
			}
			else{
				fieldContent = entry.getValue();
				setValue(field,object,fieldContent);
			}
			field.setAccessible(false);
		}
		return object;
	}
	
	private Field trySuperclass(Class<?> baseClass, Map.Entry<String, String> entry){
		Field field;
		Class<?> superClass=null;
		if(baseClass == Object.class){
			return null;
		}
		try{
			superClass = baseClass.getSuperclass();
			field = superClass.getDeclaredField(entry.getKey());
		}
		catch(NoSuchFieldException e){
			field = trySuperclass(superClass,entry);
		}
		return field;
	}
	private Object generatePrimitiveObject(Integer key) {
		Tokens ObjectTokens = tokens.get(key);
		String value = ObjectTokens.getValueOf("value");
		if(ObjectTokens.getObjectClass().equals(Integer.class.getName()) || 
			ObjectTokens.getObjectClass().equals("int"))
			return new Integer(value);
		
		if(ObjectTokens.getObjectClass().equals(String.class.getName()))	
			return new String(value);
		
		if(ObjectTokens.getObjectClass().equals(Boolean.class.getName())||
			ObjectTokens.getObjectClass().equals("boolean"))	
			return new Boolean(value);
		
		if(ObjectTokens.getObjectClass().equals(Byte.class.getName())||
			ObjectTokens.getObjectClass().equals("byte"))		
			return new Byte(value);
		
		if(ObjectTokens.getObjectClass().equals(Short.class.getName())||
			ObjectTokens.getObjectClass().equals("short"))
			return new Short(value);
		
		if(ObjectTokens.getObjectClass().equals(Character.class.getName()) || 
			ObjectTokens.getObjectClass().equals("char"))	
			return new Character(value.charAt(0));
		
		if(ObjectTokens.getObjectClass().equals(Long.class.getName())||
			ObjectTokens.getObjectClass().equals("long"))		
			return new Long(value);
		
		if(ObjectTokens.getObjectClass().equals(Float.class.getName())||
			ObjectTokens.getObjectClass().equals("float"))		
			return new Float(value);
		
		if(ObjectTokens.getObjectClass().equals(Double.class.getName())||
			ObjectTokens.getObjectClass().equals("double"))	
			return new Double(value);
		return null;
	}

	private void setValue(Field field, Object object, Object fieldContent) throws NumberFormatException, IllegalArgumentException, IllegalAccessException{
		Class<?> fieldType = field.getType();
		if(fieldType == Integer.class || fieldType == int.class){
			field.set(object, Integer.parseInt(fieldContent.toString()));
			return;
		}
		if(fieldType == String.class){
			field.set(object, fieldContent.toString());
			return;
		}
		if(fieldType == Short.class || fieldType == short.class){
			field.set(object, Short.parseShort(fieldContent.toString()));
			return;
		}
		if(fieldType == Long.class || fieldType == long.class){
			field.set(object, Long.parseLong(fieldContent.toString()));
			return;
		}
		if(fieldType == Byte.class || fieldType == byte.class){
			field.set(object, Byte.parseByte(fieldContent.toString()));
			return;
		}
		if(fieldType == Boolean.class || fieldType == boolean.class){
			field.set(object, Boolean.parseBoolean(fieldContent.toString()));
			return;
		}
		if(fieldType == Character.class || fieldType == char.class){
			field.set(object, (char)Integer.parseInt(fieldContent.toString()));
			return;
		}
		if(fieldType == Float.class || fieldType == float.class){
			field.set(object, Float.parseFloat(fieldContent.toString()));
			return;
		}
		if(fieldType == Double.class || fieldType == double.class){
			field.set(object, Double.parseDouble(fieldContent.toString()));
			return;
		}
	}
}
