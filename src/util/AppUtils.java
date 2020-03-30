package util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class AppUtils {
	public static void main(String[] args) {
		//ArrayList<Integer> aux = sequence("1:2:9");
		String whatever0 = "firstname"+"\t"+"\t"+"lastname";
		 String whatever1 = "firstname"+"\t"+"\t"+"\t"+"lastname";
		 System.out.println(whatever0);
		 System.out.println(whatever1);
		
	}
	
	public static String getFileExtension(File file) {
		if(file.getName().split("[.]").length>1) return file.getName().split("[.]")[1];
		else return "0";
	}
	

	public static File ordenagailuanGorde(Instances data,File fitx) {
		try {
			//filewriter prestatu
			FileWriter writer = new FileWriter(fitx);

			//fitxategia prozezatu
			String name = fitx.getName().split("\\.")[0].split("[.]")[0]; //fitxeroaren izena lortzen du, parent barruan
			writer.append("@relation " +name+"\n\n");
			
			int num = data.numAttributes();
			for (int i = 0; i < num; i++) {
				writer.append(data.attribute(i).toString()+"\n");
				writer.flush();
			}
			writer.append("\n@data\n");
			for (Instance instance : data) {
				writer.append(instance.toString()+"\n");
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("ordenagailuanGorde error");
		}
		System.out.println("File saved:  "+fitx);
		return fitx;
	}
	
	public static void disableWarning() {
	    System.err.close();
	    System.setErr(System.out);
	}
	
	public static int minIndex(Instances data) {
		int[] count = data.attributeStats(data.classIndex()).nominalCounts;
		int minValue = count[0];
		int minIndex = 0;
		int numAtributes = count.length;
		for (int i = 0; i < numAtributes; i++) {
			int val = count[i];
			if(val<minValue && val!=0) {
				minValue = val;
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public static ArrayList<Integer> sequence(String seq){
		//ej: 1:10:2
		ArrayList<Integer> emaitza = new ArrayList<Integer>();
		String[] data = seq.split(":.");
		for (String num : data) {
			emaitza.add(Integer.parseInt(num));
		}
		return emaitza;
	}
	
	public static Instances file2instances(String file,String classIndex) throws Exception {
		Instances data = null;
		DataSource source = new DataSource(file);
		data = source.getDataSet();
		if(classIndex.equals("last")) {
			data.setClassIndex(data.numAttributes()-1);
		}else {
			data.setClassIndex(Integer.parseInt(classIndex));
		}
		return data;
	}
	
	public static Double getMean(ArrayList<Double> in) {
		Double total = 0.0;
		for (Double val : in) {
			total = total+val;
		}
		return total/in.size();
	}
}
