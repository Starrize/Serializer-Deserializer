package Family;

public class Family extends X{
	public Male mother;
	public Male father;

	public Family(Female mother, Male father){
		super(1);
		this.father = father;
		this.mother = father;
	}
	public Family(){super();}
	public String toString(){
		return "Base class: \n\t" + mother.toString() + "\t" + father.toString() + super.toString();
	}
}
