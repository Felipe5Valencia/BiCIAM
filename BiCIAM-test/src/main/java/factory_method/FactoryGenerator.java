package factory_method;


import java.lang.reflect.InvocationTargetException;

import factory_interface.IFFactoryGenerator;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;



public class FactoryGenerator implements IFFactoryGenerator {

	private Generator generator;
	
	public Generator createGenerator(GeneratorType generatorType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "metaheuristics.generators." + generatorType.toString();
		generator = (Generator) FactoryLoader.getInstance(className);
		return generator;
	}
}
