package util;

import java.util.ArrayList;
import java.util.Arrays;

import weka.classifiers.Classifier;

public class ParamReader {
	
	public static boolean isName(String action) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static ArrayList<Integer> addbBagSize(String params){
		ArrayList<String> datuak = new ArrayList<String>();
		datuak.addAll(Arrays.asList(params.split("\\ ")));
		if(datuak.get(0).equals("seq")) {
			return sequence(datuak.get(1));
		}
		return getIntegerArray(datuak);
	}
	
	public static ArrayList<String> addBatchSize(String params){
		//stringekin lan egiten du, baina integer bezalakoak dira
		ArrayList<String> datuak = new ArrayList<String>();
		datuak.addAll(Arrays.asList(params.split("\\ ")));
		if(datuak.get(0).equals("seq")) {
			return stringSequence(datuak.get(1));
		}
		return datuak;
	}
	public static ArrayList<Boolean> addCalcOutBag(String params){
		return null;
	}
	public static ArrayList<Boolean> addDebug(String params){
		return null;
	}
	public static ArrayList<Boolean> addDoNotCheck(String params){
		return null;
	}
	public static ArrayList<Integer> addNumDecimalPlaces(String params){
		ArrayList<String> datuak = new ArrayList<String>();
		datuak.addAll(Arrays.asList(params.split("\\ ")));
		if(datuak.get(0).equals("seq")) {
			return sequence(datuak.get(1));
		}
		return getIntegerArray(datuak);
	}
	public static ArrayList<Integer> addExecutionSlots(String params){
		ArrayList<String> datuak = new ArrayList<String>();
		datuak.addAll(Arrays.asList(params.split("\\ ")));
		if(datuak.get(0).equals("seq")) {
			return sequence(datuak.get(1));
		}
		return getIntegerArray(datuak);
	}
	public static ArrayList<Integer> addClassifiers(String params){
		ArrayList<String> datuak = new ArrayList<String>();
		datuak.addAll(Arrays.asList(params.split("\\ ")));
		if(datuak.get(0).equals("seq")) {
			return sequence(datuak.get(1));
		}
		return getIntegerArray(datuak);
	}
	
	private static ArrayList<Integer> getIntegerArray(ArrayList<String> dautak) {
		ArrayList<Integer> result = new ArrayList<Integer>();
        for(String stringValue : dautak) {
            result.add(Integer.parseInt(stringValue));
        }       
        return result;
	}
	
	public static ArrayList<Integer> sequence(String seq){
		//ej: 1:10:2
		ArrayList<Integer> emaitza = new ArrayList<Integer>();
		String[] data = seq.split("\\:");
		int hasiera = Integer.parseInt(data[0]);
		int amaiera = Integer.parseInt(data[1]);
		int saltoa = Integer.parseInt(data[2]);
		for(int i=hasiera;i<=amaiera;i=i+saltoa) {
			emaitza.add(i);
		}
		return emaitza;
	}
	
	public static ArrayList<String> stringSequence(String seq){
		ArrayList<String> emaitza = new ArrayList<String>();
		String[] data = seq.split("\\:");
		int hasiera = Integer.parseInt(data[0]);
		int amaiera = Integer.parseInt(data[1]);
		int saltoa = Integer.parseInt(data[2]);
		for(int i=hasiera;i<=amaiera;i=i+saltoa) {
			emaitza.add(Integer.toString(i));
		}
		return emaitza;
	}
	

}
