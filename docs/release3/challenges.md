# Working with Module Architecture

In our code project, one of the biggest challenges we faced was working within a modular architecture.

### Key Challenges:

- **Dependency Management and POM files**

  Small changes to one POM file led to issues that impacted other modules, especially as dependencies updated or conflicts became apparent between different versions.

- **Integrating modules**

  Integrating small modules into larger once proved to be time consuming and required lots of troubleshooting. Each module had to be compatible with all other modules, which made errors difficult to identify and resolve.

### Approaches we tried:

To address these challenges, we tried several approaches:

- **Pair programming**
  Working in pairs enabled faster issue identification during integration and reduced individual troubleshooting time, providing extra review for dependency issues.

- **Documentation of each module**
  We documented each module’s functionality and dependencies, which helped team members understand how modules work together, simplifying integration.

### Lesson learned:

Through these challenges, we learned several valuable lessons on working in a team:

- **Pair programming (Collaboration)**

  Collaborative approaches, like pair programming were beneficial, allowing us to address integration issues more efficiently and leveraging collective problem-solving skills.

- **Documentation is essential**
  Making documentation eased integration and future troubleshooting, proving essential for clarity.

- **Modular Architecture requires lots of planning**
  Modular architecture demands careful planning and version control, especially as modules grow, to prevent later complications.

# Testing the project

One of the major challenges in this project was ensuring thorough test coverage, especially for all the classes and functionality within the application.

### Key Challenges:

- **Mocking Dependencies:**

  We struggled with implementing mocks, especially for objects and methods that interacted directly with the JavaFX framework. Creating and maintaining these mocks was complex and time consuming.

- **Ui and controller testing:**

  Testing the JavaFX controller layer proved difficult. Standard test setups were often insufficient, because of the complicated behaviors of controllers and UI elements during tests.

### Approaches we tried:

To overcome these testing challenges, we implemented a few key approaches:

- **Using TestFX for JavaFX GUI testing:**

  To manage JavaFX-specific challenges, we used TestFX, which allowed us to simulate user actions in a way that JavaFX could handle. This tool enabled better control over UI elements but required careful setup to avoid test flakiness.

- **Mocking static methods with Mockito:**

  Many parts of our code relied on static methods. We therefor used Mockito to mock static calls, allowing us to simulate various user scenarios without needing real API calls.

### Lessons learned

Through these challenges, we gained a deeper understanding of the importance of testing in complex UI applications:

- **Invest in tooling for UI tests:**

  Investing time in setting up tools like TestFX was essential for testing JavaFX applications effectively.

- **Refactoring for testability:**

  Testing is much easier when controllers are designed with testability in mind. By separating logic from UI and avoiding static dependencies in controllers, we created a more maintainable and testable codebase.

- **Mocking complex interactions:**

  Mocking proved essential in isolating unit tests. Having experience with Mockito and learning to mock complex interactions helped us simulate real application behavior without relying on external systems or dependencies.
