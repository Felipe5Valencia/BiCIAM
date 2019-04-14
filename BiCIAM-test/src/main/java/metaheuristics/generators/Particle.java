package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class Particle extends Generator {

	private State statePBest;
	private State stateActual;
	private ArrayList<Object> velocity;
	
	
	public Particle() {
		super();
		this.stateActual = new State();
		this.statePBest = new State();
		this.velocity = new ArrayList<Object>();
	}
	
	public Particle(State statePBest, State stateActual, ArrayList<Object> velocity) {
		super();
		this.statePBest = statePBest;
		this.stateActual = stateActual;
		this.velocity = velocity;
	}

	public ArrayList<Object> getVelocity() {
		return velocity;
	}

	public void setVelocity(ArrayList<Object> velocity) {
		this.velocity = velocity;
	}

	public State getStatePBest() {
		return statePBest;
	}

	public void setStatePBest(State statePBest) {
		this.statePBest = statePBest;
	}

	public State getStateActual() {
		return stateActual;
	}

	public void setStateActual(State stateActual) {
		this.stateActual = stateActual;
	}

	@Override
	public State generate(Integer operatornumber)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		
		ArrayList<Object> actualVelocity = UpdateVelocity();
		ArrayList<Object> newCode = UpdateCode(actualVelocity);
		this.velocity = actualVelocity;
		this.stateActual.setCode(newCode);
		return null;
	}
	
	
	private ArrayList<Object> UpdateVelocity(){ // actualizar velocidad
    	double w = ParticleSwarmOptimization.wmax - ((ParticleSwarmOptimization.wmax - ParticleSwarmOptimization.wmin) / Strategy.getStrategy().getCountMax()) * ParticleSwarmOptimization.countCurrentIterPSO;  //CALCULO DE LA INERCIA
    	double rand1 = (double)(Math.random() * (double)(1));
    	double rand2 = (double)(Math.random() * (double)(1));
    	double inertia, cognitive, social;
    	int learning = ParticleSwarmOptimization.learning1 + ParticleSwarmOptimization.learning2; // ratios de aprendizaje cognitivo y social
    	ParticleSwarmOptimization.constriction = 2/(Math.abs(2 - learning-Math.sqrt((learning * learning)- 4 * learning)));   // Factor de costriccion
    	ArrayList<Object> actualVelocity = new ArrayList<Object>();
    	if(velocity.isEmpty()){
    		for (int i = 0; i < Strategy.getStrategy().getProblem().getState().getCode().size(); i++){
    			velocity.add(0.0);
    		}
    	}
    	// recorre el vector velocidad y lo actualiza
    	for (int i = 0; i < Strategy.getStrategy().getProblem().getState().getCode().size(); i++) {  
    		// cumulo donde se encuentra la particula
    		int swarm = ParticleSwarmOptimization.countParticle / ParticleSwarmOptimization.countParticleBySwarm; 
           	inertia = w * (Double)velocity.get(i);  
           	if(ParticleSwarmOptimization.binary == true){
           		cognitive = (Double)(ParticleSwarmOptimization.learning1 * rand1 * ((Integer)(this.statePBest.getCode().get(i)) - (Integer)(stateActual.getCode().get(i))));
           		social = (Double)(ParticleSwarmOptimization.learning2 * rand2 * (((Integer)(((State) ParticleSwarmOptimization.lBest[swarm]).getCode().get(i))) - ((Integer)(stateActual.getCode().get(i)))));
           	}
           	else{
           		cognitive = (Double)(ParticleSwarmOptimization.learning1 * rand1 * ((Double)(this.statePBest.getCode().get(i)) - (Double)(stateActual.getCode().get(i))));
           		social = (Double)(ParticleSwarmOptimization.learning2 * rand2 * (((Double)(((State) ParticleSwarmOptimization.lBest[swarm]).getCode().get(i))) - ((Double)(stateActual.getCode().get(i)))));
           	}
        	actualVelocity.add(ParticleSwarmOptimization.constriction*(inertia + cognitive + social));
        }
    /*    if (ParticleSwarmOptimization.binary == true){
    		for (int i = 0; i < actualVelocity.size(); i++) {
				binaryVelocity.add((3 + (Integer)actualVelocity.get(i))%3 - 1);     //FORMULA DE LA VELOCIDAD PARA CODIFICACION BINARIA  
			}
    		return binaryVelocity;
    	}*/
        return actualVelocity;
    }
	
	private ArrayList<Object> UpdateCode(ArrayList<Object> actualVelocity) {  // CALCULO DE LA NUEA POSICION DE LA PARTICULA
		ArrayList<Object> newCode = new ArrayList<Object>();
		ArrayList<Object> binaryCode = new ArrayList<Object>();
		//poner la condicion de si se esta trabajando con valores continuos o binarios
		if(ParticleSwarmOptimization.binary == false){	
			for (int i = 0; i < stateActual.getCode().size(); i++) {
					newCode.add( (Double)(stateActual.getCode().get(i)) + (Double)(actualVelocity.get(i)) );
		    }
			return newCode;
	    }
		 else{                                                  //cálculo de la posicion para codificacion binaria
			  for (int i = 0; i < stateActual.getCode().size(); i++){
				  double rand = (double)(Math.random() * (double)(1));
				  double s = 1/(1 + 1.72 * (Double)(actualVelocity.get(i))); // 
				  if (rand < s){
				     binaryCode.add(1);
				  }
				   else{
				    	binaryCode.add(0);
				    	}
			  }
	          return binaryCode;
    }
			/*	if(ParticleSwarmOptimization.binary == true){//wendy
					for (int i = 0; i < newCode.size(); i++) {
						binaryCode.add(4 + ((Integer)newCode.get(i))%2);     //FORMULA DE LA POSICION PARA CODIFICACION BINARIA  
					}
					return binaryCode;
				}*/
		
	}
	

	@Override
	public void updateReference(State stateCandidate,
			Integer countIterationsCurrent) throws IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if(stateActual.getEvaluation().get(0) > statePBest.getEvaluation().get(0)){
				statePBest.setCode(new ArrayList<Object>(stateActual.getCode()));
				statePBest.setEvaluation(stateActual.getEvaluation());
			}
		}
		else{
			if(stateCandidate.getEvaluation().get(0) < statePBest.getEvaluation().get(0)){
				statePBest.setCode(new ArrayList<Object>(stateCandidate.getCode()));
				statePBest.setEvaluation(stateCandidate.getEvaluation());
			}
		}
		
	}

	@Override
	public State getReference() {
		return null;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {

	}

	@Override
	public GeneratorType getType() {
		return null;
	}

	@Override
	public List<State> getReferenceList() {
		return null;
	}

	@Override
	public List<State> getSonList() {
		return null;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		return false;
	}

	@Override
	public void setWeight(float weight) {

	}

	@Override
	public float getWeight() {
		return 0;
	}

	@Override
	public float[] getTrace() {
		return null;
	}

	@Override
	public int[] getListCountBetterGender() {
		return null;
	}

	@Override
	public int[] getListCountGender() {
		return null;
	}


}
