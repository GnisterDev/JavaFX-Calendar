package org.gr2404;

import org.junit.jupiter.api.Test;

public class SystemInfoTest {
    @Test
    public void javaVersion() {
        SystemInfo.javaVersion();
    }

    @Test
    public void javafxVersion() {
        SystemInfo.javafxVersion();
    }
}
