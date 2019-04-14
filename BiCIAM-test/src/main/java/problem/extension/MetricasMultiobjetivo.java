package problem.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jxl.read.biff.BiffException;
import problem.definition.State;

public class MetricasMultiobjetivo {

// % de soluciones q no son miembros del frente de pareto verdadero
	public double TasaError(List<State> solutionsFPcurrent, List<State> solutionsFPtrue) throws BiffException, IOException{
		float tasaError = 0;
		for (int i = 0; i < solutionsFPcurrent.size() ; i++) { // frente de pareto actual
			State solutionVO = solutionsFPcurrent.get(i);
			if(!Contains(solutionVO, solutionsFPtrue)){ // no esta en el frente de pareto verdadero 
				tasaError++;
			}
		}
		double total = tasaError/solutionsFPcurrent.size();
		//System.out.println(solutionsFP.size() + "/" + solutions.size() + "/" + total);
		return total;
	}
	
// % Indica  qué  tan  lejos  están  los  elementos  del frente  de  Pareto  actual  respecto  al  frente  de  Pareto  verdadero	
	public double DistanciaGeneracional(List<State> solutionsFPcurrent, List<State> solutionsFPtrue) throws BiffException, IOException{
		float min = 1000;
		float distancia = 0;
		float distanciaGeneracional = 0;
		for (int i = 0; i < solutionsFPcurrent.size();i++) {
			State solutionVO = solutionsFPcurrent.get(i);
			//Calculando la distancia euclideana entre solutionVO y el miembro más cercano del frente de Pareto verdadero
			min = 1000;
			for (int j = 0; j < solutionsFPtrue.size();j++) { 
				for (int j2 = 0; j2 < solutionVO.getEvaluation().size(); j2++) {
					State solutionFPV = solutionsFPtrue.get(j);
					// porq elevar la distancia al cuadrado
					distancia += (solutionVO.getEvaluation().get(j2) - solutionFPV.getEvaluation().get(j2))*  
							(solutionVO.getEvaluation().get(j2) - solutionFPV.getEvaluation().get(j2)); //ceros si el argumento es el cero, 1.0 si el argumento es mayor que el cero, -1.0 si el argumento está menos del cero
				}
				if(distancia < min){
					min = distancia;
				}
			}
			distanciaGeneracional += min;
		}
		double total = Math.sqrt(distanciaGeneracional)/solutionsFPcurrent.size();
		//System.out.println(total);
		return total;
	}

	public double Dispersion(ArrayList<State> solutions) throws BiffException, IOException{
		//Soluciones obtenidas con la ejecución del algoritmo X
		LinkedList<Float> distancias = new LinkedList<Float>();
		float distancia = 0;
		float min = 1000;
		for (Iterator<State> iter = solutions.iterator(); iter.hasNext();) {
			State solutionVO = (State) iter.next();
			min = 1000;
			for (Iterator<State> iterator = solutions.iterator(); iterator.hasNext();) {
				State solVO = (State) iterator.next();
				for (int i = 0; i < solutionVO.getEvaluation().size(); i++) {
					if(!solutionVO.getEvaluation().equals(solVO.getEvaluation())){
						distancia += (solutionVO.getEvaluation().get(i)- solVO.getEvaluation().get(i));
					}}
				if(distancia < min){
					min = distancia;
				}
			}
			distancias.add(Float.valueOf(min));
		}
		//Calculando las media de las distancias 
		float sum = 0;
		for (Iterator<Float> iter = distancias.iterator(); iter.hasNext();) {
			Float dist = (Float) iter.next();
			sum += dist;
		}
		float media = sum/distancias.size();
		float sumDistancias = 0;
		for (Iterator<Float> iter = distancias.iterator(); iter.hasNext();) {
			Float dist = (Float) iter.next();
			sumDistancias += Math.pow((media - dist),2);
		}
		//Calculando la dispersion
		double dispersion = 0;
		if(solutions.size() > 1){
			dispersion = Math.sqrt((1.0/(solutions.size()-1))*sumDistancias);
		}
		//System.out.println(dispersion);
		return dispersion;
	}
	private boolean Contains(State solA, List<State> solutions){
		int i = 0;
		boolean result = false;
		while(i<solutions.size()&& result==false){
			if(solutions.get(i).getEvaluation().equals(solA.getEvaluation()))
				result=true;
			else
				i++;
		}
		return result;
	}
	public double CalcularMin(ArrayList<Double> allMetrics){
		double min = 1000;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			if(element < min){
				min = element;
			}
		}
		return min;
	}

	public double CalcularMax(ArrayList<Double> allMetrics){
		double max = 0;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			if(element > max){
				max = element;
			}
		}
		return max;
	}
	public double CalcularMedia(ArrayList<Double> allMetrics){
		double sum = 0;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			sum = sum + element;
		}
		double media = sum/allMetrics.size();
		return media;
	}
}
