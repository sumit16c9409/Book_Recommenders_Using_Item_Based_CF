package alg.ib;

import java.io.File;
import alg.ib.neighbourhood.NearestNeighbourhood;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.DeviationFromItemMeanPredictor;
import alg.ib.predictor.NonPersonalisedPredictor;
import alg.ib.predictor.Predictor;
import alg.ib.predictor.SimpleAveragePredictor;
import alg.ib.predictor.WeightedAveragePredictor;
import similarity.metric.CosineMetric;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

// EXPT-1 EFFECT OF NEIGHBOURHOOD SIZE ON PREDICTIONS

public class ExecuteIB_ML20M2_neighbourhoodsize {
	public static void main(String[] args) {
		// configure the item-based CF algorithm - set the predictor, neighbourhood and
		// similarity metric ...
		Predictor predictor = null;

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

		// Initialize the variable for the total no of predictors for the experiment
		final int TOTAL_PREDICTORS = 4;
		System.out.println("EXPT-1 EFFECT OF NEIGHBOURHOOD SIZE ON PREDICTIONS");
		// Itearte over all predictors
		for (int i = 0; i < TOTAL_PREDICTORS; i++) {
			// if-else for setting the predictor object accordingly
			if (i == 0) {
				System.out.println("***Non Personalized Predictor***");
				predictor = new NonPersonalisedPredictor();

			} else if (i == 1) {
				System.out.println("***Simple Average Predictor***");
				predictor = new SimpleAveragePredictor();

			} else if (i == 2) {
				System.out.println("***Weighted Average Predictor***");
				predictor = new WeightedAveragePredictor();
			} else if (i == 3) {
				System.out.println("***Simple DeviationFromItemMeanPredictor***");
				predictor = new DeviationFromItemMeanPredictor();
			}

			// Initialize max neighbourhood size =250
			final int MAX_NEIGHBOURHOOD_SIZE = 250;

			// Iterate on each neighbourhood size with sprints of 10
			for (int j = 10; j <= MAX_NEIGHBOURHOOD_SIZE; j += 10) {
				System.out.println("\n" + "NeighBouhood Size  = " + j);
				Neighbourhood neighbourhood = new NearestNeighbourhood(j);
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

			}
		}

	}

}
