package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


import metaheurictics.strategy.Strategy;


import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class ParticleSwarmOptimization extends Generator {
	
	private State stateReferencePSO;
	private List<State> listStateReference = new ArrayList<State>(); 
	private List<Particle> listParticle =  new ArrayList<Particle> ();
	private GeneratorType generatorType;
	public static int countRef = 0;            // CANTIDAD DE PARTICULAS TOTAL = coutSwarm * countParticleSwarm
	public static int countParticle = 0;       // CANTIDAD DE PARTICULAS QUE SE HAN MOVIDO EN CADA CUMULO
	public static int coutSwarm = 0;           //CANTIDAD DE CUMULOS
	public static int countParticleBySwarm = 0; //CANTIDAD DE PARTICULAS POR CUMULO
	private float weight = 50;
	public static double wmax = 0.9;
	public static double wmin = 0.2;
	public static int learning1 = 2, learning2 = 2;
	public static double constriction;
	public static boolean binary = false;
	public static State[] lBest; 
	public static State gBest;
	public static int countCurrentIterPSO;
	//problemas dinamicos
    public static int countGender = 0;
    public static int countBetterGender = 0;
    private int[] listCountBetterGender = new int[10];
    private int[] listCountGender = new int[10];
    private float[] listTrace = new float[1200000];
			
	public ParticleSwarmOptimization(){
		super();
		countRef = coutSwarm * countParticleBySwarm;
		this.setListParticle(getListStateRef()); 
//		listStateReference = new ArrayList<State>(Strategy.getStrategy().listBest);
		this.generatorType = GeneratorType.ParticleSwarmOptimization;
		this.weight = 50;
		lBest = new State[coutSwarm];
		if(!listParticle.isEmpty()){
			countCurrentIterPSO = 1;
			inicialiceLBest();
			gBest = gBestInicial();	
		}
		countParticle = 0;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
	}

	@Override
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{ //PSO
		if (countParticle >= countRef)
			countParticle = 0;
//		System.out.println("Contador de particulas: " + countParticle + " Contador de iteraciones " + Strategy.getStrategy().getCountCurrent());
		listParticle.get(countParticle).generate(1);
	    return listParticle.get(countParticle).getStateActual();
	}
   	
	public void inicialiceLBest (){
		for (int j = 0; j < coutSwarm; j++) {
			State reference = new State();
			reference = listParticle.get(countParticle).getStatePBest();
			int iterator = countParticleBySwarm + countParticle;
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				for (int i = countParticle; i < iterator; i++) {
					if (listParticle.get(i).getStatePBest().getEvaluation().get(0) > reference.getEvaluation().get(0))
						reference = listParticle.get(i).getStatePBest();
					countParticle++;
				}
			}
			else{
				for (int i = countParticle; i < iterator; i++) {
					if (listParticle.get(i).getStatePBest().getEvaluation().get(0) < reference.getEvaluation().get(0))
						reference = listParticle.get(i).getStatePBest();
					countParticle++;
				}
			}
			
			lBest[j] = reference;
		}
	}
	
	
	@Override
	public State getReference() {
		return null;
	}
	
	private List<Particle> getListStateRef() {
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		if(RandomSearch.listStateReference.size() == 0){
			return this.setListParticle(new ArrayList<Particle>());
		}
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			//recorrer la lista de generadores, hasta que encuentre el PSO
			if(key.get(count).equals(GeneratorType.ParticleSwarmOptimization.toString())){
				//creo el generador PSO, y si su lista de particulas esta vacia entonces es la primera vez que lo estoy creando, y cada estado lo convierto en particulas
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				ParticleSwarmOptimization generator = (ParticleSwarmOptimization) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListParticle().isEmpty()){
					//convertir los estados en particulas
					for (int j = 0; j < RandomSearch.listStateReference.size(); j++) {
						//si el estado es creado con el generator RandomSearch entonces la convierto en particula
						if(getListParticle().size() != countRef){
							ArrayList<Object> velocity = new ArrayList<Object>();
							State stateAct = (State) RandomSearch.listStateReference.get(j).getCopy();
							stateAct.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
							stateAct.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
							
							State statePBest = (State) RandomSearch.listStateReference.get(j).getCopy();
							statePBest.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
							statePBest.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
							
							Particle particle = new Particle(stateAct, statePBest, velocity);
							getListParticle().add(particle);
						}
					}  
				}
				else{
					setListParticle(generator.getListStateReference());
				}
			        found = true;
			}
			count++;
		}
		return getListParticle();
	}


	public State getStateReferencePSO() {
		return stateReferencePSO;
	}

	public void setStateReferencePSO(State stateReferencePSO) {
		this.stateReferencePSO = stateReferencePSO;
	}

	public List<Particle> getListStateReference() {
		return this.getListParticle();
	}

	public void setListStateReference(List<State> listStateReference) {
		this.listStateReference = listStateReference;
	}

	public List<Particle> getListParticle() {
		return listParticle;
	}

	public List<Particle> setListParticle(List<Particle> listParticle) {
		this.listParticle = listParticle;
		return listParticle;
	}

	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	public static int getCountRef() {
		return countRef;
	}

	public static void setCountRef(int countRef) {
		ParticleSwarmOptimization.countRef = countRef;
	}


	//*****************************************
	@Override
	public void updateReference(State stateCandidate,Integer countIterationsCurrent) throws IllegalArgumentException,SecurityException, ClassNotFoundException, InstantiationException,IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		Particle particle = new Particle();
		particle = listParticle.get(countParticle);
		int swarm = countParticle/countParticleBySwarm;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if ((lBest[swarm]).getEvaluation().get(0) < particle.getStatePBest().getEvaluation().get(0)){
				lBest[swarm] = particle.getStatePBest();
				if(lBest[swarm].getEvaluation().get(0) > getReferenceList().get(getReferenceList().size() - 1).getEvaluation().get(0)){
					gBest = new State();
					gBest.setCode(new ArrayList<Object>(lBest[swarm].getCode()));
					gBest.setEvaluation(lBest[swarm].getEvaluation());
					gBest.setTypeGenerator(lBest[swarm].getTypeGenerator());
				}
			}
		}
		else {
			particle.updateReference(stateCandidate, countIterationsCurrent);
			if ((lBest[swarm]).getEvaluation().get(0) > particle.getStatePBest().getEvaluation().get(0)){
				lBest[swarm] = particle.getStatePBest();
				if(lBest[swarm].getEvaluation().get(0) < getReferenceList().get(getReferenceList().size() - 1).getEvaluation().get(0)){
					gBest = new State();
					gBest.setCode(new ArrayList<Object>(lBest[swarm].getCode()));
					gBest.setEvaluation(lBest[swarm].getEvaluation());
					gBest.setTypeGenerator(lBest[swarm].getTypeGenerator());
				}
			}
		}
		listStateReference.add(gBest);
		countParticle++;
		countCurrentIterPSO++;
	}
	
	public State gBestInicial (){
		State stateBest = lBest[0];
		for (int i = 1; i < lBest.length; i++) {
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				if (lBest[i].getEvaluation().get(0) > stateBest.getEvaluation().get(0)){
					stateBest = lBest[i];
				}
			}
			else{
				if (lBest[i].getEvaluation().get(0) < stateBest.getEvaluation().get(0)){
					stateBest = lBest[i];
				}
			}
		}
		return stateBest;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {

	}

	@Override
	public GeneratorType getType() {
		return this.generatorType;
	}

	@Override
	public List<State> getReferenceList() {
		return this.listStateReference;
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
	public int[] getListCountBetterGender() {
		return this.listCountBetterGender;
	}

	@Override
	public int[] getListCountGender() {
		return this.listCountGender;
	}

	@Override
	public float[] getTrace() {
		return this.listTrace;
	}


	
}
