package Family;

public class Female {
	private Integer age;
	public String name;
	
	public Female(int age, String name){
		this.name = name;
		this.age = age;
	}
	public Female(){}
	public int getAge(){
		return age;
	}
	public String getName(){
		return name;
	}	
	public String toString(){
		return name + ": " + age + " de ani.\n";
	}
}
