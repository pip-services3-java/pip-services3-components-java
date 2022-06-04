# <img src="https://uploads-ssl.webflow.com/5ea5d3315186cf5ec60c3ee4/5edf1c94ce4c859f2b188094_logo.svg" alt="Pip.Services Logo" width="200"> <br/> Component definitions for Java

This module is a part of the [Pip.Services](http://pipservices.org) polyglot microservices toolkit.

The Components module contains standard component definitions that can be used to build applications and services.

The module contains the following packages:
- **Auth** - authentication credential stores
- **Build** - basic factories for constructing objects
- **Cache** - distributed cache
- **Config** - configuration readers and managers, whose main task is to deliver configuration parameters to the application from wherever they are being stored
- **Connect** - connection discovery and configuration services
- **Count** - performance counters
- **Info** - context info implementations that manage the saving of process information and sending additional parameter sets
- **Lock** -  distributed lock components
- **Log** - basic logging components that provide console and composite logging, as well as an interface for developing custom loggers
- **State** - components for managing states
- **Test** - minimal set of test components to make testing easier
- **Trace** - tracer components
- **Component** - the root package

<a name="links"></a> Quick links:

* [Logging](http://docs.pipservices.org/toolkit/recipes/logging/)
* [Configuration](http://docs.pipservices.org/toolkit/recipes/configuration/) 
* [API Reference](https://pip-services3-java.github.io/pip-services3-components-java/)
* [Change Log](CHANGELOG.md)
* [Get Help](http://docs.pipservices.org/get_help/)
* [Contribute](http://docs.pipservices.org/contribute/)

## Use

Go to the pom.xml file in Maven project and add dependencies::
```xml
<dependency>
  <groupId>org.pipservices3</groupId>
  <artifactId>pip-services3-components</artifactId>
  <version>3.1.3</version>
</dependency>
```

Example how to use Logging and Performance counters.
Here we are going to use CompositeLogger and CompositeCounters components.
They will pass through calls to loggers and counters that are set in references.

```java
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IConfigurable;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.components.count.CompositeCounters;
import org.pipservices3.components.count.CounterTiming;
import org.pipservices3.components.log.CompositeLogger;

public class MyComponent implements IConfigurable, IReferenceable {
    private final CompositeLogger _logger = new CompositeLogger();
    private final CompositeCounters _counters = new CompositeCounters();


    @Override
    public void configure(ConfigParams configParams) throws ConfigException {
        this._logger.configure(configParams);
    }

    @Override
    public void setReferences(IReferences references) throws ReferenceException, ConfigException {
        this._logger.setReferences(references);
        this._counters.setReferences(references);
    }

    public void myMethod(String correlationId, Object param1) {
        this._logger.trace(correlationId, "Executed method mycomponent.mymethod");
        this._counters.increment("mycomponent.mymethod.exec_count", 1);
        CounterTiming timing = this._counters.beginTiming("mycomponent.mymethod.exec_time");

        try {
            // ...
        } catch (Exception e) {
            this._logger.error(correlationId, e, "Failed to execute mycomponent.mymethod");
            this._counters.increment("mycomponent.mymethod.error_count", 1);
        }
    }
}
```

Example how to get connection parameters and credentials using resolvers.
The resolvers support "discovery_key" and "store_key" configuration parameters
to retrieve configuration from discovery services and credential stores respectively.


```java
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IConfigurable;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.commons.run.IOpenable;
import org.pipservices3.components.auth.CredentialParams;
import org.pipservices3.components.auth.CredentialResolver;
import org.pipservices3.components.connect.ConnectionParams;
import org.pipservices3.components.connect.ConnectionResolver;

public class MyComponent implements IConfigurable, IReferenceable, IOpenable {
    private boolean _opened = false;
    private final ConnectionResolver _connectionResolver = new ConnectionResolver();
    private final CredentialResolver _credentialResolver = new CredentialResolver();

    @Override
    public void configure(ConfigParams configParams) throws ConfigException {
        this._connectionResolver.configure(configParams);
        this._credentialResolver.configure(configParams);
    }

    @Override
    public void setReferences(IReferences refs) throws ReferenceException, ConfigException {
        this._connectionResolver.setReferences(refs);
        this._credentialResolver.setReferences(refs);
    }


    @Override
    public boolean isOpen() {
        return _opened;
    }

    @Override
    public void open(String correlationId) throws ApplicationException {
        ConnectionParams connection = this._connectionResolver.resolve(correlationId);
        CredentialParams credential = this._credentialResolver.lookup(correlationId);

        String host = connection.getHost();
        int port = connection.getPort();
        String user = credential.getUsername();
        String pass = credential.getPassword();

        _opened = true;
    }

    @Override
    public void close(String s) throws ApplicationException {
        _opened = false;
    }
}


```

Using the component:

```java
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.ApplicationException;

public class MainClass {
    public static void main(String[] args) throws ApplicationException {
        // Using the component
        MyComponent myComponent = new MyComponent();

        myComponent.configure(ConfigParams.fromTuples(
                "connection.host", "localhost",
                "connection.port", 1234,
                "credential.username", "anonymous",
                "credential.password", "pass123"
        ));

        myComponent.open(null);
    }
}
```


Example how to use caching and locking.
Here we assume that references are passed externally.

```java
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.components.cache.ICache;
import org.pipservices3.components.lock.*;

public class MyComponent implements IReferenceable {

    private ICache _cache;
    private ILock _lock;

    @Override
    public void setReferences(IReferences refs) throws ReferenceException, ConfigException {
        this._cache = (ICache) refs.getOneRequired(new Descriptor("*", "cache", "*", "*", "1.0"));
        this._lock = (ILock) refs.getOneRequired(new Descriptor("*", "lock", "*", "*", "1.0"));
    }

    public Object myMethod(String correlationId, Object param1) {
        // First check cache for result
        Object result = this._cache.retrieve(correlationId, "mykey");
        if (result != null)
            return result;

        // Lock..
        this._lock.acquireLock(correlationId, "mykey", 1000, 1000);

        // Do processing
        // ...

        // Store result to cache async
        this._cache.store(correlationId, "mykey", result, 3600000);

        // Release lock async
        this._lock.releaseLock(correlationId, "mykey");

        return result;
    }
}
```

Use the component:

```java
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.References;
import org.pipservices3.components.cache.MemoryCache;
import org.pipservices3.components.lock.MemoryLock;

public class MainClass {
    public static void main(String[] args) throws ApplicationException {
        // Use the component
        MyComponent myComponent = new MyComponent();

        myComponent.setReferences(References.fromTuples(
                        new Descriptor("pip-services", "cache", "memory", "default", "1.0"), new MemoryCache(),
                        new Descriptor("pip-services", "lock", "memory", "default", "1.0"), new MemoryLock()
                )
        );

        Object result = myComponent.myMethod(null, null);
    }
}
```

If you need to create components using their locators (descriptors) implement
component factories similar to the example below.

```java
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

public class MyFactory extends Factory {
    public static Descriptor myComponentDescriptor = new Descriptor("myservice", "mycomponent", "default", "*", "1.0");

    public MyFactory() {
        this.registerAsType(MyFactory.myComponentDescriptor, MyComponent.class);
    }
}

// Using the factory

MyFactory myFactory = new MyFactory();

MyComponent myComponent1 = (MyComponent) myFactory.create(new Descriptor("myservice", "mycomponent", "default", "myComponent1", "1.0"));
MyComponent myComponent2 = (MyComponent) myFactory.create(new Descriptor("myservice", "mycomponent", "default", "myComponent2", "1.0"));
...
```

## Develop

For development you shall install the following prerequisites:
* Java SE Development Kit 18+
* Eclipse Java Photon or another IDE of your choice
* Docker
* Apache Maven

Build the project:
```bash
mvn install
```

Run automated tests:
```bash
mvn test
```

Generate API documentation:
```bash
./docgen.ps1
```

Before committing changes run dockerized build and test as:
```bash
./build.ps1
./test.ps1
./clear.ps1
```

## Contacts

The initial implementation is done by **Sergey Seroukhov**. Pip.Services team is looking for volunteers to 
take ownership over Java implementation in the project.
