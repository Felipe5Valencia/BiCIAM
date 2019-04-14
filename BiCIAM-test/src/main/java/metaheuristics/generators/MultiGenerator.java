package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import factory_method.FactoryGenerator;

import metaheurictics.strategy.Strategy;

import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class MultiGenerator extends Generator {

	private GeneratorType Generatortype;
	private static Generator[] listGenerators = new Generator[GeneratorType.values().length];
	public static List<State> listGeneratedPP = new ArrayList<State> ();
	public static Generator activeGenerator;
	public static List<State> listStateReference = new ArrayList<State>(); 
	
	public void setGeneratortype(GeneratorType generatortype) {
		Generatortype = generatortype;
	}

	public MultiGenerator(){
		super();
		this.Generatortype = GeneratorType.MultiGenerator;
	}
	

	public static void destroyMultiGenerator(){
		listGeneratedPP.clear();
		//listGenerators.clear();
		listStateReference.clear();
		activeGenerator = null;
		listGenerators = null;
	}
	
	public static void initializeListGenerator()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		listGenerators = new Generator[4];
		Generator generator1 = new HillClimbing();
		Generator generator2 = new EvolutionStrategies();
		Generator generator3 = new LimitThreshold();
		Generator generator4 = new GeneticAlgorithm();
		listGenerators[0] = generator1;
		listGenerators[1] = generator2;
		listGenerators[2] = generator3;
		listGenerators[3] = generator4;
	}
	
	public static void initializeGenerators() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Strategy.getStrategy().initializeGenerators();
		initializeListGenerator();
		State stateREF = new State(Strategy.getStrategy().getProblem().getState());
		listStateReference.add(stateREF);
		for (int i = 0; i < listGenerators.length; i++) {
			if ((listGenerators[i].getType().equals(GeneratorType.HillClimbing)) || (listGenerators[i].getType().equals(GeneratorType.RandomSearch)) || (listGenerators[i].getType().equals(GeneratorType.TabuSearch)) || (listGenerators[i].getType().equals(GeneratorType.SimulatedAnnealing) || (listGenerators[i].getType().equals(GeneratorType.LimitThreshold)))){
				listGenerators[i].setInitialReference(stateREF);
			}
		}
		createInstanceGeneratorsBPP();
		Strategy.getStrategy().listStates = MultiGenerator.getListGeneratedPP();
		
		FactoryGenerator ifFactoryGeneratorEE = new FactoryGenerator();
		Generator generatorEE = ifFactoryGeneratorEE.createGenerator(GeneratorType.EvolutionStrategies);
		
		FactoryGenerator ifFactoryGeneratorGA = new FactoryGenerator();
		Generator generatorGA = ifFactoryGeneratorGA.createGenerator(GeneratorType.GeneticAlgorithm);
		
		FactoryGenerator ifFactoryGeneratorEDA = new FactoryGenerator();
		Generator generatorEDA = ifFactoryGeneratorEDA.createGenerator(GeneratorType.DistributionEstimationAlgorithm);
		
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.EvolutionStrategies)){
				MultiGenerator.getListGenerators()[i] = generatorEE;
			}
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.GeneticAlgorithm)){
				MultiGenerator.getListGenerators()[i] = generatorGA;
			}
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.DistributionEstimationAlgorithm)){
				MultiGenerator.getListGenerators()[i] = generatorEDA;
			}
		}
		
		/*InstanceGA instanceGA = new InstanceGA();
		Thread threadGA = new Thread(instanceGA);
		
		InstanceEE instanceEE = new InstanceEE();
		Thread threadEE = new Thread(instanceEE);
		
		InstanceDE instanceDE = new InstanceDE();
		Thread threadDE = new Thread(instanceDE);
		
		threadGA.start();
		threadEE.start();
		threadDE.start();
		
		boolean stop = false;
		while (stop == false){
			if(instanceEE.isTerminate() == true && instanceGA.isTerminate() == true && instanceDE.isTerminate() == true) 
				stop = true;
		}*/
	}

	
	public static void createInstanceGeneratorsBPP() {
//		int i = 0;
//		boolean find = false;
		Generator generator = new RandomSearch();
//		while (find == false) {
//			if (listGenerators[i].getType().equals(GeneratorType.RandomSearch)) {
//				generator = listGenerators[i];
//				find = true;
//			}
//			else i++;
//		}
		int j = 0;
		while (j < EvolutionStrategies.countRef){
			State stateCandidate;
			try {
				stateCandidate = generator.generate(1);
				Strategy.getStrategy().getProblem().Evaluate(stateCandidate);
				//stateCandidate.setEvaluation(stateCandidate.getEvaluation());
				
				//stateCandidate = generator.generate(1);
				//Double evaluation = Strategy.getStrategy().getProblem().getFunction().get(0).Evaluation(stateCandidate);
				//stateCandidate.getEvaluation().set(0, evaluation);
				stateCandidate.setNumber(j);
				stateCandidate.setTypeGenerator(generator.getType());
				listGeneratedPP.add(stateCandidate);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			j++;
		}
	}
	
	private static ArrayList<State> getListGeneratedPP() {
		return (ArrayList<State>) listGeneratedPP;
	}

	public static Generator[] getListGenerators() {
		return listGenerators;
	}

	public static void setListGenerators(Generator[] listGenerators) {
		MultiGenerator.listGenerators = listGenerators;
	}

	public static Generator getActiveGenerator() {
		return activeGenerator;
	}

	public static void setActiveGenerator(Generator activeGenerator) {
		MultiGenerator.activeGenerator = activeGenerator;
	}

	public static void setListGeneratedPP(List<State> listGeneratedPP) {
		MultiGenerator.listGeneratedPP = listGeneratedPP;
	}

	@Override
	public State generate(Integer operatornumber)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Strategy.getStrategy().generator = roulette();
		activeGenerator = Strategy.getStrategy().generator;
		activeGenerator.countGender++;
		State state = Strategy.getStrategy().generator.generate(1);
		return state;
	}

	@Override
	public State getReference() {
		return null;
	}

	@Override
	public List<State> getReferenceList() {
		return listStateReference;
	}

	@Override
	public List<State> getSonList() {
		return null;
	}

	@Override
	public GeneratorType getType() {
		return this.Generatortype;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
	}

	@Override
	public void updateReference(State stateCandidate,
			Integer countIterationsCurrent) throws IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		updateWeight(stateCandidate);
		tournament(stateCandidate, countIterationsCurrent);
		/*if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if((stateCandidate.getEvaluation() > listStateReference.get(listStateReference.size() - 1).getEvaluation()))
				listStateReference.add(stateCandidate);
			else listStateReference.add(listStateReference.get(listStateReference.size() - 1)); 
		}
		else{
			if((Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar)) && (stateCandidate.getEvaluation() < listStateReference.get(listStateReference.size() - 1).getEvaluation()))
				listStateReference.add(stateCandidate);
			else listStateReference.add(listStateReference.get(listStateReference.size() - 1));
		} */
	}
	
	public void updateWeight(State stateCandidate) { 
		boolean search = searchState(stateCandidate);//premio por calidad. 
		if(search == false)
			updateAwardImp();
		else updateAwardSC();
	}
	
	public boolean searchState(State stateCandidate) {
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if(stateCandidate.getEvaluation().get(0) > Strategy.getStrategy().getBestState().getEvaluation().get(0)){
				if(stateCandidate.getEvaluation().get(0) > Strategy.getStrategy().getBestState().getEvaluation().get(0))
					activeGenerator.countBetterGender++;
//				System.out.println(activeGenerator.getType().toString() + activeGenerator.countBetterGender);
//				System.out.println(activeGenerator.countBetterGender);
				return true;
			}
			else return false;
		}
		else {
			if(stateCandidate.getEvaluation().get(0) < Strategy.getStrategy().getBestState().getEvaluation().get(0)){
				if(stateCandidate.getEvaluation().get(0) < Strategy.getStrategy().getBestState().getEvaluation().get(0))
					activeGenerator.countBetterGender++;
//				System.out.println(activeGenerator.getType().toString() + activeGenerator.countBetterGender);
//				System.out.println(activeGenerator.countBetterGender);
				return true;
			}
			else return false;
		}
		
		
	}
	
	@Override
	public float getWeight() {
		return 0;
	}
	
	public Generator roulette() {
		float totalWeight = 0;
		for (int i = 0; i < listGenerators.length; i++) {
			totalWeight = listGenerators[i].getWeight() + totalWeight;
		}
		List<Float> listProb = new ArrayList<Float>();
		for (int i = 0; i < listGenerators.length; i++) {
			float probF = listGenerators[i].getWeight() / totalWeight;
			listProb.add(probF);
		}
		List<LimitRoulette> listLimit = new ArrayList<LimitRoulette>();
		float limitHigh = 0;
		float limitLow = 0;
		for (int i = 0; i < listProb.size(); i++) {
			LimitRoulette limitRoulette = new LimitRoulette();
			limitHigh = listProb.get(i) + limitHigh;
			limitRoulette.setLimitHigh(limitHigh);
			limitRoulette.setLimitLow(limitLow);
			limitLow = limitHigh;
			limitRoulette.setGenerator(listGenerators[i]);
			listLimit.add(limitRoulette);
		}
		float numbAleatory = (float) (Math.random() * (double)(1));
		boolean find = false;
		int i = 0;
		while ((find == false) && (i < listLimit.size())){
			if((listLimit.get(i).getLimitLow() <= numbAleatory) && (numbAleatory <= listLimit.get(i).getLimitHigh())){
				find = true;
			}
			else i++;
		}
		if (find) {
			return listLimit.get(i).getGenerator();
		}
		else return listLimit.get(listLimit.size() - 1).getGenerator();
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		return false;
	}
	
	@SuppressWarnings("static-access")
	public void updateAwardSC() {
		float weightLast = activeGenerator.getWeight();
		float weightUpdate = (float) (weightLast * (1 - 0.1) + 10);
		activeGenerator.setWeight(weightUpdate);
//		activeGenerator.getTrace()getTrace().add(weightUpdate);
		for (int i = 0; i < listGenerators.length; i++) {
			if(listGenerators[i].equals(activeGenerator))
				activeGenerator.getTrace()[Strategy.getStrategy().getCountCurrent()] = weightUpdate;
			else{
				if(!listGenerators[i].getType().equals(Generatortype.MultiGenerator)){
					float trace = listGenerators[i].getWeight();
					listGenerators[i].getTrace() [Strategy.getStrategy().getCountCurrent()] = trace;
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public void updateAwardImp() {
		float weightLast = activeGenerator.getWeight();
		float weightUpdate = (float) (weightLast * (1 - 0.1));
		activeGenerator.setWeight(weightUpdate);
//		activeGenerator.getTrace().add(weightUpdate);
		for (int i = 0; i < listGenerators.length; i++) {
			if(listGenerators[i].equals(activeGenerator))
				activeGenerator.getTrace()[Strategy.getStrategy().getCountCurrent()] = weightUpdate;
			else{
				if(!listGenerators[i].getType().equals(Generatortype.MultiGenerator)){
					float trace = listGenerators[i].getWeight();
					listGenerators[i].getTrace() [Strategy.getStrategy().getCountCurrent()] = trace;
				}
			}
		}
	}
	
	@Override
	public void setWeight(float weight) {
		
	}
	@Override
	public float[] getTrace() {
		return null;
	}
	
	@SuppressWarnings("static-access")
	public void tournament(State stateCandidate,Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateTem = new State(stateCandidate);
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(!listGenerators[i].getType().equals(Generatortype.MultiGenerator))
				MultiGenerator.getListGenerators()[i].updateReference(stateTem, countIterationsCurrent);
		}
	}
	
	public Object clone(){
		return this;
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
