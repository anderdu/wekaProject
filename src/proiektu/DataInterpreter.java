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
import weka.filters.unsupervised.attribute.StringToWordVector;

// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\train.csv
public class DataInterpreter {
	public static void main(String[] args) throws Exception {
		DataInterpreter DI = new DataInterpreter();
		String csvPath = args[0];
		csvPath="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\test.csv";
		
		//1. CSV aurreprozezatu erroreak kentzeko
		File parsedCSV = DI.fileParser(csvPath);
		//2. arrf fitxategia sortu
		File newArff = DI.csvToArrf(parsedCSV);
		//3. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File wordArff = DI.toWordVector(newArff);
		
		
	}
	
	public File csvToArrf(File parsedCSV) throws IOException {
		/* CSV fitxategia artu eta arrf-an bihurtu
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
			System.out.println("exception loading");
		}



	    // save ARFF
		File newArrf = new File(csvPath.replace(".csv", ".arff"));
	    try {
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(newArrf);
		    saver.writeBatch();
	    }catch (Exception e) {
	    	System.out.println("saver exception");
		}
	    return newArrf;
	}
	
	public File fileParser(String originalCSV) {
		/* CSV fitxategia prosezatzen du konfliktorik ez egoteko wekak egiten duen interpretazioarekin
		 * in: original csv
		 * out: parsed csv in parent folder
		 * 	return parsed csv
		 */
		File parsedCSV=null;
		try {
			//fitxategia sortu
			File origFile = new File(originalCSV);
			String parent = origFile.getParent();
			String name = origFile.getName().split("\\.")[0];
			String newFileName = parent+"\\"+name+"Parsed.csv";
			parsedCSV = new File(newFileName);
			parsedCSV.createNewFile();
			
			//filewriter prestatu
			FileWriter writer = new FileWriter(parsedCSV);

			//fitxategia prozezatu
			BufferedReader csvReader = new BufferedReader(new FileReader(origFile));
			String line;
				while (true) {
					line = csvReader.readLine();
					if(line==null) break;
//				    System.out.println(Arrays.toString(data));
				    line = line.replace("'", "`");
				    //line = line.replace("?", "_?");
				    writer.append(line+"\n");
				    writer.flush();
				    //pSearchAlgorithm = algorithm.getClass().getName().split("\\.")[3]
				}
			csvReader.close();
		} catch (Exception e) {
			System.out.println("fileparser exception");
		}
		return parsedCSV;
	}
	
	public File toWordVector(File originalArff) throws Exception {
		//arrf-a kargatu
		DataSource source = new DataSource(originalArff.getAbsolutePath());
		Instances data = source.getDataSet();
		
		//filtroa aplikatu
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(data);
		Instances newData = Filter.useFilter(data, filter);
		
		
		return originalArff;
		
	}

}
