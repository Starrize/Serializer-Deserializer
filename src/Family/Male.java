package Family;

public class Male {
	public Integer  age;
	public String name;
	public Male(int age, String name){
		this.name = name;
		this.age = age;
	}
	public Male(){}
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
