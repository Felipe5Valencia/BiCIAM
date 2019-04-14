/**
 * @(#) MajorCandidate.java
 */

package local_search.candidate_type;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;




public class GreaterCandidate extends SearchCandidate {
	
	@Override
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateGreater = null;
		if(listNeighborhood.size() > 1){
			double counter = 0;
			double currentCount = listNeighborhood.get(0).getEvaluation().get(0);;
			for (int i = 1; i < listNeighborhood.size(); i++) {
				counter = listNeighborhood.get(i).getEvaluation().get(0);
				if (counter > currentCount) {
					currentCount = counter;
					stateGreater = listNeighborhood.get(i);
				}
				counter = 0;
			}
			if(stateGreater == null){
				int pos = (int)(Math.random() * (double)(listNeighborhood.size() - 1));
				stateGreater = listNeighborhood.get(pos);
			}
		}
		else stateGreater = listNeighborhood.get(0);
		return stateGreater;
	}
}