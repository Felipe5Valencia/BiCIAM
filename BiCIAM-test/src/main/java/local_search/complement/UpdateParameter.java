package local_search.complement;

import factory_interface.IFFactoryGenerator;
import factory_method.FactoryGenerator;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.ParticleSwarmOptimization;


public class UpdateParameter {
	
	private static IFFactoryGenerator ifFactoryGenerator;
	
	public static Integer updateParameter(Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {//HashMap<String, Object> map, 
		countIterationsCurrent = countIterationsCurrent + 1;
		if(countIterationsCurrent.equals(GeneticAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.GeneticAlgorithm);
		}
		else{
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
		return countIterationsCurrent;
	}
}
	


