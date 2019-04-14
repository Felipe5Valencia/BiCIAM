package evolutionary_algorithms.complement;


import problem.definition.State;

public abstract class Crossover {
	
	public abstract State crossover(State father1, State father2, double PC);
}
