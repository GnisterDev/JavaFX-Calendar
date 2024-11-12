# Group gr2404 repository

[Open in Eclipse Che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2024/gr2404/gr2404)

## Description

This project is a simple calender. The project is containd within `./calendar`. For more information see the [README.md](./calendar/readme.md) file inside the `./calendar` folder

## Building and running the project

The project uses maven to build and run.

To build the project move into the root of the application with `cd ./calendar` folder, and run the command `mvn clean install -DskipTests`. If you want to run the tests the project to see if everything is working correctly, run the command `mvn test`. You should see an overview like the one here:
![alt text](./docs/images/success.png) If anytrhing says <span style="color:red;font-weight:bold">FAILURE</span> or <span style="color:gold;font-weight:bold">SKIPPED</span>, something went wrong.

<hr>

This project has both a frontend, and a backend using restAPI.

To deploy the backend; type the command `cd ./calender` if you aren't there already and type the command `mvn exec:java`. Wait a few seconds for the program to start up. A restAPI server should now be running.

Now open a new terminal window and cd back into the calendar folder using `cd ./calendar`. Now you can run `mvn javafx:run`. After a few seconds a window should appear on your screen. You can now explore the application freely.

## Technical Information

Java version `21.0.1`

Maven version `3.8.0`

## Link to docs

-   [Release 1](./docs/release1/readme.md)
-   [Release 2](./docs/release2/readme.md)
-   [Release 3](./docs/release3/readme.md)
