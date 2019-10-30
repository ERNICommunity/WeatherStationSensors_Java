Weather Station Sensors Task
============================

## How to set up a development environment 

### Prerequisites

* a Java JDK version 8 or higher. If you don't yet have a JDK installed, we recommend installing [OpenJDK 8 from AdoptOpenJDK](https://adoptopenjdk.net/).
* an IDE of your choice with Maven support. [Eclipse](#eclipse) or [IntelliJ IDEA](#intellij-idea) will both do.

### Eclipse

If you don't have Eclipse installed yet we recommend to download the [Eclipse Installer](https://www.eclipse.org/downloads/packages/installer) and install the `Eclipse IDE for Java Developer` package.

1. Launch Eclipse
2. Click `File` > `Import` from the menu
3. In the `Import` window, click `Projects from Git (with smart import)` and click `Next`.
4. In the `Select Repository Source` window, click `Clone URI`.
5. In the `Import Projects from Git` window, enter `https://github.com/ERNICommunity/WeatherStationSensors_Java.git` as URI and click `Next`.
6. Step through the wizard and click `Finish` to Import the project.
7. Find the WeatherStationSensorsTest class in the `Package Explorer` view and execute the unit tests. There should be 4 passing tests and 7 failing tests.

### IntelliJ IDEA

Any version of IntelliJ IDEA is supported. The free community edition is sufficient.
It can be downloaded [here](https://www.jetbrains.com/idea/download/).

1. Launch IntelliJ IDEA. If there is already an open project, close it with `File` > `Close Project` in the menu.
2. Select `Check out from Version controll` from the welcome screen.
3. Enter `https://github.com/ERNICommunity/WeatherStationSensors_Java.git` as repository URL
4. When asked if you like to open an IntelliJ IDEA project file, click `Yes`.
5. Run unit tests in the WeatherStationSensorsTest class. There should be 4 passing tests and 7 failing tests.
