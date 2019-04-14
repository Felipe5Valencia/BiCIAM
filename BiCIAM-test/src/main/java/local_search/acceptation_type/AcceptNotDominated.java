package local_search.acceptation_type;


import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

public class AcceptNotDominated extends AcceptableCandidate {
	
	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = false;
		Dominance dominace = new Dominance();
		
		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateCurrent.clone());
		}
		if(dominace.dominance(stateCurrent, stateCandidate)== false)
		{
			if(dominace.ListDominance(stateCandidate, Strategy.getStrategy().listRefPoblacFinal) == true){
				accept = true;
			}
			else{
				accept = false;
			}
		}
		return accept;
	}

}
