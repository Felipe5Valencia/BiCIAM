package problem.definition;

import problem.definition.Problem.ProblemType;

public abstract class ObjetiveFunction {
	
	private ProblemType typeProblem;
	private float weight;
	
	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public ProblemType getTypeProblem() {
		return typeProblem;
	}

	public void setTypeProblem(ProblemType typeProblem) {
		this.typeProblem = typeProblem;
	}

	public abstract Double Evaluation(State state);
}
