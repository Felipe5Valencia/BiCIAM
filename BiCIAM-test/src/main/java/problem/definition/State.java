package problem.definition;



import java.util.ArrayList;

import metaheuristics.generators.GeneratorType;

public class State {
	
	protected GeneratorType typeGenerator;
	protected ArrayList<Double> evaluation;
	protected int number;
	protected ArrayList<Object> code;
	
	public State(State ps) {
		typeGenerator = ps.getTypeGenerator();
		evaluation = ps.getEvaluation();
		number = ps.getNumber();
		code = new ArrayList<Object>(ps.getCode());
	}
	
	public State(ArrayList<Object> code) {
		super();
		this.code = code;
	}
	
	public State() {
		code=new ArrayList<Object>();
	}	
	
	public ArrayList<Object> getCode() {
		return code;
	}

	public void setCode(ArrayList<Object> listCode) {
		this.code = listCode;
	}

	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	
	public ArrayList<Double> getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(ArrayList<Double> evaluation) {
		this.evaluation = evaluation;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public State clone(){
		return this;
	}
	
	public Object getCopy(){
		return new State(this.getCode());
	}
	
	public boolean Comparator(State state){

		boolean result=false;
		if(state.getCode().equals(getCode())){
			result=true;
		}
		return result;
	}
	public double Distance(State state){
		double distancia = 0;
		for (int i = 0; i < state.getCode().size(); i++) {
			if (!(state.getCode().get(i).equals(this.getCode().get(i)))) {
				distancia++;
			}
		}
	return distancia;
	}
}
