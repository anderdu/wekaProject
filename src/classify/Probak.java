package classify;

import java.io.File;

public class Probak {
	public static void main(String[] args) {
		try {
			//File parameters = new File(args[2]);
			//ClassifyNaiveBayes.classify(args[0]);
			File a = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainFull.arff");
			ClassifyBagging.parametroEkorketa(a);
		} catch (Exception e) {
			System.out.println("error probak classify");
		}
	}

}
