package local_search.complement;

import java.util.ArrayList;
import java.util.List;

import problem.definition.State;

public class TabuSolutions {
	
	public static List<State> listTabu = new ArrayList<State>();

	public static int maxelements; 

	public List<State> filterNeighborhood(List<State> listNeighborhood) throws Exception {
		List<State> listFiltrate = new ArrayList<State>();
		if (!listTabu.isEmpty()) {
			for (int i = listNeighborhood.size() - 1; i >= 0 ; i--) {
				int count_tabu = 0; 
				while (listTabu.size() > count_tabu) {
					if (listNeighborhood.get(i).equals(listTabu.get(count_tabu))) {
						listNeighborhood.remove(i);
					}
					count_tabu++;
				}
			}
			listFiltrate = listNeighborhood;
			if (listFiltrate.isEmpty()) {
				throw new Exception();
			}
		} else {
			listFiltrate = listNeighborhood;
		}
		return listFiltrate;
	}
}