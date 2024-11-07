module calendar.rest {
    requires transitive calendar.types;
    requires transitive calendar.persistence;
    requires jdk.httpserver;

    exports calendar.rest;

}
