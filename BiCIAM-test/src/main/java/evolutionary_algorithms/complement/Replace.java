package evolutionary_algorithms.complement;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;


public abstract class Replace {

	public abstract List<State> replace(State stateCandidate, List<State>listState) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
