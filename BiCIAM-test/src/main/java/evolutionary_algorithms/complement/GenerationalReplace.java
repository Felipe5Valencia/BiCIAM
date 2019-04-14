package evolutionary_algorithms.complement;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

public class GenerationalReplace extends Replace {

	@Override
	public List<State> replace(State stateCandidate, List<State> listState) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		listState.remove(0);
		listState.add(stateCandidate);
		/*List<State> sonList = Strategy.getStrategy().generator.getSonList();
		for (int i = 0; i < listState.size(); i++) {
			listState.set(i, sonList.get(i));
		}*/
		return listState;
	}
}
