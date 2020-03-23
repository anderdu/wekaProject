package procesing;

import java.io.File;

import util.AppUtils;


// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\train.csv
// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\test.csv
public class Probak {
	public static void main(String[] args) throws Exception {
		AppUtils.disableWarning();
		String trainCSV="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\train.csv";
		String testCSV="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\test.csv";
		// 1. raw datuak dituen fitxategia aurreprosezatu konpatibilitate arazoak ekiditzeko
		File trainARFF = GetRaw.getRaw(trainCSV);
		// 2. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File trainBOWarff = TransformRaw.transformRaw(trainARFF,"non","BOW"); //sparse non    BOW  TF-IDF
		//berdina test-ekin
		File testARRF = GetRaw.getRaw(testCSV);
		File testBOWarff = TransformRaw.transformRaw(testARRF,"non","BOW"); //TODO puede ser nonsparse o sparse 
		
		// 3. train eta test konpatibleak egin: atributuak eta posizioak berdindu
		MakeCompatible.makeCompatible(trainBOWarff, testBOWarff);
		System.out.println("END data proccessing test");

		
	}
	


	

}
