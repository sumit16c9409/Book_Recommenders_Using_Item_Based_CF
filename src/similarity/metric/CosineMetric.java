package similarity.metric;

import profile.Profile;

public class CosineMetric implements SimilarityMetric {

	@Override
	public double getSimilarity(Profile p1, Profile p2) {
		// TODO Auto-generated method stub
		// initialize the dotProduct sum to 0
		double dotProduct = 0.0;

		// itearate on the common ids for x and y using framework method getCommonIds
		for (Integer tagid : p1.getCommonIds(p2)) {
			dotProduct += (p1.getValue(tagid) * p2.getValue(tagid));
		}
		// calculated the cosine of angle between two items and computed their cosine
		double denominator = p1.getNorm() * p2.getNorm();

		return denominator > 0.0 ? dotProduct / denominator : 0.0;
	}

}
