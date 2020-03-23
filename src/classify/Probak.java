package classify;

import java.io.File;

public class Probak {
	public static void main(String[] args) {
		try {
			File train = new File(args[0]);
			File test = new File(args[1]);
			File parameters = new File(args[2]);
			ClassifyNaiveBayes.classify(train, test, parameters);
		} catch (Exception e) {
			System.out.println("error ClassifyNaiveBayes.classify");
		}

	}

}
