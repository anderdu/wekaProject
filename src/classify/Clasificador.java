package classify;

import java.io.File;

import procesing.GetRaw;
import procesing.MakeCompatible;
import procesing.TransformRaw;
import util.AppUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.meta.Bagging;
import weka.core.DictionaryBuilder;
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
		//6_baseline_model
		String paraModelPath = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\6_baseline_model\\trainFullBOWfss.arff";
		String modelo = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\6_baseline_model\\modelo.model";
		String dicc = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\6_baseline_model\\trainFullBOWfss_dict.txt";

		//
		Instances paraModel = AppUtils.file2instances(paraModelPath, "last");
		Clasificador.createModel(paraModelPath,modelo);
		String archivoAnalizar = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\6_baseline_model\\test_unk.csv";
		Clasificador.use(modelo, archivoAnalizar,dicc);
		
	}
	
	
	public static void use(String model,String test,String dicc) throws Exception {
		NaiveBayes classi =  (NaiveBayes) SerializationHelper.read(model);
		File rawFile = GetRaw.getRaw(test);
		//File bowFile = TransformRaw.transformRaw(rawFile, "non", "BOW")[1];
		
		//cambio label de numeric a nominal
		//AppUtils.modifyFile(bowFile,"@attribute label numeric","@attribute label {DESC,ENTY,ABBR,HUM,NUM,LOC}");
		
		//no tengo que aplicar el filtro porque me quedarion cosas distintas
		//tengo que generar diciconario cuando hago el modelo, de manera que puedo hacer todo compatible con ese
		//usar make compatible y a ver que pasa
		//File reducedTest = FeatureSubSel.apply(bowFile, "0");
		File reducedTest = MakeCompatible.makeCompatible(rawFile.getAbsolutePath(), dicc);
		Instances redTest= AppUtils.file2instances(reducedTest.getAbsolutePath(), "0");
		//Instances redTest = AppUtils.file2instances(reducedTest.getAbsolutePath(), "0");
		double actualValue = redTest.instance(0).classValue();
		Instance newInst = redTest.instance(0);
		System.out.println(newInst);
		System.out.println(newInst.classIndex());
		double predSMO = classi.classifyInstance(newInst);
		System.out.println(actualValue+"     "+predSMO);
	}
	
	public static void createModel(String dataPath,String modelo) throws Exception {
		Instances data = AppUtils.file2instances(dataPath, "last");
		NaiveBayes clasificador = new NaiveBayes();
		clasificador.buildClassifier(data);
		SerializationHelper.write(modelo, clasificador);
		String dictName = modelo.replace(".model", "_dic.txt");
		AppUtils.bow2dict(dataPath);
		
	}
	public static void createModel(Instances data,String modelo) throws Exception {

	}
	

}
