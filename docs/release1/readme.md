# Release 1

This is the first release of the project. It mostly consisted of generic project setup and planning.

### Project setup

We used most of our time setting up the gitlab project. As it is now, `mvn javafx:run` runs a generic javafx window that we will expand upon in further releases. We added some plugins into the pom.xml file to get test coverage reports and clean up `mvn test` output.

#### Test coverage

For test coverage we used jacoco. It is configured to run automatically when running `mvn test` and if all the tests pass it generates a code coverage report in [target/site/jacoco/index.html](../../calendar/target/site/jacoco/index.html).

#### Cleaner test output

We also added a junit5 plugin that changes how test outputs are displayed in the terminal. It gives a tree view of all tests instead of the default list view. This will be very nice for working on new features in the future.

### The app we chose

We decided to create a calendar app. It should enable multiple users to manage their weekly events. A full description of the app and user stories associated with it can be found in the [javafx project readme.md file](../../calendar/readme.md).

### Issues we encounterd
We had some problems with gitlab issues as we started setting up the project without creating issues for it. We have since added issues for the things that were missing, but as a result they are not properly linked to the commits that address them. After talking to the student assistent, who made us aware of this, we have strived to properly define issues and make branches and merge requests that solve and close them. We will also, of course, strive to do this in the future.

### AI statement
We have not used AI for this release, but we are planning to use it as a helping tool later on in this project. 
