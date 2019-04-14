package problem.extension;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;

import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class MultiObjetivoPuro extends SolutionMethod {

	@Override
	public void evaluationState(State state) {
		double tempEval = -1;
		ArrayList<Double> evaluation = new ArrayList<Double>(Strategy.getStrategy().getProblem().getFunction().size());
		for (int i = 0; i < Strategy.getStrategy().getProblem().getFunction().size(); i++)
		{
			ObjetiveFunction objfunction = (ObjetiveFunction)Strategy.getStrategy().getProblem().getFunction().get(i);
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				if(objfunction.getTypeProblem().equals(ProblemType.Maximizar))
				{
					tempEval = objfunction.Evaluation(state);
				}
				else{
					tempEval = 1-objfunction.Evaluation(state);
				}
			}
			else{
				if(objfunction.getTypeProblem().equals(ProblemType.Maximizar))
				{
					tempEval = 1-objfunction.Evaluation(state);
				}
				else{
					tempEval = objfunction.Evaluation(state);
				}
			}
			evaluation.add(tempEval);
		}
		//evaluation.add( (double) -1);
		state.setEvaluation(evaluation);
	}

}
