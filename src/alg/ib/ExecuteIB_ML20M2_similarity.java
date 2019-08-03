package alg.ib;

import java.io.File;

import alg.ib.neighbourhood.NearestNeighbourhood;
import alg.ib.neighbourhood.Neighbourhood;
import alg.ib.predictor.DeviationFromItemMeanPredictor;
import alg.ib.predictor.Predictor;
import alg.ib.predictor.SimpleAveragePredictor;
import alg.ib.predictor.WeightedAveragePredictor;
import similarity.metric.CosineMetric;
import similarity.metric.MeanSquaredDifferenceMetric;
import similarity.metric.PearsonMetric;
import similarity.metric.PearsonSigWeightingMetric;
import similarity.metric.SimilarityMetric;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

//EXPT-3 EFFECT OF SIMILARITY METRIC ON PREDICTIONS;
public class ExecuteIB_ML20M2_similarity {
	public static void main(String[] args) {
		// configure the item-based CF algorithm - set the predictor, neighbourhood and
		// similarity metric ...
		Predictor predictor = new DeviationFromItemMeanPredictor();

		SimilarityMetric metric = null;

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
		//In order to keep neighbourhood size to 200
		
		// Initialize the no of similarity metrics to be caluclated
		final int TOTAL_SIMILARITY_METRICS = 4;
		System.out.println("EXPT-3 EFFECT OF SIMILARITY METRIC ON PREDICTIONS;");
		// Iteration for calculating similarity of all the reqd. metrics
		for (int i = 0; i < TOTAL_SIMILARITY_METRICS; i++) {
			Neighbourhood neighbourhood = new NearestNeighbourhood(200);
			// If else to accordingly initialize object for a particular metric
			if (i == 0) {
				System.out.println("***cosine Similarity***");
				metric = new CosineMetric();

			} else if (i == 1) {
				System.out.println("***Pearson Corelation***");
				metric = new PearsonMetric();
			} else if (i == 2) {
				System.out.println("***Pearson Corelation with significance***");
				metric = new PearsonSigWeightingMetric(50);
			} else {
				System.out.println("***Mean Squared Difference***");
				metric = new MeanSquaredDifferenceMetric();
			}

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
