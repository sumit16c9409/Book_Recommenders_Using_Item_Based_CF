package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

public class DeviationFromItemMeanPredictor implements Predictor {

	@Override
	public Double getPrediction(Integer userId, Integer itemId, Map<Integer, Profile> userProfileMap,
			Map<Integer, Profile> itemProfileMap, Neighbourhood neighbourhood, SimilarityMap simMap) {

		// Initializing of the Numerator and Denominator to 0.0
		double numerator = 0.0;
		double denominator = 0.0;

		// Iterate over each item ids in userprofilemap
		for (Integer id : userProfileMap.get(userId).getIds()) {
			// check whether the current item lies in the neighbourhood
			if (neighbourhood.isNeighbour(itemId, id)) {
				//
				Double rating = userProfileMap.get(userId).getValue(id);
				Double itemSimilarity = simMap.getSimilarity(itemId, id); // similarity between two items using
																			// similarity map(movies)

				// Numerator is calculated as per the below formulae
				numerator += itemSimilarity.doubleValue()
						* (rating.doubleValue() - itemProfileMap.get(id).getMeanValue());

				denominator += Math.abs(itemSimilarity); // to get the absoulute value of itemSimilarity
			}
		}

		// As per the formulae get the mean value for all items
		double itemMean = itemProfileMap.get(itemId).getMeanValue();
		// Handled the check for zero
		return denominator > 0 ? itemMean + (numerator / denominator) : null;

	}

}
