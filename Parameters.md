# Introduction #

Once you have created a detector by loading an OpenCV XML file that way
```
Detector detector = new Detector("PathtoXMLfile.xml");
```

you may detect objects (such as faces) on an image by calling the detectFaces function, which returns a List`<Rectangle>` containing the rectangles (pixels-based) identified as faces on the image.


# Signature #

The algorithm tests, from sliding windows on the image, of variable size, which regions should be considered as searched objects.
Please see the [original paper](http://research.microsoft.com/en-us/um/people/viola/Pubs/Detect/violaJones_CVPR2001.pdf) for a description of the algorithm.

The complete signature of the detectFaces function is the following :
```
public List<java.awt.Rectangle> getFaces(String file,float baseScale, float scale_inc,float increment, int min_neighbors,boolean doCannyPruning)
```
where the parameters mean the following :
  * _file_ : the image to work on
  * _baseScale_ : The initial ratio between the window size and the Haar classifier size (default 2).
  * _scale\_inc_ The scale increment of the window size, at each step (default 1.25).
  * _increment_ The shift of the window at each sub-step, in terms of percentage of the window size.
  * _min\_neighbors_ : The minimum numbers of similar rectangles needed for the region to be considered as a face (avoid noise)
  * _doCannyPruning_ : enable Canny Pruning to pre-detect regions unlikely to contain faces, in order to speed up the execution.