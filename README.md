ILITE Robotics FRC ui repository
===========================
>Before using please make sure
>
> - you have the latest [Java 8 JRE/JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
> - you have JavaFX installed in eclipse in the form of [e(fx)clipse](http://www.eclipse.org/efxclipse/index.html)
> - you have pulled the latest version of this repository from the current working branch

##Code Structure

**org/css**
 - files containing the css styling for each widget and module in the GUI
 - each widget has it's own .css file
**org/widget**
 - widgets for displaying various data or for user interaction
 - each widget directory has it's own *skins* sub-directory for storing skinning files for that widget
 - widgets are complete and modular, they should have the capability to be used wherever any other node can
**org/test**
 - files for test runs and debugging