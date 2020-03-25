package procesing;

import java.io.File;

import util.AppUtils;


// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\train.csv
// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\test.csv
public class Probak {
	public static void main(String[] args) throws Exception {
		AppUtils.disableWarning();
		String trainCSV="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\train.csv";
		File devARRF;
		// 1. raw datuak dituen fitxategia aurreprosezatu konpatibilitate arazoak ekiditzeko
		File trainARFF = GetRaw.getRaw(trainCSV);
		// 1.2 traini splip egin eta train eta dev-en banandu
		File[] splited = DataSplit.split(trainARFF, 30); //f0->train f1->test
		trainARFF = splited[0];
		devARRF = splited[1];
		// 2. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File[] DiccANDtrainBOW = TransformRaw.transformRaw(trainARFF,"non","BOW"); //sparse non    BOW  TF-IDF
		//berdina test-ekin
		
		MakeCompatible.makeCompatible(DiccANDtrainBOW[1], devARRF, DiccANDtrainBOW[0]);
		
		System.out.println("END data proccessing test");

		
	}
	


	

}
