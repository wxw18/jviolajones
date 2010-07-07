package detection;


import java.util.ArrayList;
import java.util.List;

public class Feature {

	Rect[] rects;
	int nb_rects;
	float threshold;
	float left_val;
	float right_val;
	public Feature(	float threshold,float left_val,	float right_val) {
		nb_rects = 0;
		rects=new Rect[3];
		this.threshold=threshold;
		this.left_val=left_val;
		this.right_val=right_val;
	}

	public float getVal(int[][] grayImage, int[][] squares, int i, int j, float scale) {
		int w=(int) (scale*24);
		double w2=1./(w*w);
		//System.out.println("w2 : "+w2);
		int total_x=grayImage[i+w][j+w]+grayImage[i][j]-grayImage[i][j+w]-grayImage[i+w][j];
		int total_x2=squares[i+w][j+w]+squares[i][j]-squares[i][j+w]-squares[i+w][j];
		double moy=total_x*w2;
		double vnorm=total_x2*w2-moy*moy;
		vnorm=(vnorm>1)?Math.sqrt(vnorm):1;

				int rect_sum=0;
				for(int k=0;k<nb_rects;k++)
				{
					Rect r = rects[k];
					int rx1=i+(int) (scale*r.x1);
					int rx2=i+(int) (scale*(r.x1+r.y1));
					int ry1=j+(int) (scale*r.x2);
					int ry2=j+(int) (scale*(r.x2+r.y2));
					//System.out.println((rx2-rx1)*(ry2-ry1)+" "+r.weight);
					rect_sum+=(int)((grayImage[rx2][ry2]-grayImage[rx1][ry2]-grayImage[rx2][ry1]+grayImage[rx1][ry1])*r.weight);
				}
				//System.out.println(rect_sum);
				double rect_sum2=rect_sum*w2;

		return (rect_sum2<threshold*vnorm?left_val:right_val);
	}

	public void add(Rect r) {
		rects[nb_rects++]=r;
	}
}
