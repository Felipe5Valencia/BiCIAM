package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


public abstract class Distribution {
	public abstract List<Probability> distribution(List<State> fathers);

}
