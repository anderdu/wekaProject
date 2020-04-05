package procesing;

import java.io.File;

import classify.FeatureSubSel;
import util.AppUtils;


// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\train.csv
// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\test.csv
public class Probak {
	public static void main(String[] args) throws Exception {
		AppUtils.disableWarning();
		String trainCSV="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\train.csv";
		//String trainCSV=args[0];
		//String trainCSV = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\6_baseline_model\\test_unk.csv";
		// 1. raw datuak dituen fitxategia aurreprosezatu konpatibilitate arazoak ekiditzeko
		File trainARFF = GetRaw.getRaw(trainCSV);
		// 1.2 traini splip egin eta train eta dev-en banandu
		//File[] splited = DataSplit.split(trainARFF, 30); //f0->train f1->test
		//trainARFF = splited[0];
		//devARRF = splited[1];
		// 2. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File[] DiccANDtrainBOW = TransformRaw.transformRaw(trainARFF,"non","BOW"); //sparse non    BOW  TF-IDF
		//Integer num = Integer.parseInt(args[1]);
		//3. lortu dugun fitxategiaren 
		File reducedTest = FeatureSubSel.apply(DiccANDtrainBOW[1], "0",2000);
		//berdina test-ekin
		trainARFF.delete();
		DiccANDtrainBOW[0].delete();
		DiccANDtrainBOW[1].delete();
		
		//MakeCompatible.makeCompatible(devARRF, DiccANDtrainBOW[0]);
		
		System.out.println("END data proccessing test");

		
	}
	


	

}
