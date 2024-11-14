# Working with Module Architecture

In our code project, one of our biggest challenges we faced was working within a modular architecture.

### Key Challenges:

- **Dependency Manegment and POM files**

  Small changes to one POM file lead to issues that impacted other modules, especially as dependencies updated or conflicts became apparent between different versions.

- **Integrating modules**

  Integrating small modules into larger once proved to be time consuming and required lots of troubleshooting. Each module had to be compatible with all other modules, which made errors difficult to identify and resolve.

### Approchehes we tried:

To address these challenges we tried several approaches:

- **Pair programming**

  By working in pairs, team members were able to intergrate their modules more seamlessly due to more understanding of that module. Each issue that became apparent could also be identified more quickly, especially those that came up during the integration. This reduced the time spent on individual troubleshooting and provided an additional layer of review to catch dependency and compatibility issues.

- **Documentation of each module**

  We created documentation for each module, explaning its functionality and dependencies. This made it easier for team members to understand the workings of each module, which in turn simplified the integration process, helping everyone to understand how the modules would work together.

### Lesson learned:

Through these challenges, we learned several valuable lessons on working in a team:

- **Pair programming (Collaboration)**

  Collaborative approaches, like pair programming were beneficial, allowing us to address integration issues more efficiently and leveraging collective problem-solving skills.

- **Documentation is essential**

  Making documentation for each module not only helps during integration but also ensures that future development or troubleshooting can proceed more easly. Documentation, while time-consuming, saves time by providing clarity where this would be more time consuming.

- **Modular Architecture requires lots of planning**

  We learned that modular development requires a much more structured approach to planning and version control, especially as more modules are added and intergraated. By addressing these aspects from the start, we could of minimized complications later in the development process.

# making the project to a shippable product

## -difficult in it self

'temporary'

# Testing the project

One of the major challenges in this project was ensuring thorough test coverage, especially for all the classes and functionality within the application. Creating effective tests was particularly challenging for the controller classes in JavaFX, where certain elements, such as managing scene transitions and switching stages, were difficult to simulate and mock effectively.

### Key Challenges:

- **Mocking Dependencies:**

  We struggled with implementing mocks, especially for objects and methods that interacted directly with the JavaFX framework. For example, simulating user interactions with components like button, color pickers, and date selectors required a detailed setup, and maintaining thes mocks was complex and timeconsuming.

- **Ui and controller testing:**

Testing the JavaFX controller layer proved difficult because JavaFX requires the UI thread for most interactions. Standard test setups were often insufficient, as asynchonous tasks and multithreading issues complicated the behavior of controllers and UI elements during tests.

### Approchehes we tried:

To overcome these testing challenges, we implemented a few key approaches:

- **Using TestFX for JavaFX GUI testing:**

  To manage JavaFX-specific challenges, we used TestFX, which allowed us to simulate user actions (e.g., clicks, typing) in a way that JavaFX could handle. This tool enabled better control over UI elements but required careful setup to avoid test flakiness.

- **Mocking static methods with Mockito:**

  Many parts of our code relied on static methods, particularly for retrieving user data. To address this, we used Mockito to mock static calls, allowing us to simulate various user scenarios without needing real API calls.

  - **Separating UI logic:**

    We found that separating logic from the UI controller helped with testability. By placing logic into service classes and using dependency injection, we reduced the complexity of testing JavaFX controllers directly

  ### Lessons learned

  Through thes challenges, we gained a deeper understanding of the importance of testing in complex UI applications:

  - **Invest in tooling for UI tests:**

    Investing time in setting up tools like TestFX was essential for testing JavaFX applications effectively.

  - **Refactoring for testability:**

    Testing is much easier when controllers are designed with testability in mind. By separating logic from UI and avoiding static dependencies in controllers, we created a more maintainable and testable codebase.

  - **Mocking complex interactions:**

    Mocking proved essential in isolating unit tests. Having experience with Mockito and learning to mock complex interactions (espacially with JavaFX components) helped us simulate real application behavior effectively without relying on external systems or dependencies.

  This approach to testing, though challenging, improved to catch issues early and ensured that the UI behaved as expected in various scenarios.
