package factory_interface;

import java.lang.reflect.InvocationTargetException;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;

public interface IFFactorySolutionMethod {
	
	SolutionMethod createdSolutionMethod(TypeSolutionMethod  method) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;

}
