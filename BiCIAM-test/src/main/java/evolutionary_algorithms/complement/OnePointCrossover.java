package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;


public class OnePointCrossover extends Crossover {

	@Override
	public State crossover(State father1, State father2, double PC) {
				
		State newInd = (State) father1.getCopy();
		
	    List<Object> ind1 = new ArrayList<Object>();
	    List<Object> ind2 = new ArrayList<Object>();
	    
	    double number = (double) Math.random() * (double)(1);	
		if(number <= PC){
			int pos = (int) Math.random() * (int)(Strategy.getStrategy().getProblem().getCodification().getVariableCount() - 1); 
			for (int i = 0; i < father1.getCode().size(); i++) {
				if(i <= pos){
					ind1.add(father1.getCode().get(i));
					ind2.add(father2.getCode().get(i));
				}
				else{
					ind1.add(father2.getCode().get(i));
					ind2.add(father1.getCode().get(i));
				}
			}
			
			for (int j = pos; j < ind1.size(); j++) {
		       for (int k = pos; k < ind2.size(); k++) {
				  Integer value1 = (Integer) ind1.get(j);
		    	  ind1.set(j, ind2.get(k));
				  ind2.set(k,value1);
			   }				  
		    }
			int random = (int)(Math.random() * (double)(2));
			if(random == 0)
				newInd.setCode((ArrayList<Object>) ind1);
			else newInd.setCode((ArrayList<Object>) ind2); 
		}
		return newInd;			
	}
	
	
}
