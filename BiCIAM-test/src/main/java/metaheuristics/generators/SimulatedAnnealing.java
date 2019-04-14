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

public class SimulatedAnnealing extends Generator {

	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceSA;
    private IFFactoryAcceptCandidate ifacceptCandidate;
    public static Double alpha;
    public static Double tinitial;
    public static Double tfinal;
    public static int countIterationsT;
    private int countRept;
    private GeneratorType typeGenerator;
    private List<State> listStateReference = new ArrayList<State>();
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

	public SimulatedAnnealing(){

    	super();
    	/*SimulatedAnnealing.alpha = 0.93;
    	SimulatedAnnealing.tinitial = 250.0;
    	SimulatedAnnealing.tfinal = 41.66;
    	SimulatedAnnealing.countIterationsT = 50;*/

    	this.typeAcceptation = AcceptType.AcceptNotBadT;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.SimulatedAnnealing;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
    }

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//<State>list=new ArrayList<State>();
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceSA, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceSA, typeCandidate, strategy, operatornumber, neighborhood);
	   // list.add(statecandidate);
	    return statecandidate;
	}

	@Override
	public State getReference() {
		return stateReferenceSA;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceSA = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceSA = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		countRept = countIterationsT;
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceSA, stateCandidate);
		if(accept.equals(true))
		  stateReferenceSA = stateCandidate;
		if(countIterationsCurrent.equals(countIterationsT)){
			tinitial = tinitial * alpha;
			countIterationsT = countIterationsT + countRept;
		}
//		getReferenceList();
	}

	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceSA);
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
