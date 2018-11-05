package org.pipservices3.components.log;

import org.junit.*;

public class NullLoggerTest {
	private ILogger log;
	private LoggerFixture fixture;
	
	@Before
	public void setUp() throws Exception {
		log = new NullLogger();
		fixture = new LoggerFixture(log);
	}
	
	@Test
	public void testLogLevel() {
		fixture.testLogLevel();
	}

	@Test
	public void testTextOutput() {
		fixture.testTextOutput();
	}
}
