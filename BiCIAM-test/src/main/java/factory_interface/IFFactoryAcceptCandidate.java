/**
 * @(#) IFFactoryAcceptCandidate.java
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;





public interface IFFactoryAcceptCandidate
{
	AcceptableCandidate createAcceptCandidate(AcceptType typeacceptation) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

}
