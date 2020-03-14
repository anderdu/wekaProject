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
import weka.filters.unsupervised.attribute.StringToWordVector;

// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\train.csv
public class DataInterpreter {
	private String procesedFilesPath = null; //Sortutzako fitxategiak hemen gordeko dira, parent forlder barruan
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
	
	public File fileParser(String originalCSV) {
		/* CSV fitxategia prosezatzen du konfliktorik ez egoteko wekak egiten duen interpretazioarekin
		 * in: original csv
		 * out: parsed csv in parent folder
		 * 	  return parsed csv
		 */
		File parsedCSV=null;
		//fitxategia sortu
		try {
			File origFile = new File(originalCSV);
			this.procesedFilesPath = origFile.getParent()+"\\procesedFiles";
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
				while (true) {
					line = csvReader.readLine();
					if(line==null) break;
				    line = line.replace("'", "`");
				    //line = line.replace("?", "_?");
				    writer.append(line+"\n");
				    writer.flush();
				}
			csvReader.close();
		} catch (Exception e) {
			System.out.println("fileParser - error");
		}
		return parsedCSV;
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
			System.out.println("csvToArrf- exception loading");
		}

		//arrf filtroa aplikatu nominalToString
		try {
			NominalToString filterString = new NominalToString();
			filterString.setAttributeIndexes("2");
			filterString.setInputFormat(data);
			data = Filter.useFilter(data, filterString);
		} catch (Exception e) {
			// TODO: handle exception
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
	
	public File toWordVector(File originalArff) {
		/* Lehen sortutako arrf fitxategiaren textuak hitzetan banatzen du
		 * in:  originalArff
		 * out: words.arff in parent folder
		 * 	  return wordsArff
		 */
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
