package detection;





import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test extends JFrame{
	
	public Test(File img)
	{
		Image image=null;
		try {
			image = ImageIO.read(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dessin d = new Dessin(image);
		Detector detector=new Detector("haarcascade_frontalface_default.xml");
		List<Rectangle> res=detector.getFaces(img.getAbsolutePath(), 2, 1.25f, 0.1f,3,true);
        d.setRects(res);
		setContentPane(d);
		this.setExtendedState(MAXIMIZED_BOTH);
	}
public static void main(String[] args)
{
	new Test(new File(args[0])).setVisible(true);
	
		
}

/*public static void speedTest()
{
Detector detector=new Detector("haarcascade_frontalface_default.xml");
	
	final JFileChooser fc = new JFileChooser();
	fc.setDialogTitle("Veuillez choisir le dossier contenant vos photos");
	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int returnVal;
	File path;
    while((returnVal =fc.showOpenDialog(null))!=JFileChooser.APPROVE_OPTION) ;
    path=fc.getSelectedFile();
    FilenameFilter fn=new FilenameFilter(){
		@Override
		public boolean accept(File dir, String name) {
			//System.out.println(name);
			String[] ext = {"jpg","bmp","gif","png"};
			for(String s:ext)
			{
				if(name.toLowerCase().endsWith(s)) return true;
			}
			return false;
		}};
		File[] images = path.listFiles(fn);
		System.out.println("NB IMAGES : "+images.length);
		float cpuUserAvant = CpuMonitorWindows.getUserTime();
		float cpuKernelAvant = CpuMonitorWindows.getKernelTime();
		for(File f: images)
		{
			List<Rectangle> res=detector.getFaces(f.getAbsolutePath(), 2, 1.25f, 0.1f,3,true);
		}
		float cpuUserApres = CpuMonitorWindows.getUserTime();
		float cpuKernelApres = CpuMonitorWindows.getKernelTime();
		for(File f: images)
		{
			List<Rectangle> res=detector.getFaces(f.getAbsolutePath(), 2, 1.25f, 0.1f,3,false);
		}
		float cpuUserApres2 = CpuMonitorWindows.getUserTime();
		float cpuKernelApres2 = CpuMonitorWindows.getKernelTime();
		System.out.println("AVEC CANNY : ");
		System.out.println("CPU : "+(cpuUserApres-cpuUserAvant));
		System.out.println("Kernel : "+(cpuKernelApres-cpuKernelAvant));
		System.out.println("SANS CANNY : ");
		System.out.println("CPU : "+(cpuUserApres2-cpuUserApres));
		System.out.println("Kernel : "+(cpuKernelApres2-cpuKernelApres));
}*/

}

class Dessin extends JPanel
{
	protected Image img;
	int img_width,img_height;
	List<Rectangle> res;
	
	public Dessin(Image img) {
		super();
		this.img=img;
		img_width=img.getWidth(null);
		img_height=img.getHeight(null);
		res=null;
		
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g1 = (Graphics2D) g;
		g1.setColor(Color.red);
		g1.setStroke(new BasicStroke(2f));
		if(img==null)
			return;
		Dimension dim=getSize();
		//System.out.println("véridique");
		g1.clearRect(0,0, dim.width, dim.height);
		double scale_x=dim.width*1.f/img_width;
		double scale_y=dim.height*1.f/img_height;
		double scale=Math.min(scale_x, scale_y);
		int x_img=(dim.width-(int)(img_width*scale))/2;
		int y_img=(dim.height-(int)(img_height*scale))/2;
		g1.drawImage(img,x_img,y_img,(int)(img_width*scale),(int)(img_height*scale), null);
		if(res==null) return;
		
		for(Rectangle rect : res)
		{
		int w=(int) (rect.width*scale);
		int h=(int) (rect.height*scale);
		int x=(int) (rect.x*scale)+x_img;
		int y=(int) (rect.y*scale)+y_img;
		g1.drawRect(x,y,w,h);
		}
	}

	public void setRects(List<Rectangle> list) {
		this.res=list;
		repaint();
	}
	
}