package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

import problem.definition.State;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;




public class MultiobjectiveHillClimbingRestart extends Generator{

	protected CandidateValue candidatevalue;
	protected AcceptType typeAcceptation;
	protected StrategyType strategy;
	protected CandidateType typeCandidate;
	protected State stateReferenceHC;
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	protected GeneratorType Generatortype;
	protected List<State> listStateReference = new ArrayList<State>(); 
	protected float weight;
	protected List<Float> listTrace = new ArrayList<Float>();
	private List<State> visitedState = new ArrayList<State>();
	public static int sizeNeighbors;


	public MultiobjectiveHillClimbingRestart() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotDominated;
		this.strategy = StrategyType.NORMAL;
		//Problem problem = Strategy.getStrategy().getProblem();
		this.typeCandidate = CandidateType.NotDominatedCandidate;
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.MultiobjectiveHillClimbingRestart;
		this.weight = 50;
		listTrace.add(weight);
	}

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
		State statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
		return statecandidate;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//Agregando la primera solución a la lista de soluciones no dominadas

		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateReferenceHC.clone());
		}

		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		State lastState = Strategy.getStrategy().listRefPoblacFinal.get(Strategy.getStrategy().listRefPoblacFinal.size()-1);
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, sizeNeighbors);
		int i= 0;

		Boolean accept = candidate.acceptCandidate(lastState, stateCandidate.clone());

		if(accept.equals(true)){
			stateReferenceHC = stateCandidate.clone();
			visitedState = new ArrayList<State>();
			lastState = stateCandidate.clone();
			//tomar xc q pertenesca a la vecindad de xa
		}
		else{
			boolean stop = false;
			while (i < neighborhood.size()&& stop==false) {
				if (Contain(neighborhood.get(i))==false) {
					stateCandidate = neighborhood.get(i);
					Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
					visitedState.add(stateCandidate);
					accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
					stop=true;
				}
				i++;
			}
			while (stop == false) {
				stateCandidate = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
				if (Contain(stateCandidate)==false) {
					Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
					stop=true;
					accept = candidate.acceptCandidate(lastState, stateCandidate.clone());

				}
			}
			if(accept.equals(true)){
				stateReferenceHC = stateCandidate.clone();
				visitedState = new ArrayList<State>();
				lastState = stateCandidate.clone();
				//tomar xc q pertenesca a la vecindad de xa
			}
		}


		getReferenceList();
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceHC.clone());
		return listStateReference;
	}

	@Override
	public State getReference() {
		return stateReferenceHC;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceHC = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceHC = stateInitialRef;
	}

	public GeneratorType getGeneratorType() {
		return Generatortype;
	}

	public void setGeneratorType(GeneratorType Generatortype) {
		this.Generatortype = Generatortype;
	}

	@Override
	public GeneratorType getType() {
		return this.Generatortype;
	}

	@Override
	public List<State> getSonList() {
		return null;
	}

	private boolean Contain(State state){
		boolean found = false;
		for (Iterator<State> iter = visitedState.iterator(); iter.hasNext();) {
			State element = (State) iter.next();
			if(element.Comparator(state)==true){
				found = true;
			}
		}
		return found;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		return false;
	}


	@Override
	public float getWeight() {
		return 0;
	}

	@Override
	public void setWeight(float weight) {

	}

	@Override
	public float[] getTrace() {
		return null;
	}

	@Override
	public int[] getListCountBetterGender() {
		return null;
	}

	@Override
	public int[] getListCountGender() {
		return null;
	}
}
