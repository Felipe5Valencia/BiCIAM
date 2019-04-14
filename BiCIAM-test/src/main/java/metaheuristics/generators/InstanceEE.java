package metaheuristics.generators;

import factory_method.FactoryGenerator;

public class InstanceEE implements Runnable {

	private boolean terminate = false;
	
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorEE = null;
		try {
			generatorEE = ifFactoryGenerator.createGenerator(GeneratorType.EvolutionStrategies);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.EvolutionStrategies)){
				MultiGenerator.getListGenerators()[i] = generatorEE;
				find = true;
			}
			else i++;	
		}
		terminate = true;
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

}
