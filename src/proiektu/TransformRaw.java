package proiektu;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class TransformRaw {
	private static String procesedFilesPath;


	public static void main(String[] args) {
		try {
			File arff = new File(args[0]);
			
			toWordVector(arff);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
	public static File toWordVector(File originalArff) {
		/* Lehen sortutako arrf fitxategiaren textuak hitzetan banatzen du
		 * in:  originalArff
		 * out: words.arff in parent folder
		 * 	  return wordsArff
		 */
		System.out.println("atento");
		System.out.println();
		if(originalArff.getParentFile().getName().equals("procesedFiles")) procesedFilesPath = originalArff.getParent();
		else procesedFilesPath = originalArff.getParent()+"\\procesedFiles";
		
		//arrf-a kargatu
		Instances data=null;
		try {
			DataSource source = new DataSource(originalArff.getAbsolutePath());
			data = source.getDataSet();
		} catch (Exception e) {
			System.out.println("toWordVector - arrf karga error");
		}
		
		//filtroa aplikatu - String-ak hitzetan banantzeko
		Instances wordsData=null;
		try {
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(data);
			wordsData = Filter.useFilter(data, filter);
		} catch (Exception e) {
			System.out.println("toWordVector - filter error");
		}
		
		//Fitxategia sortu, words.arrf, filtroa aplikatuta dago
		File wordsArff = new File(procesedFilesPath+"\\words.arff");
		System.out.println(procesedFilesPath);
	    System.out.println("prue1:   "+wordsArff);
	    System.out.println("prue2:   "+wordsArff);
		try {
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(wordsData);
		    saver.setFile(wordsArff);
		    saver.writeBatch();
		} catch (Exception e) {
			System.out.println("toWordVector - file creator error");
		}
		
		return wordsArff;
	}

}
