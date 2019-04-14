package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import factory_interface.IFFactoryReplace;





public class FactoryReplace implements IFFactoryReplace {

private Replace replace;
	
	public Replace createReplace( ReplaceType typereplace ) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String className = "evolutionary_algorithms.complement." + typereplace.toString();
		replace = (Replace) FactoryLoader.getInstance(className);
		return replace;
	}
}
