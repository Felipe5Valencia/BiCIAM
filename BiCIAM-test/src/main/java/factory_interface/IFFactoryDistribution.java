package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Distribution;
import evolutionary_algorithms.complement.DistributionType;




public interface IFFactoryDistribution {
	Distribution createDistribution(DistributionType typedistribution) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
