# Setup Mars Robot Project #

  1. Install Oracle Java VM
> > e.g. on Ubuntu Linux check that `/usr/bin/java` point to Oracle Java VM.
```
ls -al /usr/bin/java
java -version
```
  1. Download Java 3D
> > Download the Java 3D API Version **1.4.0\_01** (the latest version of Java 3D API does **NOT** work) from http://java3d.java.net/binary-builds-old.html.
```
curl -o java3d-1_4_0_01-linux-i586.bin http://download.java.net/media/java3d/builds/release/1.4.0_01/java3d-1_4_0_01-linux-i586.bin
```
  1. Install Java 3D API into the JRE folder of Oracle JDK
> > e.g. install Java 3D on Ubuntu Linux, change into the JRE directory and run the Java 3D API install script
```
cd /usr/lib/jvm/java-6-sun/jre
sh /path-to-download/java3d-1_4_0_01-linux-i586.bin
```
  1. Checkout the Mars Robot Project from SCM
```
svn checkout https://svn.codespot.com/a/eclipselabs.org/occ/trunk marsrobot
```
  1. Build the Mars Robot with Maven
```
cd marsrobot
mvn clean install
```
  1. Run the Robot via PAX Runner
```
mvn pax:run
```
  1. Import the Project into Eclipse
```
mvn eclipse:eclipse 
```

## TODO ##
  * adapt doc to use of bndtool
    1. clean projects "org.eclipselabs.occ.simbad, org.eclipselabs.occ.robot, org.eclipselabs.occ.wall"