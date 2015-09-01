# Description #
This package is a Java implementation of the Viola-Jones algorithm, able to load OpenCV XML files.

It can be used to detect either faces or any objects you have learned a cascade classifier from.

However, this project does not implement the learning part of the Viola-Jones algorithm (you still have to learn examples with OpenCV tools).

Features :
- Optional Canny pruning, as in OpenCV.

## Update ##
A multi-threaded version of the detector is available in source code! I've been able to get a 3x speed-up factor on my dual core ! Many optimizations are still to come...

Thanks to Gary Frost, member of the Aparapi team, who made Jviolajones run on GPU (http://aparapi.googlecode.com/) for his code and valuable advice.

# Use #
The runnable jar available in the Downloads section should be called this way :
```
java -jar jviolajones2.jar imageFileName OpenCVXmlFile
```
_(Some cascade files, as well as a test image, can be downloaded from the Downloads section.)_

If you want to integrate this package in your code, this should work :
```
import detection.Detector;

String fileName="yourfile.jpg";
Detector detector=Detector.create("haarcascade_frontalface_default.xml");
List<Rectangle> res=detector.getFaces(fileName, 1.2f,1.1f,.05f, 2,true);
```
These parameters have been proposed by an user, who got good results using them.
If you don't detect enough faces (or too much), please adjust the [Parameters](Parameters.md).