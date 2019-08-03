package similarity.metric;

import java.util.Set;

import profile.Profile;

public class MeanSquaredDifferenceMetric implements SimilarityMetric {

	@Override
	public double getSimilarity(Profile p1, Profile p2) {
		// initialize the numerator to 0.0
		double numerator = 0.0;
		double rMax = 5.0;
		double rMin = 0.5;
		// itearate on the common ids for x and y using framework method getCommonIds
		Set<Integer> commonIds = p1.getCommonIds(p2);
		if (commonIds.isEmpty()) { // return if the common ids are empty (i.e size =0 , to avoid further compute)
			return 0.0;
		}
		for (Integer tagid : commonIds) {
			numerator += Math.pow(p1.getValue(tagid) - p2.getValue(tagid), 2);
		}

		double meanSquaredDifference = numerator / commonIds.size();
		double similarity = 1 - (meanSquaredDifference / Math.pow(rMax - rMin, 2));
		return similarity;
	}

}
