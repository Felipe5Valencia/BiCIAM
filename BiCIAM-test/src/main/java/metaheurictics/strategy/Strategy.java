package metaheurictics.strategy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import factory_interface.IFFactoryGenerator;
import factory_method.FactoryGenerator;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

import local_search.acceptation_type.Dominance;
import local_search.complement.StopExecute;
import local_search.complement.UpdateParameter;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.MultiGenerator;
import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.RandomSearch;

public class Strategy {

	private static Strategy strategy = null;
	private State bestState;
	private Problem problem;
	public SortedMap<GeneratorType, Generator> mapGenerators;
	private StopExecute stopexecute;
	private UpdateParameter updateparameter;
	private IFFactoryGenerator ifFactoryGenerator;
	private int countCurrent;
	private int countMax;
	public Generator generator;
	public double threshold;

	public ArrayList<State> listStates; 
	public ArrayList<State> listBest; 
	public List<State> listRefPoblacFinal = new ArrayList<State> (); 
	public Dominance notDominated; 

	
	public boolean saveListStates; 
	public boolean saveListBestStates; 
	public boolean saveFreneParetoMonoObjetivo;
	public boolean calculateTime;
	
	long initialTime;
	long finalTime;
	public static long timeExecute;
	
	public float[] listOfflineError = new float[100]; 
	public int countPeriodChange = 0; 
	public int countChange = 0;
	private int countPeriodo;
	private int periodo;


	private Strategy(){
		super();
	}

	public static Strategy getStrategy() {
		if (strategy == null) {
			strategy = new Strategy();
		}
		return strategy;
	}

