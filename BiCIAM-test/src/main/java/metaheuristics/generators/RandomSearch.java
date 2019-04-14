package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

public class RandomSearch extends Generator {

	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceRS;
    private IFFactoryAcceptCandidate ifacceptCandidate;
    private GeneratorType typeGenerator;

    private float weight;
    //para acceder desde los algoritmos basados en poblaciones de puntos
	public static List<State> listStateReference = new ArrayList<State>();
	
	//problemas dinamicos
    public static int countGender = 0;
    public static int countBetterGender = 0;
    private int[] listCountBetterGender = new int[10];
    private int[] listCountGender = new int[10];
    private float[] listTrace = new float[1200000];
	
	public RandomSearch() {
		super();
		this.typeAcceptation = AcceptType.AcceptBest;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.RandomSearch;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
		listStateReference = new ArrayList<State>();
	}
	
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//ArrayList<State>list =new ArrayList<State>();
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generateRandomState(operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceRS, typeCandidate, strategy, operatornumber, neighborhood);
	    if(GeneticAlgorithm.countRef != 0 || EvolutionStrategies.countRef != 0 || DistributionEstimationAlgorithm.countRef != 0 || ParticleSwarmOptimization.countRef != 0)
	    	listStateReference.add(statecandidate);
	    return statecandidate;
	}

	@Override
	public State getReference() {
		return stateReferenceRS;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
	  this.stateReferenceRS = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException,	IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceRS, stateCandidate);
		if(accept.equals(true))
		  stateReferenceRS = stateCandidate;
//		getReferenceList();
	}


	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}
	
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceRS);
		return listStateReference;
	}

	@Override
	public List<State> getSonList() {
		return null;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		return false;
	}


	@Override
	public float getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public int[] getListCountBetterGender() {
		return this.listCountBetterGender;
	}

	@Override
	public int[] getListCountGender() {
		return this.listCountGender;
	}

	@Override
	public float[] getTrace() {
		return this.listTrace;
	}

}
