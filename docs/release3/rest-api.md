# Rest API

This RESTful API provides a way to manage calendars for multiple users. It has support for multiple users with multiple calendars each. Every important endpoint has authentication, so that only someone who knows a users credentials (username and password) can obtain information about said user.

### User management

The API provides ways to create new users and fetch information like user settings and list of calendars the user owns.

### Calendar management

The user may also manage their calendars through this API. It provides methods to add and remove calendars, to better organize groups of events that belong together.

### Event management

The API also provides endpoints for fetching and editing, deleting and adding new events. The fetching endpoint also offers an optional filtering based on event start and end time, so the response length remains reasonable even when the calendar contains many events.

## Endpoints

> ## <span style="color:#3ca71f">**GET**</span> /users/{username}
>
> Returns user data for a user with the specified username. \
> A password must be provided in the header as a "password" field.
>
> ### Curl example
>
> ```bash
> curl -X GET -H "password: $PASSWORD" $ADDRESS/users/$USERNAME
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">200</span> (OK)**
>
> ```json
> {
>     "username": "username",
>     "userId": "5382f888-3eb9-4c2c-98f3-fbb633bc33ea",
>     "calendars": [
>         {
>             "name": "calendarName",
>             "calendarId": "206d6f02-4490-4f1c-97a2-4ab0ac65c2c9"
>         }
>     ],
>     "settings:": {
>         "userId": "5382f888-3eb9-4c2c-98f3-fbb633bc33ea",
>         "timezone": "Europe/Oslo",
>         "militaryTime": true,
>         "showWeekNr": true
>     }
> }
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a GET request to `/users/username/other` would result in a 400 response code.
>
> #### **<span style="color:red">404</span> (Not found)**
>
> Returned if there is no existing user with the supplied username.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided password does not match the users actual password.

> ## <span style="color:#3ca71f">**POST**</span> /users/{username}
>
> Creates a user with the specified username. \
> A password must be provided in the header as a "password" field.
>
> ### Curl example
>
> ```bash
> curl -X POST -H "password: $PASSWORD" $ADDRESS/users/$USERNAME
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">201</span> (Created)**
>
> ```json
> User 'username' created successfully
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a POST request to `/users/username/other` would result in a 400 response code.
>
> It is also returned if the password field in the headers is empty.
>
> #### **<span style="color:red">409</span> (Conflict)**
>
> Returned if a user with the specified username already exists.

> ## <span style="color:#3ca71f">**GET**</span> /calendar/{calendarId}
>
> Returns a list of events within the calendar with the specified calendarId. \
> Username and password must be provided in the header as the "username" and "password" field respectively. \
> Optionally the "before" and "after" headers can be used to filter the returned events based on time.
>
> ### Curl example
>
> ```bash
> curl -X GET -H "username: $USERNAME -H "password: $PASSWORD" $ADDRESS/calendar/$CALENDARID
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">200</span> (OK)**
>
> ```json
> [
>     {
>         "title": "eventName",
>         "description": "eventDescription",
>         "startTime": [2024, 11, 13, 12, 0],
>         "endTime": [2024, 11, 13, 14, 0],
>         "color": {
>             "red": 0.9176470637321472,
>             "green": 0.2705882489681244,
>             "blue": 0.2980392277240753
>         },
>         "type": "REGULAR",
>         "id": "6ac18707-0609-46cf-ae95-ef41ba09e8f0"
>     }
> ]
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a GET request to `/calendar/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/other` would result in a 400 response code.
>
> It is also returned if the username or password field in the header is missing or if the before or after fields have a wrong format.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided credentials are wrong.
>
> #### **<span style="color:red">404</span> (Not found)**
>
> Returned if there is no calendar with the provided calendarId.

> ## <span style="color:#3ca71f">**POST**</span> /calendar
>
> Creates a calendar with the specified name for the specified user. \
> The user credentials must be provided in the "username" and "password" fields in the header. \
> The name is optionally provided as a "name" field in the header. \
> If no name is provided, defaults to "Unnamed calendar".
>
> ### Curl example
>
> ```bash
> curl -X POST -H "username: $USERNAME -H "password: $PASSWORD" -H "name: $CALENDARNAME" $ADDRESS/calendar
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">201</span> (Created)**
>
> ```
> Calendar with id '206d6f02-4490-4f1c-97a2-4ab0ac65c2c9' successfully created
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a POST request to `/calendar/other` would result in a 400 response code.
>
> It is also returned if the username or password field in the header is missing.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided credentials are wrong.

> ## <span style="color:#3ca71f">**DELETE**</span> /calendar/{calendarId}
>
> Deletes the calendar with the specified calendarId from the specified user \
> The user credentials must be provided in the "username" and "password" fields in the header.
>
> ### Curl example
>
> ```bash
> curl -X DELETE -H "username: $USERNAME -H "password: $PASSWORD" $ADDRESS/calendar/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">200</span> (OK)**
>
> ```
> Calendar with id '206d6f02-4490-4f1c-97a2-4ab0ac65c2c9' successfully deleted
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a DELETE request to `/calendar/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/other` would result in a 400 response code.
>
> It is also returned if the username or password field in the header is missing.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided credentials are wrong.
>
> #### **<span style="color:red">404</span> (Not found)**
>
> Returned if there is no calendar with the provided calendarId.

