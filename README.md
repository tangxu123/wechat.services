![](http://dangdangdotcom.github.io/dubbox/images/rest.jpg)

* 支持REST风格远程调用（HTTP + JSON/XML)：基于非常成熟的JBoss [RestEasy](http://resteasy.jboss.org/)框架，在dubbo中实现了REST风格（HTTP + JSON/XML）的远程调用，以显著简化企业内部的跨语言交互，同时显著简化企业对外的Open API、无线API甚至AJAX服务端等等的开发。事实上，这个REST调用也使得Dubbo可以对当今特别流行的“微服务”架构提供基础性支持。 另外，REST调用也达到了比较高的性能，在基准测试下，HTTP + JSON与Dubbo 2.x默认的RPC协议（即TCP + Hessian2二进制序列化）之间只有1.5倍左右的差距。
* 支持基于Kryo和FST的Java高效序列化实现：基于当今比较知名的[Kryo](https://github.com/EsotericSoftware/kryo)和[FST](https://github.com/RuedigerMoeller/fast-serialization)高性能序列化库，为Dubbo默认的RPC协议添加新的序列化实现，并优化调整了其序列化体系，比较显著的提高了Dubbo RPC的性能。
* 支持基于Jackson的JSON序列化：基于业界应用最广泛的[Jackson](http://jackson.codehaus.org/)序列化库，为Dubbo默认的RPC协议添加新的JSON序列化实现。
* 支持基于嵌入式Tomcat的HTTP remoting体系：基于嵌入式tomcat实现dubbo的HTTP remoting体系（即dubbo-remoting-http），用以逐步取代Dubbo中旧版本的嵌入式Jetty，可以显著的提高REST等的远程调用性能，并将Servlet API的支持从2.5升级到3.1。（注：除了REST，dubbo中的WebServices、Hessian、HTTP Invoker等协议都基于这个HTTP remoting体系）。
* 升级Spring：将dubbo中Spring由2.x升级到目前最常用的3.x版本，减少版本冲突带来的麻烦。
* 升级ZooKeeper客户端：将dubbo中的zookeeper客户端升级到最新的版本，以修正老版本中包含的bug。
* 支持完全基于Java代码的Dubbo配置：基于Spring的Java Config，实现完全无XML的纯Java代码方式来配置dubbo
* 调整Demo应用：暂时将dubbo的demo应用调整并改写以主要演示REST功能、Dubbo协议的新序列化方式、基于Java代码的Spring配置等等。
* Ajax 跨域