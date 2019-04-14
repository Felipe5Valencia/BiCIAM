package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;




public interface IFFactoryMutation {
	Mutation createMutation(MutationType typeMutation)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
