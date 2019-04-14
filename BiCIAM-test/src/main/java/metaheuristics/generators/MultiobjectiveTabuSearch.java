package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;
import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;




public class MultiobjectiveTabuSearch extends Generator {

	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceTS;
    private IFFactoryAcceptCandidate ifacceptCandidate;
    private GeneratorType typeGenerator;
    private List<State> listStateReference = new ArrayList<State>();
    private float weight;
	private List<Float> listTrace = new ArrayList<Float>();

    public State getStateReferenceTS() {
		return stateReferenceTS;
	}

	public void setStateReferenceTS(State stateReferenceTS) {
		this.stateReferenceTS = stateReferenceTS;
	}

	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	public MultiobjectiveTabuSearch() {
    	super();
		this.typeAcceptation = AcceptType.AcceptNotDominatedTabu;
		this.strategy = StrategyType.TABU;
		@SuppressWarnings("unused")
		Problem problem = Strategy.getStrategy().getProblem();
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.MultiobjectiveTabuSearch;
		this.weight = 50;
		listTrace.add(weight);
	}

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = new ArrayList<State>();
		Problem problem = Strategy.getStrategy().getProblem();
		//List<State> list = new ArrayList<State>();
		//Devuelve la lista de soluciones no dominadas de todos los vecinos posibles de stateReferenceTS que no se encuentran en la lista Tabu
		neighborhood = problem.getOperator().generatedNewState(stateReferenceTS, operatornumber);
		//Se escoge uno aleatoriamente como vecino con RandomCandidate
		State statecandidate = candidatevalue.stateCandidate(stateReferenceTS, typeCandidate, strategy, operatornumber, neighborhood);
	    return statecandidate;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean acept = candidate.acceptCandidate(stateReferenceTS, stateCandidate);
		if(acept.equals(true))
		  stateReferenceTS = stateCandidate;

		if (strategy.equals(StrategyType.TABU) && acept.equals(true)) {
			if (TabuSolutions.listTabu.size() < TabuSolutions.maxelements) {
				Boolean find = false;
				int count = 0;
				while ((TabuSolutions.listTabu.size() > count) && (find.equals(false))) {
					if (TabuSolutions.listTabu.get(count).equals(stateCandidate)) {
						find = true;
					}
					count++;
				}
				if (find.equals(false)) {
					TabuSolutions.listTabu.add(stateCandidate);
				}
			} else {
				TabuSolutions.listTabu.remove(0);
				Boolean find = false;
				int count = 0;
				while (TabuSolutions.listTabu.size() > count && find.equals(false)) {
					if (TabuSolutions.listTabu.get(count).equals(stateCandidate)) {
						find = true;
					}
					count++;
				}
				if (find.equals(false)) {
					TabuSolutions.listTabu.add(stateCandidate);
				}
			}
	}
		getReferenceList();
  }
	
	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceTS);
		return listStateReference;
	}

	@Override
	public State getReference() {
		return stateReferenceTS;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceTS = stateInitialRef;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceTS = stateRef;
	}
	
	@Override
	public List<State> getSonList() {
		return null;
	}

	public void setTypeCandidate(CandidateType typeCandidate){
		this.typeCandidate = typeCandidate;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		return false;
	}


	public float getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public int[] getListCountBetterGender() {
		return null;
	}

	@Override
	public int[] getListCountGender() {
		return null;
	}

	@Override
	public float[] getTrace() {
		return null;
	}

}