> ## <span style="color:#3ca71f">**POST**</span> /event/{calendarId}
>
> Creates a new event in the specified users calendar. \
> The user credentials must be provided in the "username" and "password" fields in the header.
>
> ### **Event header options:**
>
> -   <span style="color:gold">**title:**</span> The title of the event. (<span style="color:lightblue">default</span>: `Untitled event`)
> -   <span style="color:gold">**description:**</span> The description of the event. (<span style="color:lightblue">default</span>: ` `)
> -   <span style="color:gold">**start:**</span> The start time of the event. (<span style="color:red">required</span>)
> -   <span style="color:gold">**end:**</span> The end time of the event. (<span style="color:red">required</span>)
> -   <span style="color:gold">**color:**</span> The color of the event for displaying in the client. (<span style="color:lightblue">default</span>: `blue`)
> -   <span style="color:gold">**type:**</span> The type of the event. One of [`REGULAR`, `ALL_DAY`] (<span style="color:lightblue">default</span>: `REGULAR`)
>
> ### Curl example
>
> ```bash
> curl -X POST -H "username: $USERNAME -H "password: $PASSWORD" $ADDRESS/event
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">201</span> (Created)**
>
> ```
> Event with id '6ac18707-0609-46cf-ae95-ef41ba09e8f0' successfully created
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a POST request to `/event/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/other` would result in a 400 response code.
>
> It is also returned if the username or password field in the header is missing or if the start date, end date, color or type header fields have the wrong format or are empty.
>
> An end time that is before the start time will also trigger this reponse.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided credentials are wrong.
>
> #### **<span style="color:red">404</span> (Not found)**
>
> Returned if there is no calendar with the provided calendarId.

> ## <span style="color:#3ca71f">**PUT**</span> /event/{calendarId}/{eventId}
>
> Modifies the specified event with the new data. \
> The user credentials must be provided in the "username" and "password" fields in the header.
>
> ### **Event header options:**
>
> All headers are optional. If left empty, will leave the event field as is.
>
> -   <span style="color:gold">**title:**</span> The title of the event.
> -   <span style="color:gold">**description:**</span> The description of the event.
> -   <span style="color:gold">**start:**</span> The start time of the event.
> -   <span style="color:gold">**end:**</span> The end time of the event.
> -   <span style="color:gold">**color:**</span> The color of the event for displaying in the client.
> -   <span style="color:gold">**type:**</span> The type of the event. One of [`REGULAR`, `ALL_DAY`]
>
> ### Curl example
>
> ```bash
> curl -X PUT -H "username: $USERNAME -H "password: $PASSWORD" -H "title: $NEW_TITLE" $ADDRESS/event/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/6ac18707-0609-46cf-ae95-ef41ba09e8f0
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">200</span> (OK)**
>
> ```
> Event with id '6ac18707-0609-46cf-ae95-ef41ba09e8f0' successfully edited
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a PUT request to `/event/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/6ac18707-0609-46cf-ae95-ef41ba09e8f0/other` would result in a 400 response code.
>
> It is also returned if the username or password field in the header is missing or if the start date, end date, color or type header fields have the wrong format.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided credentials are wrong.
>
> #### **<span style="color:red">404</span> (Not found)**
>
> Returned if there is no calendar with the provided calendarId or if there is no event with the provided eventId.

> ## <span style="color:#3ca71f">**DELETE**</span> /event/{calendarId}/{eventId}
>
> Deletes the specified event from the specified calendar. \
> The user credentials must be provided in the "username" and "password" fields in the header.
>
> ### Curl example
>
> ```bash
> curl -X DELETE -H "username: $USERNAME -H "password: $PASSWORD" $ADDRESS/event/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/6ac18707-0609-46cf-ae95-ef41ba09e8f0
> ```
>
> ### Return value
>
> #### **<span style="color:#3ca71f">200</span> (OK)**
>
> ```
> Event with id '6ac18707-0609-46cf-ae95-ef41ba09e8f0' successfully deleted
> ```
>
> ### Errors
>
> #### **<span style="color:red">400</span> (Bad request)**
>
> Returned if the route queried route contains more than just a username. For instance, a DELETE request to `/event/206d6f02-4490-4f1c-97a2-4ab0ac65c2c9/6ac18707-0609-46cf-ae95-ef41ba09e8f0/other` would result in a 400 response code.
>
> It is also returned if the username or password field in the header is missing.
>
> #### **<span style="color:red">401</span> (Unauthorized)**
>
> Returned if the provided credentials are wrong.
>
> #### **<span style="color:red">404</span> (Not found)**
>
> Returned if there is no calendar with the provided calendarId or if there is no event with the provided eventId.
