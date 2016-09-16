package cass.wsd;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

import cass.languageTool.Language;
import cass.languageTool.LanguageTool;
import cass.testGenerator.TestData;
import cass.testGenerator.TestSentenceGenerator;

public class WSDBenchmark {
	
	public double benchmark(Algorithm algorithm) {
		int n = 0;
		double meanScore = 0;
		
		Iterator<TestData> tsg = new TestSentenceGenerator("semcor3.0");
		
		while(tsg.hasNext()) {
			n++;
			TestData ts = tsg.next();
			WSD wsd = new WSD(ts.getLeftContext(), ts.getTarget(), ts.getRightContext(), Language.EN);				
			LanguageTool lt = new LanguageTool(Language.EN);
			
			if (!lt.getSenses(ts.getTarget()).isEmpty()) {				
				List<ScoredSense> results = wsd.scoreSensesUsing(algorithm);
				
				int numCorrectAnswers = ts.getSenses().size();
				double bestScore = IntStream.rangeClosed(1, numCorrectAnswers).mapToDouble(x -> 1/x).sum();
				// best score is based on harmonic series
						
				double score = IntStream.range(0, results.size())
					.mapToObj(i -> new Pair<Integer, String>(i, results.get(i).getSense().getId()))
					.filter(pair -> ts.getSenses().contains(pair.s))
					.mapToDouble(pair -> 1/(pair.t + 1))
					.sum() / bestScore;
				
				meanScore = (meanScore * n + score) / (n+1);
				System.out.println(meanScore);
				
				n++;
			}
		}
		
		return meanScore;
	}
	
	class Pair<T,S> {
		public T t;
		public S s;
		
		public Pair(T t, S s) {
			this.t = t;
			this.s = s;
		}
	}
}
