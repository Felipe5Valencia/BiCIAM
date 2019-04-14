package metaheuristics.generators;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;


public abstract class Generator {

	public abstract State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	public abstract void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	public abstract State getReference();

	public abstract void setInitialReference (State stateInitialRef);

	public abstract GeneratorType getType ();

	public abstract List<State> getReferenceList();

	public abstract List<State> getSonList ();

	public abstract boolean awardUpdateREF(State stateCandidate);

	public abstract void setWeight(float weight);

	public abstract float getWeight();

	
	public abstract float[] getTrace();
	public int countGender;
	public int countBetterGender;
	public abstract int[] getListCountBetterGender();
	public abstract int[] getListCountGender();
	public int[] listCountBetterGender; // arreglo con las mejoras de cada generador en un periodo de 10, acumulativo
	

}
