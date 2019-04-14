package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.SelectionType;




public interface IFFactoryFatherSelection {
	
	FatherSelection createSelectFather(SelectionType selectionType)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
