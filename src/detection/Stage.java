package detection;


import java.util.LinkedList;
import java.util.List;

public class Stage {
List<Feature> features;
float threshold;
	public Stage(float threshold) {
this.threshold=threshold;
features = new LinkedList<Feature>();
	}
	public boolean pass(int[][] grayImage, int[][] squares, int i, int j, float scale) {
		float sum=0;
		for(Feature f : features)
			sum+=f.getVal(grayImage, squares,i, j, scale);
		//System.out.println(sum+" "+threshold);
		return sum>threshold;
	}

}
