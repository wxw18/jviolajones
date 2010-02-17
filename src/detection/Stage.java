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
	public boolean pass(int[][] grayImage, int[][] squares, int i, int j, int size) {
		float scale=size/24;
		float sum=0;
		for(Feature f : features)
			sum+=f.getVal(grayImage, squares,i, j, scale);
		//System.out.println(sum+" "+threshold);
		return sum>threshold;
	}

}
