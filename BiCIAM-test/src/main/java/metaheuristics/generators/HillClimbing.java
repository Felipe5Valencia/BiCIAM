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
import problem.definition.Problem.ProblemType;



import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;


public class HillClimbing extends Generator{

    protected CandidateValue candidatevalue;
	protected AcceptType typeAcceptation;
	protected StrategyType strategy;
	protected CandidateType typeCandidate;
	protected State stateReferenceHC;
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	protected GeneratorType Generatortype;
	protected List<State> listStateReference = new ArrayList<State>(); 
	protected float weight;
	

	public static int countGender = 0;
	public static int countBetterGender = 0;
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
	public HillClimbing() {
		super();
		this.typeAcceptation = AcceptType.AcceptBest;
		this.strategy = StrategyType.NORMAL;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			this.typeCandidate = CandidateType.GreaterCandidate;
		}
		else{
			this.typeCandidate = CandidateType.SmallerCandidate;
		}
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.HillClimbing;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
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
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceHC, stateCandidate);
		if(accept.equals(true))
		  stateReferenceHC = stateCandidate;
}
	
	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceHC);
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

	public void setTypeCandidate(CandidateType typeCandidate){
		this.typeCandidate = typeCandidate;
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
