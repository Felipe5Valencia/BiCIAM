package evolutionary_algorithms.complement;

import java.util.ArrayList;

import config.tspDynamic.TSPState;


import metaheurictics.strategy.Strategy;

import problem.definition.State;

public class AIOMutation extends Mutation {
	
	
	public static ArrayList<Object> path = new ArrayList<Object>();

	@Override
	public State mutation(State state, double PM) {
		int key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey(); 
		int key1 = 0;
		boolean found = false;
		while (found == false){
			key1 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
			if(key1 != key)
				found = true;
		}
		sortedPathValue(state);
		int p1 = 0;
		int p2 = 0;
		if(key > key1){
			p2 = key;
			p1 = key1;
		}
		else{
			p1 = key;
			p2 = key1;
		}
		int length = (p2 - p1) / 2;
		for (int i = 1; i <= length + 1; i++) {
			int tempC = ((TSPState) state.getCode().get(p1 + i - 1)).getIdCity();
			((TSPState)state.getCode().get(p1 + i - 1)).setIdCity(((TSPState)state.getCode().get(p2 - i + 1)).getIdCity());
			((TSPState)state.getCode().get(p2 - i + 1)).setIdCity(tempC);
		}
		path.clear();
		return state;
	}
	
	public void sortedPathValue(State state) {
		for(int k = 0; k < state.getCode().size(); k++){
			path.add( state.getCode().get(k));
		}
		for(int i = 1; i < path.size(); i++){
			for(int j = 0; j < i; j++){
				Integer data1 = ((TSPState)state.getCode().get(i)).getValue();
				Integer data2 = ((TSPState)state.getCode().get(j)).getValue();
				if(data1 < data2){
					state.getCode().add(j, state.getCode().remove(i));
					path.add(j, path.remove(i));
				}
			}
		}
	
	}

	public static void fillPath() {
		for(int k = 0; k < Strategy.getStrategy().getProblem().getCodification().getVariableCount(); k++){
			path.add(k);
		}
	}

}
