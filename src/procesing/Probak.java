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
		//String trainCSV = "C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\probak\\6_baseline_model\\test_unk.csv";
		File devARRF;
		// 1. raw datuak dituen fitxategia aurreprosezatu konpatibilitate arazoak ekiditzeko
		File trainARFF = GetRaw.getRaw(trainCSV);
		// 1.2 traini splip egin eta train eta dev-en banandu
		//File[] splited = DataSplit.split(trainARFF, 30); //f0->train f1->test
		//trainARFF = splited[0];
		//devARRF = splited[1];
		// 2. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File[] DiccANDtrainBOW = TransformRaw.transformRaw(trainARFF,"non","BOW"); //sparse non    BOW  TF-IDF
		
		//File reducedTest = FeatureSubSel.apply(DiccANDtrainBOW[1], "0");
		//berdina test-ekin
		
		//MakeCompatible.makeCompatible(devARRF, DiccANDtrainBOW[0]);
		
		System.out.println("END data proccessing test");

		
	}
	


	

}
