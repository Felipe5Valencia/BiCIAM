/**
 * @(#) SearchCandidate.java
 */

package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;


public abstract class SearchCandidate {
	
	public abstract State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
