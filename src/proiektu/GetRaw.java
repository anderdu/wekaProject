package proiektu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class GetRaw {
	private static String procesedFilesPath = null; //Sortutzako fitxategiak hemen gordeko dira, parent forlder barruan
	/*
	 * Raw data: Prozesatu gabeko datuak
	 * 
	 */
	
	public static void main(String[] args) {
		try {
			getRaw(args[0]);
		} catch (Exception e) {
			System.out.println("error: GetRaw");
		}
	}
	
	public static File getRaw(String csvPath) {
		/* Aurrebaldintzak: lehen lerroa atributuak definitzen ditu
		 *     hurrengo lerroak instantziak izango dira komekin bananduak
		 * in: raw file, without data processsed
		 * out: 
		 *    file 1: parsed csv, fixing conflict errors
		 *    file 2: f1 converted in arrf
		 * 	  return: f2
		 * 
		 * PROIEKTU
		 * getRawARFF.jar  train train.arff
		 *    
		 */
		File parsedCSV=null;
		File newArff=null;
		try {
			//1. CSV aurreprozezatu erroreak kentzeko
			File origFile = new File(csvPath);
			procesedFilesPath = origFile.getParent()+"\\procesedFiles";
			parsedCSV = fileParser(origFile);
		} catch (Exception e) {
			System.out.println("getRaw - fileParser error");
		}
		try {
			//2. arrf fitxategia sortu
			newArff = csvToArrf(parsedCSV);
		} catch (Exception e) {
			System.out.println("getRaw - csvToArrf error");
		}
		return newArff;

	}

	
	private static File fileParser(File origFile) {
		/* CSV fitxategia prosezatzen du konfliktorik ez egoteko wekak egiten duen interpretazioarekin
		 * in: original csv
		 * out: parsed csv in parent folder
		 * 	  return parsed csv
		 */
		File parsedCSV=null;
		//fitxategia sortu
		try {
			
			File parent = new File(procesedFilesPath);
			if(!parent.isDirectory()) parent.mkdir(); //direktorio ez bada existitzen, horain sortuko du
			String name = origFile.getName().split("\\.")[0]; //fitxeroaren izena lortzen du, parent barruan
			String newFileName = procesedFilesPath+"\\"+name+"Parsed.csv"; //Sortuko dugun CSV berriaren izena definitu
			parsedCSV = new File(newFileName);
			parsedCSV.createNewFile(); //Fisikoki sortu
			
			//filewriter prestatu
			FileWriter writer = new FileWriter(parsedCSV);

			//fitxategia prozezatu
			BufferedReader csvReader = new BufferedReader(new FileReader(origFile));
			String line;
			int n = 0;
				while (true) {
					line = csvReader.readLine();
					if(line==null) break;
				    line = line.replace("'", "`");
				    line = line.replace("?", "\\?");
				    if(n!=0) line = line.replace("label", "\\label");
				    if(n!=0) line = line.replace("id", "\\id");
				    if(n!=0) line = line.replace("text", "\\text");
				    writer.append(line+"\n");
				    writer.flush();
				    n++;
				}
			csvReader.close();
		} catch (Exception e) {
			System.out.println("fileParser - error");
		}
		return parsedCSV;
	}
	
	private static File csvToArrf(File parsedCSV) throws IOException {
		/* CSV fitxategia artu eta arrf-an bihurtu, beharrezkoak ez diren indizeak ezabatu
		 * in: parsed csv
		 * out: arrf file in paren folder
		 * 	return arrf file
		 */
		Instances data = null;
		String csvPath = parsedCSV.getAbsolutePath();
		
//	    loadeeer.setSource(otro);
		// load CSV
		try {
		    CSVLoader loader = new CSVLoader();
		    loader.setSource(parsedCSV);
		    data = loader.getDataSet();
		} catch (Exception e) {
			System.out.println("csvToArrf - loading exception");
		}
		
		try {
			Remove removFilter = new Remove();
			removFilter.setAttributeIndices("1");
			removFilter.setInputFormat(data);
			data = Filter.useFilter(data, removFilter);
		} catch (Exception e) {
			System.out.println("csvToArrf - remove atribute exception");
		}
		

		//arrf filtroa aplikatu nominalToString
		try {
			NominalToString filterString = new NominalToString();
			filterString.setAttributeIndexes("1");
			filterString.setInputFormat(data);
			data = Filter.useFilter(data, filterString);
		} catch (Exception e) {
			System.out.println("csvToArrf - nominalToString filter exception");
		}
		


	    // save ARFF  -  Fitxategia sortu
		File newArrf = new File(csvPath.replace(".csv", ".arff")); //csv-aren izen eta kokapen bera, baina .arrf
		try {
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(newArrf);
		    saver.writeBatch();
	    }catch (Exception e) {
	    	System.out.println("csvToArrf - saver exception");
		}
	    return newArrf;
	}
	
	
}
