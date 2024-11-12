# Release 3

This is the third release for the project. It contains the a complete working calendar app, which has a backend that is running on a seperate web server.

### Project architecture

### Final functions for application

-Update the functionallity in the readme, explain why we changed it at what changes we made

### Work habits

Our work habits have remained largely consistent since Release 2, with the primary change being that we now create more specific issues. In Release 2, we often created issues that were quite broad, which caused challenges when attempting to work in parallel ([explained in greater detail under Work Habits in Release 2](../release2/README.md)). By defining issues more narrowly, we were able to merge branches into the development branch earlier and collaborate more efficiently, reducing the need for complex merges and allowing us to work on separate issues with minimal conflict.

### Code quality

We have implemented Checkstyle to maintain consistent code formatting and ensure adherence to coding standards. Additionally, we have documented the entire project with JavaDoc to improve code readability for anyone who wants to understand how the code works.

To futher enhance code quality, we refactored a big portion of the code to minimize duplication and improve overall structure, making the concise and readable.

### Test coverage

We have good test Coverage for the whole project, We are missing some test coverage for calendarController and popUpController. The reason is because we could not find a way to mock the the ui changes that happen when we add and edit an event.

write what our actuall test coverage is:

### REST-service

### Contribution

[Own md-file for discussing the contribution](./contribution.md)

### Sustainability

[Own md-file for discussing the sustainability](./sustainability.md)

### Challenges

[Own md-file for the challenges we expirenced](./challenges.md)

### Known issues

Event with startime after endtime, still gives error if you chose to edit the event to span the whole day.

Events that span the same time will overlapp.

### AI statement

[Own md-file for ai statement](./ai-tools.md)
