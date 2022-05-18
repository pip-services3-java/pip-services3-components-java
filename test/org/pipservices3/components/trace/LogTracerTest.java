package org.pipservices3.components.trace;

import org.junit.Before;
import org.junit.Test;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.commons.refer.References;
import org.pipservices3.components.log.NullLogger;

public class LogTracerTest {
    private LogTracer _tracer;

    @Before
    public void setup() throws ReferenceException {
        _tracer = new LogTracer();
        _tracer.setReferences(References.fromTuples(
                new Descriptor("pip-services", "logger", "null", "default", "1.0"), new NullLogger()
        ));
    }

    @Test
    public void testSimpleTracing() {
        _tracer.trace("123", "mycomponent", "mymethod", 123456L);
        _tracer.failure("123", "mycomponent", "mymethod", new Exception("Test error"), 123456);
    }

    @Test
    public void testTraceTiming() {
        TraceTiming timing = _tracer.beginTrace("123", "mycomponent", "mymethod");
        timing.endTrace();

        timing = _tracer.beginTrace("123", "mycomponent", "mymethod");
        timing.endFailure(new Exception("Test error"));
    }
}
