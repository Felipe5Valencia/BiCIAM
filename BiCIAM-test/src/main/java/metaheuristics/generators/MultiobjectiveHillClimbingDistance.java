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

public class MultiobjectiveHillClimbingDistance extends Generator{

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
	//Lista que contiene las distancias de cada solución del frente de Pareto estimado
	public static List<Double> distanceSolution = new ArrayList<Double>();


	public MultiobjectiveHillClimbingDistance() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotDominated;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.NotDominatedCandidate;
		this.candidatevalue = new CandidateValue();
		this.Generatortype = GeneratorType.MultiobjectiveHillClimbingDistance;
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
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, 
	InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//Agregando la primera solución a la lista de soluciones no dominadas
		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateReferenceHC.clone());
			distanceSolution.add(new Double(0));
		}
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		State lastState = Strategy.getStrategy().listRefPoblacFinal.get(Strategy.getStrategy().listRefPoblacFinal.size()-1);
		List<State> neighborhood = new ArrayList<State>();
		neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, sizeNeighbors);
		int i= 0;
//		Boolean restart= true;

//		while (restart==true) {
			Boolean accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
			if(accept.equals(true)){
				stateReferenceHC = stateCandidate.clone();
				visitedState = new ArrayList<State>();
				lastState=stateReferenceHC.clone();
//				restart=false;
			}

			else{

				boolean stop = false;
				while (i < neighborhood.size()&& stop==false) {
					if (Contain(neighborhood.get(i))==false) {
						stateReferenceHC = SolutionMoreDistance(Strategy.getStrategy().listRefPoblacFinal, distanceSolution);
						visitedState.add(stateReferenceHC);
						stop=true;
						lastState=stateReferenceHC.clone();
//						restart=false;
					}
					i++;
				}
				int coutrestart=0;
				while (stop == false && coutrestart < sizeNeighbors && accept==false) {
					stateCandidate = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
					if (Contain(stateCandidate)==false) {
						Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
						visitedState.add(stateCandidate);
						stop=true;
						coutrestart++;
						accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
					}
				}
				if(accept.equals(true)){
					stateReferenceHC = stateCandidate.clone();
					visitedState = new ArrayList<State>();
					lastState = stateReferenceHC.clone();
					//tomar xc q pertenesca a la vecindad de xa
				}
			}

		getReferenceList();
	}

	private State SolutionMoreDistance(List<State> state, List<Double> distanceSolution) {
		Double max = (double) -1;
		int pos = -1;
		Double[] distance = distanceSolution.toArray(new Double[distanceSolution.size()]);
		State[] solutions = state.toArray(new State[state.size()]);
		for (int i = 0; i < distance.length; i++) {
			Double dist = distance[i];
			if(dist > max){
				max = dist;
				pos = i;
			}
		}
		if(pos != -1)
			return solutions[pos];
		else
			return null;
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

	public static List<Double> DistanceCalculateAdd(List<State> solution) {
		State[] solutions = solution.toArray(new State[solution.size()]);
		Double distance = 0.0;
		List<Double>listDist=new ArrayList<Double>();
		State lastSolution = solution.get(solution.size()-1);
		//Actualizando las distancias de todos los elmentos excepto el nuevo insertando
		for (int k = 0; k < solutions.length-1; k++) {
			State solA = solutions[k];
			distance = solA.Distance(lastSolution);
			listDist.add(distanceSolution.get(k)+distance);
//			distanceSolution.set(k, distanceSolution.get(k) + distance);
		}
		distance = 0.0;
		//Calculando la distancia del último elemento (elemento insertado) respecto al resto de los elementos
		if (solutions.length==1) {
			return distanceSolution;
		
		}else {
		
			for (int l = 0; l < solutions.length-1; l++) {
				State solB = solutions[l];
				distance += lastSolution.Distance(solB);
			}
			listDist.add(distance);
//			distanceSolution.add(distance);
			distanceSolution=listDist;
			
			return distanceSolution;
		}

	}


	private boolean Contain(State state){
		boolean found = false;
		for (Iterator<State> iter = visitedState.iterator(); iter.hasNext();) {
			State element = (State) iter.next();
			if(element.Comparator(state)){
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
