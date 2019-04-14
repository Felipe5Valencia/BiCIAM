package evolutionary_algorithms.complement;


import metaheurictics.strategy.Strategy;
import problem.definition.State;
public class TowPointsMutation extends Mutation {

	/*@Override
	public ProblemState mutation(SortedMap<Object, Object> newind, double PM) {
		
		int pos1 = (int) (Math.random() * (int)Problem.countvariable);
		int pos2 = (int) (Math.random() * (int)Problem.countvariable);
		
		Object value1 = (Integer)(newind.get("x" + pos1));
		Object value2 = (Integer)(newind.get("x" + pos2));
		newind.put("x" + pos1, value2);
		newind.put("x" + pos2, value1);
	
		return newind;
	}*/

	@Override
	public State mutation(State newind, double PM) {
		Object key1 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
		Object key2 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
		Object value1 = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key1);
		Object value2 = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key2);
		newind.getCode().set((Integer) key1, (Integer)value2);
		newind.getCode().set((Integer) key2, (Integer)value1);
		return newind;
	}
}
  