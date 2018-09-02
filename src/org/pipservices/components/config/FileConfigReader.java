package org.pipservices.components.config;

import org.pipservices.commons.config.*;

public abstract class FileConfigReader extends ConfigReader implements IConfigurable {
	protected String _path;
	
	public FileConfigReader() {}
	
    public FileConfigReader(String path) {
        _path = path;
    }

    public String getPath() { return _path; }
    public void setPath(String value) { _path = value; }

    public void configure(ConfigParams config) {
        _path = config.getAsString("path");
    }
}
