package proiektu;

import java.io.File;
import java.io.FileWriter;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class AppUtils {
	static String getFileExtensionn(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
	}
	static String getFileExtension(File file) {
		if(file.getName().split("[.]").length>1) return file.getName().split("[.]")[1];
		else return "0";
	}
	
	static void ordenagailuanGordeee(Instances data,File fitx) {
		try {
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(fitx);
		    saver.writeBatch();
	    }catch (Exception e) {
	    	System.out.println("ordenagailuanGorde error");
		}
	}
	
	static void ordenagailuanGorde(Instances data,File fitx) {
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
	}
	
}