	public void executeStrategy (int countmaxIterations, int countIterationsChange, int operatornumber, GeneratorType generatorType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		if(calculateTime == true){
			initialTime = System.currentTimeMillis();
		}
		this.countMax = countmaxIterations;
		Generator randomInitial = new RandomSearch();
		State initialState = randomInitial.generate(operatornumber);
		problem.Evaluate(initialState);
		initialState.setTypeGenerator(generatorType);
		getProblem().setState(initialState);
		if(saveListStates ==  true){
			listStates = new ArrayList<State>();
			listStates.add(initialState);
		}
	
		if(saveListBestStates == true){
			listBest = new ArrayList<State>(); 
			listBest.add(initialState);
		}
		if(saveFreneParetoMonoObjetivo == true){
			notDominated = new Dominance();
		}

		generator = newGenerator(generatorType);
		generator.setInitialReference(initialState);
		bestState = initialState;
		countCurrent = 0;
		listRefPoblacFinal = new ArrayList<State>();
		MultiGenerator multiGenerator = null;
		countPeriodChange = countIterationsChange;
		countChange = countIterationsChange;
		countPeriodo = countIterationsChange / 10; 
		if(generatorType.equals(GeneratorType.MultiGenerator)){
			initializeGenerators();
			MultiGenerator.initializeGenerators();
			MultiGenerator.listGeneratedPP.clear();
			multiGenerator = (MultiGenerator)((MultiGenerator)generator).clone();
		}
		else initialize();
		update(countCurrent);
		
		float sumMax = 0; 
		int countOff = 0; 
		while (!stopexecute.stopIterations(countCurrent, countmaxIterations)){
			if(countCurrent == countChange){
				calculateOffLinePerformance(sumMax, countOff);
				countOff++;
				sumMax = 0;
				updateRef(generatorType);
				countChange = countChange + countPeriodChange;
				State stateCandidate = null;
				if(generatorType.equals(GeneratorType.MultiGenerator)){
					if(countPeriodo == countCurrent){
						updateCountGender();
						countPeriodo = countPeriodo + countPeriodChange / 10;
						periodo = 0;
						MultiGenerator.activeGenerator.countBetterGender = 0;
					}
					updateWeight();
					stateCandidate = multiGenerator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					multiGenerator.updateReference(stateCandidate, countCurrent);
				}
				else {
					stateCandidate = generator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					generator.updateReference(stateCandidate, countCurrent);
					if(saveListStates ==  true){
						listStates.add(stateCandidate);
					}
				}
				
				if ((getProblem().getTypeProblem().equals(ProblemType.Maximizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) < stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if ((problem.getTypeProblem().equals(ProblemType.Minimizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) > stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if(saveListBestStates == true){
					listBest.add(bestState);
				}
				sumMax = (float) (sumMax + bestState.getEvaluation().get(0));

			}
			else {
				State stateCandidate = null;
				if(generatorType.equals(GeneratorType.MultiGenerator)){
					if(countPeriodo == countCurrent){
						updateCountGender();
						countPeriodo = countPeriodo + countPeriodChange / 10;
						periodo++;
						MultiGenerator.activeGenerator.countBetterGender = 0;
					}
					stateCandidate = multiGenerator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					multiGenerator.updateReference(stateCandidate, countCurrent);
				}
				else {
					stateCandidate = generator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					generator.updateReference(stateCandidate, countCurrent); 
					if(saveListStates ==  true){
						listStates.add(stateCandidate);
					}
					if(saveFreneParetoMonoObjetivo == true){
						notDominated.ListDominance(stateCandidate, listRefPoblacFinal);
					}
				}
				countCurrent = UpdateParameter.updateParameter(countCurrent);
				if ((getProblem().getTypeProblem().equals(ProblemType.Maximizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) < stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if ((problem.getTypeProblem().equals(ProblemType.Minimizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) > stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if(saveListBestStates == true){
					listBest.add(bestState);
				}
				sumMax = (float) (sumMax + bestState.getEvaluation().get(0));
			}
		}
		if(calculateTime == true){
			finalTime = System.currentTimeMillis();
			timeExecute = finalTime - initialTime;
			System.out.println("El tiempo de ejecucion: " + timeExecute);
		}
		if(generatorType.equals(GeneratorType.MultiGenerator)){
			listBest = (ArrayList<State>) multiGenerator.getReferenceList();
			calculateOffLinePerformance(sumMax, countOff);
			if(countPeriodo == countCurrent){
				updateCountGender();
			}
		}
		else{
			listBest = (ArrayList<State>) generator.getReferenceList();
			calculateOffLinePerformance(sumMax, countOff);
		} 
	}
	
	public void updateCountGender(){ 
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MultiGenerator) ){
				MultiGenerator.getListGenerators()[i].getListCountGender()[periodo] = MultiGenerator.getListGenerators()[i].countGender + MultiGenerator.getListGenerators()[i].getListCountGender()[periodo];
				MultiGenerator.getListGenerators()[i].getListCountBetterGender()[periodo] = MultiGenerator.getListGenerators()[i].countBetterGender + MultiGenerator.getListGenerators()[i].getListCountBetterGender()[periodo];
				MultiGenerator.getListGenerators()[i].countGender = 0;
				MultiGenerator.getListGenerators()[i].countBetterGender = 0;
			}
		}
	}
	
	public void updateWeight(){
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MultiGenerator)){
				MultiGenerator.getListGenerators()[i].setWeight((float) 50.0);
			}
		}
	}
	
	
	public void update(Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException { 
		if(countIterationsCurrent.equals(GeneticAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.GeneticAlgorithm);
		}
		if(countIterationsCurrent.equals(EvolutionStrategies.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.EvolutionStrategies);
		}			
		if(countIterationsCurrent.equals(DistributionEstimationAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.DistributionEstimationAlgorithm);
		}
		if(countIterationsCurrent.equals(ParticleSwarmOptimization.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.ParticleSwarmOptimization);
		}
	}

	public Generator newGenerator(GeneratorType Generatortype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifFactoryGenerator = new FactoryGenerator();
		Generator generator = ifFactoryGenerator.createGenerator(Generatortype);
		return generator;
	}

	public State getBestState() {
		return bestState;
	}

	public void setBestState(State besState) {
		this.bestState = besState;
	}
	public StopExecute getStopexecute() {
		return stopexecute;
	}

	public int getCountMax() {
		return countMax;
	}

	public void setCountMax(int countMax) {
		this.countMax = countMax;
	}

	public void setStopexecute(StopExecute stopexecute) {
		this.stopexecute = stopexecute;
	}

	public UpdateParameter getUpdateparameter() {
		return updateparameter;
	}

	public void setUpdateparameter(UpdateParameter updateparameter) {
		this.updateparameter = updateparameter;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public ArrayList<String> getListKey(){
		ArrayList<String> listKeys = new ArrayList<String>();
		String key = mapGenerators.keySet().toString();
		String returnString = key.substring(1, key.length() - 1);
		returnString = returnString + ", ";
		int countKey = mapGenerators.size();
		for (int i = 0; i < countKey; i++) {
			String r = returnString.substring(0, returnString.indexOf(','));
			returnString = returnString.substring(returnString.indexOf(',') + 2);
			listKeys.add(r);
		}
		return listKeys;
	}

	public void initializeGenerators()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<GeneratorType>	listType = new ArrayList<GeneratorType>();
		this.mapGenerators = new TreeMap<GeneratorType, Generator>();
		GeneratorType type[]= GeneratorType.values();
		for (GeneratorType generator : type) {
			listType.add(generator);
		}
		for (int i = 0; i < listType.size(); i++) {
			Generator generator = newGenerator(listType.get(i));
			mapGenerators.put(listType.get(i), generator);
		}
	}

	public void initialize()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<GeneratorType>	listType = new ArrayList<GeneratorType>();
		this.mapGenerators = new TreeMap<GeneratorType, Generator>();
		GeneratorType type[]= GeneratorType.values();
		for (GeneratorType generator : type) {
			listType.add(generator);
		}
		for (int i = 0; i < listType.size(); i++) {
			Generator generator = newGenerator(listType.get(i));
			mapGenerators.put(listType.get(i), generator);
		}
	}

	public int getCountCurrent() {
		return countCurrent;
	}

	public void setCountCurrent(int countCurrent) {
		this.countCurrent = countCurrent;
	}

	public static void destroyExecute() {
		strategy = null;
		RandomSearch.listStateReference = null;
	}
	
	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public void calculateOffLinePerformance(float sumMax, int countOff){
		float off = sumMax / countPeriodChange;
		listOfflineError[countOff] = off;
	}
	
	public void updateRef(GeneratorType generatorType){
		if(generatorType.equals(GeneratorType.MultiGenerator)){
			updateRefMultiG();
			bestState = MultiGenerator.listStateReference.get( MultiGenerator.listStateReference.size() - 1);
		}
		else{
			updateRefGenerator(generator);
			bestState = generator.getReference();
		}
	}
	public void updateRefMultiG() {
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			updateRefGenerator(MultiGenerator.getListGenerators()[i]);
		}
	}
	public void updateRefGenerator(Generator generator) {
		if(generator.getType().equals(GeneratorType.HillClimbing) || generator.getType().equals(GeneratorType.TabuSearch) || generator.getType().equals(GeneratorType.RandomSearch) || generator.getType().equals(GeneratorType.SimulatedAnnealing)){
			double evaluation = getProblem().getFunction().get(0).Evaluation(generator.getReference());
			generator.getReference().getEvaluation().set(0, evaluation);
		}
		if(generator.getType().equals(GeneratorType.GeneticAlgorithm) || generator.getType().equals(GeneratorType.DistributionEstimationAlgorithm) || generator.getType().equals(GeneratorType.EvolutionStrategies)){
			for (int j = 0; j < generator.getReferenceList().size(); j++) {
				double evaluation = getProblem().getFunction().get(0).Evaluation(generator.getReferenceList().get(j));
				generator.getReferenceList().get(j).getEvaluation().set(0, evaluation);
			}
		}
	}

}
