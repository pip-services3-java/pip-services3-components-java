# <img src="https://github.com/pip-services/pip-services/raw/master/design/Logo.png" alt="Pip.Services Logo" style="max-width:30%"> <br/> Basic portable abstractions for Java

This framework is a part of [Pip.Services](https://github.com/pip-services/pip-services) project.
It provides basic building blocks that can be used to implement non-trivial business logic in applications and services.

The key difference of this framework is a portable implementation across variety of different languages. 
Currently it supports Java, .NET, Python, Node.js, Golang. The code provides reasonably thin abstraction layer 
over most fundamental functions and delivers symmetric implementation that can be quickly ported between different platforms.

All functionality is decomposed into several packages:

- **Auth** - authentication and authorization primitives
- **Build** - component factories framework
- **Cache** - object caching
- **Config** - configuration framework
- **Connect** - connection management
- **Count** - performance counters components
- **Info** - context info
- **Log** - logging components

Quick Links:

* [Downloads](https://github.com/pip-services3-java/pip-services3-components-java/blob/master/doc/Downloads.md)
* [API Reference](http://htmlpreview.github.io/?https://github.com/pip-services3-java/pip-services3-components-java/blob/master/doc/api/index.html)
* [Building and Testing](https://github.com/pip-services3-java/pip-services3-components-java/blob/master/doc/Development.md)
* [Contributing](https://github.com/pip-services3-java/pip-services3-components-java/blob/master/doc/Development.md/#contrib)

## Acknowledgements

The initial implementation is done by **Sergey Seroukhov**. Pip.Services team is looking for volunteers to 
take ownership over Java implementation in the project.
