package org.pipservices3.components.log;

import org.junit.Test;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.commons.refer.References;

public class CompositeLoggerTest {
	
	private CompositeLogger _log;
    private LoggerFixture _fixture;
    
	public CompositeLogger getLog() {return _log;}
	public void setLog(CompositeLogger _log) {this._log = _log;}
	
	public LoggerFixture getFixture() {return _fixture;}
	public void setFixture(LoggerFixture _fixture) {this._fixture = _fixture;}
	
	public CompositeLoggerTest() throws ReferenceException
    {
        _log = new CompositeLogger();

        References refs;
		
			refs = References.fromTuples(
			    DefaultLoggerFactory.ConsoleLoggerDescriptor, new ConsoleLogger(), 
			    DefaultLoggerFactory.DiagnosticsLoggerDescriptor, new DiagnosticsLogger(),
			    DefaultLoggerFactory.CompositeLoggerDescriptor, _log
			);
		
        
        _log.setReferences(refs);

        _fixture = new LoggerFixture(_log);

    }
	
	@Test
	public void testLogLevel()
    {
        _fixture.testLogLevel();
    }
	
	@Test
	public void testSimpleLogging()
    {
        _fixture.testTextOutput();
    }
		
	@Test
	public void testErrorLogging()
    {
        _fixture.testErrorLogging();
    }

}
