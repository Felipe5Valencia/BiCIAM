/**
 * @(#) AcceptNoBad.java
 */

package local_search.acceptation_type;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class AcceptNotBad extends AcceptableCandidate{

	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		for (int i = 0; i < problem.getFunction().size(); i++) {
		if (problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			if (stateCandidate.getEvaluation().get(0) >= stateCurrent.getEvaluation().get(0)) {
				accept = true;
			} else {
				accept = false;
			}
		} else {
			if (stateCandidate.getEvaluation().get(0) <= stateCurrent.getEvaluation().get(0)) {
				accept = true;
			} else {
				accept = false;
			}
		}}
		return accept;
	}
}
