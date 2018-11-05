package org.pipservices3.components.count;

import org.junit.*;
import org.pipservices3.commons.errors.*;
import org.pipservices3.commons.refer.*;
import org.pipservices3.components.log.ConsoleLogger;

public class LogCountersTest {
    private LogCounters counters;
    private CountersFixture fixture;
    
	@Before
	public void setUp() throws ReferenceException {
        ConsoleLogger log = new ConsoleLogger();
        References refs = References.fromTuples(log);

        counters = new LogCounters();
        counters.setReferences(refs);
        
        fixture = new CountersFixture(counters);
    }

    @Test
    public void testSimpleCounters() throws InvocationException {
        fixture.testSimpleCounters();
    }

    @Test
    public void TestMeasureElapsedTime() throws InvocationException {
        fixture.testMeasureElapsedTime();
    }
}
