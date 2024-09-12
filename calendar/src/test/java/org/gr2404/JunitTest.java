package org.gr2404;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JunitTest {
    @Test
    public void thisShouldPass1() {
        return;
    }

    @Test
    public void thisShouldPass2() {
        return;
    }

    @Test
    public void thisShouldFail1() {
        assertEquals(true, false, "This should fail");
    }

    @Test
    public void thisShouldPass3() {
        return;
    }

    @Test
    public void thisShouldFail2() {
        assertEquals(true, false, "This should fail");
    }
}
