package local_search.complement;


public class StopExecute {
		
	public Boolean stopIterations(int countIterationsCurrent, int countmaxIterations) {
		if (countIterationsCurrent < countmaxIterations) {
			return false;
		} else {
			return true;
		}
	}
}
