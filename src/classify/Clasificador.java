package classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import procesing.GetRaw;
import procesing.MakeCompatible;
import util.AppUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class Clasificador {
	public static void main(String[] args) throws Exception {
		/*
		 * es para hacer el ultmo apartado del ejercicio, pilar unb csv y clasificarlo
		 * 
		 */
		AppUtils.disableWarning();
		//createModel(args[0],args[1]);//para primer jar --> create model
		//use(args[0],args[1],args[2]);
		//check(args[0],args[1]);
		
		//6_baseline_model
		String paraModelPath = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\modelo\\4\\trainFullBOWfss.arff";
		String modelo = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\modelo\\4\\modelo.model";
		String test = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\modelo\\4\\test.csv";
		String predictions = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\modelo\\4\\predictions.txt";
		String dicc = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\modelo\\4\\trainFullBOWfss_dict.txt";

		//
		Instances paraModel = AppUtils.file2instances(paraModelPath, "last");
		//Clasificador.createModel(paraModelPath,modelo);
		String archivoAnalizar = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\modelo\\2\\test_unk.csv";
		//Clasificador.use(modelo, archivoAnalizar,dicc);
		
		check(test,predictions);
		
	}
	
	
	public static void use(String model,String test,String dicc) throws Exception {
		Bagging classi =  (Bagging) SerializationHelper.read(model);
		File rawFile = GetRaw.getRaw(test);
		
		File reducedTest = MakeCompatible.makeCompatible(rawFile.getAbsolutePath(), dicc);
		AppUtils.modifyFile(reducedTest,"@attribute label numeric","@attribute label {DESC,ENTY,ABBR,HUM,NUM,LOC}");

		Instances redTest= AppUtils.file2instances(reducedTest.getAbsolutePath(), "0");
		redTest.setClassIndex(redTest.numAttributes()-1);
		PrintStream write = new PrintStream(reducedTest.getParent()+File.separatorChar+"predictions.txt");
		for (Instance ins : redTest) {
			double predSMO = classi.classifyInstance(ins);
			//System.out.println(predSMO);
			
			System.out.println(redTest.attribute(redTest.numAttributes()-1).value((int)predSMO));
			write.println(redTest.attribute(redTest.numAttributes()-1).value((int)predSMO));
		}
		write.close();
	}
	
	public static void createModel(String dataPath,String modelo) throws Exception {
		Instances data = AppUtils.file2instances(dataPath, "last");
		Bagging clasificador = new Bagging();
		clasificador.setBagSizePercent(50);
		clasificador.buildClassifier(data);
		SerializationHelper.write(modelo, clasificador);
		String dictName = modelo.replace(".model", "_dic.txt");
		AppUtils.bow2dict(dataPath);
	}
	public static void createModel(Instances data,String modelo) throws Exception {

	}
	
	public static void check(String csvPath,String predictionsPath) throws IOException {
		BufferedReader csvReader = new BufferedReader(new FileReader(csvPath));
		BufferedReader predReader = new BufferedReader(new FileReader(predictionsPath));
		File file = new File(csvPath);
		
		String csvLine = csvReader.readLine();
		String predVal = "";
		PrintStream write = new PrintStream(file.getParent()+File.separatorChar+"comparation.txt");
		int correct = 0;
		int valCounter = 0;
		
		write.println("predVal \t realVal");
		while (true) {
			valCounter++;
			csvLine = csvReader.readLine();
			predVal = predReader.readLine();
			if(csvLine==null) break;
			String realVal = csvLine.split(",")[2];
			if(predVal.equals(realVal)) correct++;
			if(predVal.length()<4) write.println(predVal+"\t\t\t"+realVal);
			else write.println(predVal+"\t\t"+realVal);
			
		}
		int incorrect = valCounter-correct;
		int pct = correct*100/valCounter;
		write.println("correct:  "+correct);
		write.println("incorrect:  "+incorrect);
		write.println("pct correct:  "+pct);
		csvReader.close();
		predReader.close();
		write.close();
	}
	

}
