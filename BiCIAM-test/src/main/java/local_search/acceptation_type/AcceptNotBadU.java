/**
 * @(#) AcceptNoBadU.java
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class AcceptNotBadU extends AcceptableCandidate{

	@Override
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		if (problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			Double result = stateCurrent.getEvaluation().get(0) - stateCandidate.getEvaluation().get(0);
			if (result < Strategy.getStrategy().getThreshold())
				accept = true;
			else
				accept = false;
		} else {
			Double result_min = stateCurrent.getEvaluation().get(0) - stateCandidate.getEvaluation().get(0);
			if (result_min > Strategy.getStrategy().getThreshold())
				accept = true;
			else
				accept = false;
		}
		return accept;
	}
}
