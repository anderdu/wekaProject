package classify;

import java.io.File;

public class Probak {
	public static void main(String[] args) {
		try {
			File parameters = new File(args[2]);
			ClassifyNaiveBayes.classify(args[0], args[1], parameters);
		} catch (Exception e) {
			System.out.println("error ClassifyNaiveBayes.classify");
		}
	}

}
