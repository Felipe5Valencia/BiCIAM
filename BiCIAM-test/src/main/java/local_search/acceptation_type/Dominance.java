package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiobjectiveHillClimbingDistance;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class Dominance {
	
	public boolean ListDominance(State solutionX, List<State> list){
		boolean domain = false;
		for (int i = 0; i < list.size() && domain == false; i++) {
			if(dominance(solutionX, list.get(i)) == true){
				list.remove(i);
				if (i!=0) {
					i--;	
				}
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MultiobjectiveHillClimbingDistance)&&list.size()!=0) {
					MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(list);
				}
			}
			if (list.size()>0) {
				if(dominance(list.get(i), solutionX) == true){
					domain = true;
				}
			}

		}
		if(domain == false){
			boolean found = false;
			for (int k = 0; k < list.size() && found == false; k++) {
				State element = list.get(k);
				found = solutionX.Comparator(element);
			}
			if(found == false){
				list.add(solutionX.clone());
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MultiobjectiveHillClimbingDistance)) {
					MultiobjectiveHillClimbingDistance.DistanceCalculateAdd(list);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean dominance(State solutionX,  State solutionY)	{
		boolean dominance = false;
		int countBest = 0;
		int countEquals = 0;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() > solutionY.getEvaluation().get(i).floatValue()){
					countBest++;
				}
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue()){
					countEquals++;
				}	
			}
		}
		else{
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() < solutionY.getEvaluation().get(i).floatValue()){
					countBest++;
				}
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue()){
					countEquals++;
				}	
			}
		}
		if((countBest >= 1) && (countEquals + countBest == solutionX.getEvaluation().size())) {
			dominance = true;
		}
		return dominance;
	} 
}
