package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

public class NotDominatedCandidate extends SearchCandidate {

	@Override
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State state = new State();
		State stateA = listNeighborhood.get(0);
		boolean stop = false;
		if(listNeighborhood.size() == 1){
			stop = true;
			state = stateA;
		}
		else {
			Strategy.getStrategy().getProblem().Evaluate(stateA);
			State stateB = new State();
			Dominance dominance = new Dominance();
			for (int i = 1; i < listNeighborhood.size(); i++) {
				while(stop == false){
					stateB = listNeighborhood.get(i);
					Strategy.getStrategy().getProblem().Evaluate(stateB);
					if(dominance.dominance(stateB, stateA) == true){
						stateA = stateB;
					}else{
						stop = true;
						state = stateA;
					}
				}
			}
		}
		return state;
	}

}
