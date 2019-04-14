package evolutionary_algorithms.complement;

import problem.definition.State;

public abstract class Mutation {
	
	public abstract State mutation (State state, double PM);

}
