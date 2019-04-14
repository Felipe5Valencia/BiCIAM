/**
 * @(#) SmallerCandidate.java
 */

package local_search.candidate_type;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

public class SmallerCandidate extends SearchCandidate {

	@Override
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateSmaller = null;
		if(listNeighborhood.size() > 1){
			double counter = 0;
			double currentCount = listNeighborhood.get(0).getEvaluation().get(0);
			for (int i = 1; i < listNeighborhood.size(); i++) {
				counter = listNeighborhood.get(i).getEvaluation().get(0);
				if (counter < currentCount) {
					currentCount = counter;
					stateSmaller = listNeighborhood.get(i);
				}
				counter = 0;
			}
		}
		else stateSmaller = listNeighborhood.get(0);
		return stateSmaller;
	}
}