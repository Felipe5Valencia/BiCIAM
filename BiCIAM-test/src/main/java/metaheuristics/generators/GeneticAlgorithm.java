package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryCrossover;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryMutation;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryCrossover;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryMutation;
import factory_method.FactoryReplace;

public class GeneticAlgorithm extends Generator {

	private State stateReferenceGA;
	private List<State> listState = new ArrayList<State>(); 
	private IFFactoryFatherSelection iffatherselection;
	private IFFactoryCrossover iffactorycrossover;
	private IFFactoryMutation iffactorymutation;
	private IFFactoryReplace iffreplace;
	
	public static MutationType mutationType;
	public static CrossoverType crossoverType;
	public static ReplaceType replaceType;
	public static SelectionType selectionType;

	private GeneratorType generatorType;
	public static double PC;
	public static double PM;
	public static int countRef = 0;
	public static int truncation;
	private float weight;

	public static int countGender = 0;
	public static int countBetterGender = 0;
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
    public GeneticAlgorithm() {
		super();
		this.listState = getListStateRef();
		this.generatorType = GeneratorType.GeneticAlgorithm;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
	}
    
	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		List<State> refList = new ArrayList<State>(this.listState); 
    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(refList, truncation);
    	int pos1 = (int)(Math.random() * fathers.size());
    	int pos2 = (int)(Math.random() * fathers.size());
    	
    	State auxState1 = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    	auxState1.setCode(new ArrayList<Object>(fathers.get(pos1).getCode()));
    	auxState1.setEvaluation(fathers.get(pos1).getEvaluation());
    	auxState1.setNumber(fathers.get(pos1).getNumber());
    	auxState1.setTypeGenerator(fathers.get(pos1).getTypeGenerator());
    	
    	State auxState2 = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    	auxState2.setCode(new ArrayList<Object>(fathers.get(pos2).getCode()));
    	auxState2.setEvaluation(fathers.get(pos2).getEvaluation());
    	auxState2.setNumber(fathers.get(pos2).getNumber());
    	auxState2.setTypeGenerator(fathers.get(pos2).getTypeGenerator());
    	
	
	    iffactorycrossover = new FactoryCrossover();
    	Crossover crossover = iffactorycrossover.createCrossover(crossoverType);
    	auxState1 = crossover.crossover(auxState1, auxState2, PC);
	    
	
    	iffactorymutation = new FactoryMutation();
    	Mutation mutation =iffactorymutation.createMutation(mutationType);
    	auxState1 = mutation.mutation(auxState1, PM);
    
    	
    	return auxState1;
	}
    
	@Override
	public State getReference() {
		stateReferenceGA = listState.get(0);
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			for (int i = 1; i < listState.size(); i++) {
				if(stateReferenceGA.getEvaluation().get(0) < listState.get(i).getEvaluation().get(0))
					stateReferenceGA = listState.get(i);
			}
		}
		else{
			for (int i = 1; i < listState.size(); i++) {
				if(stateReferenceGA.getEvaluation().get(0) > listState.get(i).getEvaluation().get(0))
					stateReferenceGA = listState.get(i);
			}
		}
		return stateReferenceGA;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceGA = stateRef;
	}
	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceGA = stateInitialRef;
	}

	@Override
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		listState = replace.replace(stateCandidate, listState);
	}
	
	public List<State> getListState() {
		return listState;
	}

	public void setListState(List<State> listState) {
		this.listState = listState;
	}
	
	public List<State> getListStateRef(){
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;

		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			if(key.get(count).equals(GeneratorType.GeneticAlgorithm.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				GeneticAlgorithm generator = (GeneticAlgorithm) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListState().isEmpty()){
					listState.addAll(RandomSearch.listStateReference);

				}
				else{
					listState = generator.getListState();
				}
			        found = true;
			}
			count++;
		}
		return listState;
	}

	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	@Override
	public GeneratorType getType() {
		return this.generatorType;
	}

	@Override
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < listState.size(); i++) {
			State value = listState.get(i);
			ReferenceList.add(value);
		}
		return ReferenceList;
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
