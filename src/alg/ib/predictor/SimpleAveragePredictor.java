/**
 * An class to compute the target user's predicted rating for the target item (item-based CF) using the simple average technique.
 * 
 * Michael O'Mahony
 * 20/01/2011
 */

package alg.ib.predictor;

import java.util.Map;

import alg.ib.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

public class SimpleAveragePredictor implements Predictor {
	/**
	 * constructor - creates a new SimpleAveragePredictor object
	 */
	public SimpleAveragePredictor() {
	}

	/**
	 * @returns the target user's predicted rating for the target item or null if a
	 *          prediction cannot be computed
	 * @param userId
	 *            - the numeric ID of the target user
	 * @param itemId
	 *            - the numerid ID of the target item
	 * @param userProfileMap
	 *            - a map containing user profiles
	 * @param itemProfileMap
	 *            - a map containing item profiles
	 * @param neighbourhood
	 *            - a Neighbourhood object
	 * @param simMap
	 *            - a SimilarityMap object containing item-item similarities
	 */
	public Double getPrediction(final Integer userId, final Integer itemId, final Map<Integer, Profile> userProfileMap,
			final Map<Integer, Profile> itemProfileMap, final Neighbourhood neighbourhood, final SimilarityMap simMap) {
		// Initialized counter and Numerator
		double numerator = 0;
		int counter = 0;

		// Iterate over each item ids in userprofilemap
		for (Integer id : userProfileMap.get(userId).getIds()) {
			// check whether the current item lies in the neighbourhood
			if (neighbourhood.isNeighbour(itemId, id)) {
				Double rating = userProfileMap.get(userId).getValue(id);
				numerator += rating.doubleValue();
				counter++;
			}
		}

		//check the zero condition and return null if zero
		return (counter > 0) ? new Double(numerator / counter) : null;
	}
}
