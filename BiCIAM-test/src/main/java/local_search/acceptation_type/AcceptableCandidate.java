package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import problem.definition.State;

public abstract class AcceptableCandidate {
  
	public abstract Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
