package util;

import java.io.File;
import java.io.FileWriter;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class AppUtils {
	public static String getFileExtensionn(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
	}
	public static String getFileExtension(File file) {
		if(file.getName().split("[.]").length>1) return file.getName().split("[.]")[1];
		else return "0";
	}
	
	public static void ordenagailuanGordeee(Instances data,File fitx) {
		try {
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(fitx);
		    saver.writeBatch();
	    }catch (Exception e) {
	    	System.out.println("ordenagailuanGorde error");
		}
	}
	
	public static void ordenagailuanGorde(Instances data,File fitx) {
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
	}
	
	public static void disableWarning() {
	    System.err.close();
	    System.setErr(System.out);
	}
	
	public static int minIndex(Instances data) {
		int[] count = data.attributeStats(data.classIndex()).nominalCounts;
		int minValue = count[0];
		int minIndex = 0;
		for (int i = 0; i < data.numAttributes()-3; i++) {
			int val = count[i];
			if(val<minValue && val!=0) {
				minValue = val;
				minIndex = i;
			}
		}
		return minIndex;
	}
	
}
