package evolutionary_algorithms.complement;

import metaheurictics.strategy.Strategy;

import problem.definition.State;

public class UniformCrossover extends Crossover {
	
	
	public int[] mascara(int length){
		int[] mascara = new int[length];
		for (int i = 0; i < mascara.length; i++) {
			int value = (int)(Math.random() * (int)(2));
			mascara[0] = value;
		}
		return mascara;
	}	
    
	@Override
	public State crossover(State father1, State father2, double PC) {
		Object value = new Object();
		State state = (State) father1.getCopy();
		int[] mascara = mascara(father1.getCode().size());
   		for (int k = 0; k < mascara.length; k++) {
   			if(mascara[k] == 1){
   				value = father1.getCode().get(k);
   				state.getCode().set(k, value);
   			}
   			else{
   				if(mascara[k] == 0){
   					value = father2.getCode().get(k);  
   					state.getCode().set(k, value);
   				}
   			}
		}
		return state;
	}
}
