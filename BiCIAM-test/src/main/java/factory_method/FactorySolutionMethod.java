package factory_method;

import java.lang.reflect.InvocationTargetException;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;
import factory_interface.IFFactorySolutionMethod;

public class FactorySolutionMethod implements IFFactorySolutionMethod {

	private SolutionMethod solutionMethod;
	
	public SolutionMethod createdSolutionMethod(TypeSolutionMethod method) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "problem.extension." + method.toString();
		solutionMethod = (SolutionMethod) FactoryLoader.getInstance(className);
		return solutionMethod;
	}

}
