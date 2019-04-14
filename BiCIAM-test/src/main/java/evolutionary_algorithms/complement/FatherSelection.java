package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


public abstract class FatherSelection {
	
	public abstract List<State> selection(List<State> listState, int truncation);

}
