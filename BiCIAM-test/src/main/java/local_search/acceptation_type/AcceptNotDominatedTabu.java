package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;

public class AcceptNotDominatedTabu extends AcceptableCandidate {

	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		List<State> list = Strategy.getStrategy().listRefPoblacFinal;
		
		Dominance dominance = new Dominance();
		if(list.size() == 0){
			list.add(stateCurrent.clone());
		}
		dominance.ListDominance(stateCandidate, list);
		return true;
	}
}
