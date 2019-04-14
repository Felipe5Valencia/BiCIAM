package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryFatherSelection;




public class FactoryFatherSelection implements IFFactoryFatherSelection{
    private FatherSelection selection;
	
    public FatherSelection createSelectFather(SelectionType selectionType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	String className = "evolutionary_algorithms.complement." + selectionType.toString();
		selection = (FatherSelection) FactoryLoader.getInstance(className);
		return selection;
	}
}
