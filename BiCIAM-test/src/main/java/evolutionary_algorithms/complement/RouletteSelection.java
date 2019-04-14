package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;

import metaheuristics.generators.LimitRoulette;

import problem.definition.State;

public class RouletteSelection extends FatherSelection {

	@Override
	public List<State> selection(List<State> listState, int truncation) {
		float totalWeight = 0;
		for (int i = 0; i < listState.size(); i++) {
			totalWeight = (float) (listState.get(i).getEvaluation().get(0) + totalWeight);
		}
		List<Float> listProb = new ArrayList<Float>();
		for (int i = 0; i < listState.size(); i++) {
			float probF = (float) (listState.get(i).getEvaluation().get(0) / totalWeight);
			listProb.add(probF);
		}
		List<LimitRoulette> listLimit = new ArrayList<LimitRoulette>();
		float limitHigh = 0;
		float limitLow = 0;
		for (int i = 0; i < listProb.size(); i++) {
			LimitRoulette limitRoulette = new LimitRoulette();
			limitHigh = listProb.get(i) + limitHigh;
			limitRoulette.setLimitHigh(limitHigh);
			limitRoulette.setLimitLow(limitLow);
			limitLow = limitHigh;
			//			limitRoulette.setGenerator(listGenerators.get(i));
			listLimit.add(limitRoulette);
		}
		List<State> fatherList = new ArrayList<State>();
		for (int j = 0; j < listState.size(); j++) {
			float numbAleatory = (float) (Math.random() * (double)(1));
			boolean find = false;
			int i = 0;
			while ((find == false) && (i < listLimit.size())){
				if((listLimit.get(i).getLimitLow() <= numbAleatory) && (numbAleatory <= listLimit.get(i).getLimitHigh())){
					find = true;
					fatherList.add(listState.get(i));
				}
				else i++;
			}
		}
		return fatherList;
	}
}
