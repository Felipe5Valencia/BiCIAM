package problem.definition;

import java.util.List;

public abstract class Operator {
	
		public abstract List<State> generatedNewState(State stateCurrent, Integer operatornumber);
		public abstract List<State> generateRandomState (Integer operatornumber);

	}

