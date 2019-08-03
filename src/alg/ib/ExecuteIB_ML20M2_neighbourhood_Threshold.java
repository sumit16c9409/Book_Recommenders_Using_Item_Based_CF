package alg.ib;

import java.io.File;

import alg.ib.neighbourhood.ThresholdNeighbourhood;
import alg.ib.predictor.DeviationFromItemMeanPredictor;
import alg.ib.predictor.Predictor;
import similarity.metric.CosineMetric;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

//EXP-2 EFFECT OF NEIGHBOURHOOD THRESHOLDS ON PREDICTIONS
public class ExecuteIB_ML20M2_neighbourhood_Threshold {
	public static void main(String[] args) {
		// configure the item-based CF algorithm - set the predictor, neighbourhood and
		// similarity metric ...

		SimilarityMetric metric = new CosineMetric();

		// set the paths and filenames of the item file, genome scores file, train file
		// and test file ...
		String folder = "ml-20m-2018-2019";
		String itemFile = folder + File.separator + "movies-sample.txt";
		String itemGenomeScoresFile = folder + File.separator + "genome-scores-sample.txt";
		String trainFile = folder + File.separator + "train.txt";
		String testFile = folder + File.separator + "test.txt";

		// set the path and filename of the output file ...
		String outputFile = "results" + File.separator + "predictions.txt";

		DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);
		System.out.println("EXP-2 EFFECT OF NEIGHBOURHOOD THRESHOLDS ON PREDICTIONS");
		System.out.println("***DeviationFromItemMeanPredictor***");
		Predictor predictor = new DeviationFromItemMeanPredictor(); // set predictor for DeviationFromItemMeanPredictor

		final double MAX_THRESHOLD = 0.70; // given value of max threshold

		// Iterate over different values of threshold with an increment of 0.05
		for (double j = 0.0; j <= MAX_THRESHOLD;) {
			
			System.out.println("\n" + "Threshold = " + j);
			ThresholdNeighbourhood neighbourhood = new ThresholdNeighbourhood(j);
			ItemBasedCF ibcf = new ItemBasedCF(predictor, neighbourhood, metric, reader);
			Evaluator eval = new Evaluator(ibcf, reader.getTestData());

			// Write to output file
			eval.writeResults(outputFile);

			// Display RMSE and coverage
			Double RMSE = eval.getRMSE();
			if (RMSE != null)
				System.out.printf("RMSE for: %.6f\n", RMSE);

			double coverage = eval.getCoverage();
			System.out.printf("coverage: %.2f%%\n", coverage);

			j += 0.05;
			j = (double) Math.round(j * 100) / 100; // Rounding off to get exact value

		}
	}

}
