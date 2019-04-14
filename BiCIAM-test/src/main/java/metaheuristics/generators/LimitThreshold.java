package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;



import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;


public class LimitThreshold extends Generator{
	
	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceLT;
    private IFFactoryAcceptCandidate ifacceptCandidate;
	private GeneratorType typeGenerator;
	//private List<State> listStateReference = new ArrayList<State>(); // lista de estados referencias por los que va pasando el algoritmo
	private float weight;
	
	//problemas dinamicos
	public static int countGender = 0;
	public static int countBetterGender = 0;
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	public LimitThreshold() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotBadU;
		this.strategy = StrategyType.NORMAL;


		Problem problem = Strategy.getStrategy().getProblem();

		if(problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			this.typeCandidate = CandidateType.GreaterCandidate;
		}
		else{
			this.typeCandidate = CandidateType.SmallerCandidate;
		}

		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.LimitThreshold;
		this.weight = (float) 50.0;
		listTrace[0] = weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;

	}
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceLT, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceLT, typeCandidate, strategy, operatornumber, neighborhood);
	    return statecandidate;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceLT , stateCandidate);
		if(accept.equals(true)){
			//listStateReference.add(stateCandidate);
			stateReferenceLT = stateCandidate;
		}
		//else listStateReference.add(stateReferenceHC);
	}
	

	@Override
	public State getReference() {
		return stateReferenceLT;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceLT = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceLT = stateInitialRef;
	}


	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		return null;
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
