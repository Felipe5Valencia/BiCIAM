package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;




public interface IFFactoryReplace {
	Replace createReplace(ReplaceType typereplace)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
