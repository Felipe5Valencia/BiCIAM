package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


public abstract class Sampling {
	public abstract List<State> sampling (List<State> fathers, int countInd);
}
