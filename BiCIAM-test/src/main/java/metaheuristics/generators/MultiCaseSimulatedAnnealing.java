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
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;


public class MultiCaseSimulatedAnnealing extends Generator {

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
	private List<Float> listTrace = new ArrayList<Float>();

    public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	public MultiCaseSimulatedAnnealing(){
    	super();
    	this.typeAcceptation = AcceptType.AcceptMulticase;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.MultiCaseSimulatedAnnealing;
		this.weight = 50;
		listTrace.add(weight);
    }

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = new ArrayList<State>();
		Problem problem = Strategy.getStrategy().getProblem();
		neighborhood = problem.getOperator().generatedNewState(stateReferenceSA, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceSA, typeCandidate, strategy, operatornumber, neighborhood);
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
		  stateReferenceSA = stateCandidate.clone();
		if(countIterationsCurrent.equals(countIterationsT)){
			tinitial = tinitial * alpha;
			//Variante Fast MOSA
			//tinitial = tinitial/(1 + countIterationsCurrent);
			
			//Variante Two-Stage Annealing MC-MOSA
			/*if(countIterationsCurrent/2 < countIterationsT){
				tinitial = tinitial * alpha;
			}
			else{
				tinitial = tinitial/(1 + countIterationsCurrent);
			}*/
			System.out.println("La T:" + tinitial);
			countIterationsT = countIterationsT + countRept;
			System.out.println("La Cant es: " + countIterationsT);
		}
		getReferenceList();
	}

	@Override
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceSA.clone());
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
		return 0;
	}

	@Override
	public void setWeight(float weight) {
		
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
