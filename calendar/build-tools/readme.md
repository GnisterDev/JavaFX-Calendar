# Build tools module

This module is used to aggregate results of build tools ran on the individual calendar modules. Currently it is only used to generate an aggregated jacoco test code coverage report. The jacoco report can be found at [./target/jacoco/index.html](./target/jacoco/index.html). Note that to generate the report you must first run `mvn test` in the calendar directory.
