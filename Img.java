
public class Img {

	private String name;
	private double score;
	
	public Img(String name, double score){
		this.name =name;
		this.score = score;
	}
	
	public void setName(String name){
		name = this.name;
	}
	
	public void setName(double score){
		score = this.score;
	}
	
	public String getName(){
		return name;
	}
	
	public double getScore(){
		return score;
	}
}

