package org.pipservices3.components.log;

import org.junit.Test;

public class DiagnosticsLoggerTest {

	private ILogger _log;
    private LoggerFixture _fixture;
    
	public ILogger getLog() {return _log;}
	public void setLog(ILogger _log) {this._log = _log;}
	
	public LoggerFixture getFixture() {return _fixture;}
	public void setFixture(LoggerFixture _fixture) {this._fixture = _fixture;}
	
	
	public DiagnosticsLoggerTest()
    {
        _log = new DiagnosticsLogger();
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
