/**
 * @(#) IFFactoryCandidate.java
 */

package factory_interface;

import java.lang.reflect.InvocationTargetException;

import local_search.candidate_type.CandidateType;
import local_search.candidate_type.SearchCandidate;




public interface IFFactoryCandidate
{
	SearchCandidate createSearchCandidate(CandidateType typeCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
