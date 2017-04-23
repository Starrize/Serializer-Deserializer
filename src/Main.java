import java.io.IOException;

import Family.*;
public class Main {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException, SecurityException, IOException{
		Male father = new Male(30,"Gigi");
		Female mother = new Female(29,"Maria");
		Family f = new Family(mother,father);
		
		new Serializer("testFamily.txt",f);
		
		
		Deserializer rez = new Deserializer("testFamily.txt");
		Family rezultat = (Family)rez.resultingObject;
		System.out.println(rezultat);
		
	}
}
