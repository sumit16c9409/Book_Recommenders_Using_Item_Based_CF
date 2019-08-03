package alg.ib.neighbourhood;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import profile.Profile;
import similarity.SimilarityMap;
import util.ScoredThingDsc;

public class ThresholdNeighbourhood extends Neighbourhood {
	private final double thresholdVal;

	// get the threshold value
	public ThresholdNeighbourhood(double k) {
		super();
		this.thresholdVal = k;
	}

	@Override
	public void computeNeighbourhoods(SimilarityMap simMap) {

		// iterate over each item in the similarity map
		for (Integer itemId : simMap.getIds()) {
			Set<ScoredThingDsc> ss = new HashSet<ScoredThingDsc>();

			Profile profile = simMap.getSimilarities(itemId); // get the item similarity profile
			if (profile != null) {
				for (Integer id : profile.getIds()) // iterate over each item in the profile
				{
					double sim = profile.getValue(id);
					if (sim > thresholdVal) // insert the ones whose score is less than the given threshold
						ss.add(new ScoredThingDsc(sim, id));
				}
			}

			// get the k most similar items (neighbours)
			for (Iterator<ScoredThingDsc> iter = ss.iterator(); iter.hasNext();) {
				ScoredThingDsc st = iter.next();
				Integer id = (Integer) st.thing;
				this.add(itemId, id);
			}
		}

	}

}
