package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

public class WeightedAveragePredictor implements Predictor {

	@Override
	public Double getPrediction(Integer userId, Integer itemId, Map<Integer, Profile> userProfileMap,
			Map<Integer, Profile> itemProfileMap, Neighbourhood neighbourhood, SimilarityMap simMap) {
		
		// Initializing of the Numerator and Denominator to 0.0
		double numerator = 0.0;
		double denominator = 0.0;

		// Iterate over each item ids in userprofilemap
		for (Integer id : userProfileMap.get(userId).getIds()) 
		{
			if (neighbourhood.isNeighbour(itemId, id)) // the current item is in the neighbourhood
			{
				Double rating = userProfileMap.get(userId).getValue(id);
				Double similarity = simMap.getSimilarity(itemId, id); //get the similarity between the two items(movies)
				numerator += similarity * rating.doubleValue();

				denominator += Math.abs(similarity);
			}
		}
		//check the zero condition and return null if zero 
		return (denominator > 0.0) ? new Double(numerator / denominator) : null;

	}

}
