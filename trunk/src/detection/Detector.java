package detection;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.filter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import javax.imageio.ImageIO;


public class Detector {
	static org.jdom.Document document;
	   static Element racine;
List<Stage> stages;
	public Detector(String filename) {
		stages=new LinkedList<Stage>();
		SAXBuilder sxb = new SAXBuilder();
	      try
	      {
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	         document = sxb.build(new File(filename));
	      }
	      catch(Exception e){e.printStackTrace();}

	      //On initialise un nouvel élément racine avec l'élément racine du document.
	      racine = (Element) document.getRootElement().getChildren().get(0);
	      Iterator it=((Element) racine.getChildren("stages").get(0)).getChildren("_").iterator();
	      while(it.hasNext())
	      {
	    	  Element stage=(Element)it.next();
	    	  float thres=Float.parseFloat(stage.getChild("stage_threshold").getText());
	    	  System.out.println(thres);
	    	  Iterator it2=stage.getChild("trees").getChildren("_").iterator();
	    	  Stage st=new Stage(thres);
	    	 while(it2.hasNext())
	    	 {
	    		 Element feature=((Element)it2.next()).getChild("_");
	    		 float thres2=Float.parseFloat(feature.getChild("threshold").getText());
	    		 float left_val=Float.parseFloat(feature.getChild("left_val").getText());
	    		 float right_val=Float.parseFloat(feature.getChild("right_val").getText());
	    		 Feature f = new Feature(thres2,left_val,right_val);
	    		 Iterator it3=feature.getChild("feature").getChild("rects").getChildren("_").iterator();
	    		 while(it3.hasNext())
	    		 {
	    			 Rect r = Rect.fromString(((Element) it3.next()).getText());
	    			 f.rects.add(r);
	    		 }
	    		 st.features.add(f);
	    		 }
	    	 stages.add(st);
	    	 }
	      System.out.println(stages.size());
	      }

	public List<java.awt.Rectangle> getFaces(String file,float baseScale, float scale_inc,float increment)
	{
		try {
			List<Rectangle> ret=new ArrayList<Rectangle>();
			BufferedImage image = ImageIO.read(new File(file));
			int width=image.getWidth();
			int height=image.getHeight();
			int max=Math.max(width,height);
			int[][] grayImage=new int[width][height];
			int[][] squares=new int[width][height];
			for(int i=0;i<width;i++)
			{
				int col=0;
				int col2=0;
				for(int j=0;j<height;j++)
				{
					int c = image.getRGB(i,j);
					int  red = (c & 0x00ff0000) >> 16;
					int  green = (c & 0x0000ff00) >> 8;
					int  blue = c & 0x000000ff;
					int value=(int) (0.3*red + 0.59*green + 0.11*blue);
					grayImage[i][j]=(i>0?grayImage[i-1][j]:0)+col+value;
					squares[i][j]=(i>0?squares[i-1][j]:0)+col2+value*value;
					col+=value;
					col2+=value*value;
				}
			}
			for(int size=(int) (baseScale*24);size<max;size=(int) (size*scale_inc))
			{
				int step=(int) (increment*size);
				for(int i=0;i<width-size;i+=step)
				{
					for(int j=0;j<height-size;j+=step)
					{
						//System.out.println(size+" ["+i+","+j+"]");
						boolean pass=true;
						int k=0;
						for(Stage s:stages)
						{
							
							if(!s.pass(grayImage,squares,i,j,size))
								{pass=false;
								//System.out.println(k);
								break;}
							k++;
						}
						if(pass) ret.add(new Rectangle(i,j,size,size));
					}
				}
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<java.awt.Rectangle> merge(List<java.awt.Rectangle> rects)
	{
		 List<java.awt.Rectangle> retour=new  LinkedList<java.awt.Rectangle>();
		int[] ret=new int[rects.size()];
		int nb_classes=0;
		for(int i=0;i<rects.size();i++)
		{
			boolean found=false;
			for(int j=0;j<i;j++)
			{
				if(equals(rects.get(j),rects.get(i)))
				{
					found=true;
					ret[i]=ret[j];
				}
			}
			if(!found)
			{
				ret[i]=nb_classes;
				nb_classes++;
			}
		}
		System.out.println(Arrays.toString(ret));
		int[] neighbors=new int[nb_classes];
		Rectangle[] rect=new Rectangle[nb_classes];
		for(int i=0;i<nb_classes;i++)
		{
			neighbors[i]=0;
			rect[i]=new Rectangle(0,0,0,0);
		}
		for(int i=0;i<rects.size();i++)
		{
			neighbors[ret[i]]++;
			rect[ret[i]].x+=rects.get(i).x;
			rect[ret[i]].y+=rects.get(i).y;
			rect[ret[i]].height+=rects.get(i).height;
			rect[ret[i]].width+=rects.get(i).width;
		}
		for(int i = 0; i < nb_classes; i++ )
        {
            int n = neighbors[i];
            if( n >= 3 )
            {
            	Rectangle r=new Rectangle(0,0,0,0);
                r.x = (rect[i].x*2 + n)/(2*n);
                r.y = (rect[i].y*2 + n)/(2*n);
                r.width = (rect[i].width*2 + n)/(2*n);
                r.height = (rect[i].height*2 + n)/(2*n);
                retour.add(r);
            }
        }
		return retour;
	}
	
	public boolean equals(Rectangle r1, Rectangle r2)
	{
		int distance = (int)(r1.width*0.2);

	    /*return r2.x <= r1.x + distance &&
	           r2.x >= r1.x - distance &&
	           r2.y <= r1.y + distance &&
	           r2.y >= r1.y - distance &&
	           r2.width <= (int)( r1.width * 1.2 ) &&
	           (int)( r2.width * 1.2 ) >= r1.width;*/
		if(r2.x <= r1.x + distance &&
	           r2.x >= r1.x - distance &&
	           r2.y <= r1.y + distance &&
	           r2.y >= r1.y - distance &&
	           r2.width <= (int)( r1.width * 1.2 ) &&
	           (int)( r2.width * 1.2 ) >= r1.width) return true;
		if(r1.x>=r2.x&&r1.x+r1.width<=r2.x+r2.width&&r1.y>=r2.y&&r1.y+r1.height<=r2.y+r2.height) return true;
		return false;
	}
}
