# Project Objectives
This is a sample project to demonstrate Spring Cloud Config and Service Discovery with Consul.  The project demonstrates how to load Consul KV Store from a [Git Repository](https://github.com/jmorgan0825/Git2Consul) using Git2Consul. In addition, the project demonstrates a simple Feign client, Ribbon client-side load-balancing, and dynamic property refreshing.

> I could only get the project to work correctly when the following property was set to `yaml` or `properties` despite the documentation calling for the property to be set with `files`.  
  `spring.cloud.consul.config.format = yaml | properties`

## Remaining Objectives
* Integrate Vault to store and resolve secrets
* Refine the local development process (bypass bootstrap)
* Include Zuul | Cloud Gateway into this project
* Include Hystrix into this project
* Connect Git2Consul to a private repository.  How will it be secure?

# Running this project  
1. Start Docker Container for Consul (runs on port 8500)  
`docker-compose up`
2. Initialize Vault
[Initialize Vault](see below)
3. Run Application  
`./gradlew bootRun -Dspring.profiles.active="mypillar,holding"`  
Or without Spring Profiles  
`./gradlew bootRun`


# Consul Key Value (KV) Structure  

/ config / application / data  
/ config / application-[spring-profile] / data  
/ config / [microservice-name] / data  
/ config / [microservice-name]-[spring-profile] / data  

> The application key: `/config/application/data` is considered global to all applications.  
> The application key: `/config/application-[spring-profile]/data` is considered global to all applications with the specified spring profile.  
> Consul Requires that YAML is suffixed by 'data' key to work properly.  The configuration above assumes that the key values are all YAML.  

# Useful Project Links
* [Consul - localhost:8500](http://localhost:8500)
* [Application Health Check](http://localhost:8080/health)
* [Simple Rest Ping - Returns Alive!](http://localhost:8080/ping)
* [RestTemplate mapped to localhost:8080/ping - Returns Alive!](http://localhost:8080/ping/rest)
* [Ribbon load balanced RestTemplate mapped to springconsuldemo - Returns Alive!](http://localhost:8080/ping/rest/ribbon)
* [Ribbon load balanced Feign client - Returns Alive!](http://localhost:8080/ping/rest/feign)
* [Endpoint to display the properties resolved from the Consul KV store](http://localhost:8080/property)

# Resources
* [Spring Cloud Consul](https://github.com/spring-cloud/spring-cloud-consul/blob/master/docs/src/main/asciidoc/spring-cloud-consul.adoc)
* [Spring Cloud Consul - Reference](http://cloud.spring.io/spring-cloud-consul/multi/multi_spring-cloud-consul-config.html)
* [Git2Consul Configuration Repo](https://github.com/jmorgan0825/Git2Consul)
* [Article used as base for project](http://www.jorgehernandezramirez.com/2017/09/02/spring-cloud-consul-ribbon-feign/)
* [Overriding Bootstrap Properties](http://cloud.spring.io/spring-cloud-static/spring-cloud.html#overriding-bootstrap-properties)
* [Overriding Remote Properties](https://stackoverflow.com/questions/43800256/how-spring-cloud-config-use-local-property-override-remote-property)
* [Multi-Profile YAML](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-multi-profile-yaml)
* [Cloud Config - Local Dev Mode](https://stackoverflow.com/questions/43636201/spring-cloud-config-in-local-dev-mode)
* [Spring Profiles - Order of Precedence](https://stackoverflow.com/questions/23617831/what-is-the-order-of-precedence-when-there-are-multiple-springs-environment-pro)
* [Applying multiple Spring profiles](https://stackoverflow.com/questions/32832734/applying-multiple-spring-profiles-to-a-single-configuration-class)
* [Good tip for disabling Bootstrap](https://github.com/spring-cloud/spring-cloud-consul/issues/147)
* [More Details on Bootstrap context](http://cloud.spring.io/spring-cloud-static/spring-cloud.html#_the_bootstrap_application_context)


# Cloud Config Requirements
https://bitbucket.org/bettercloud/bettercloud-properties/src/03363ff028ab906fb2d349127b8dc230d2cd2a0d/config/?at=master
* Need to support a directory structure approach, all properties are contained within a single continuous/master branch
* Resolve global configs (application), Pillar (profile), Infrastructure (profile), App (spring.application.name)
* Determine if Git2Consul will support yaml with multiple embedded profiles
  - Profiles:
    Environments: Local, Holding, Staging, Prod, LoadTest
    Infrastructure: MySQL, Kafka, Redis, etc.
    Pillar: Directory, Workflow, CIC
    Deployment Modes: REST, Kafka, Shadow Migration, Autobot, etc

    
    https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-change-configuration-depending-on-the-environment

    Global
    Environment.Infrastructure.[cluster id] - ex. holding-kafka, holding-mysql-auditlog
    Pillar
    Application


#once you disable bootstrap then service discovery is diabled hence no feign @Loadbalanced annotated endpoint will fail
#Also any name-resolved rest templates will not work.  We would need to make more dynamic configuration to support disabling bootstrap.

#Stackoverflow
#https://stackoverflow.com/questions/38251693/correct-way-to-load-values-properties-from-spring-cloud-consul

https://github.com/JorgeHernandezRamirez/SpringCloudConsul
https://github.com/spring-cloud/spring-cloud-consul/blob/master/docs/src/main/asciidoc/spring-cloud-consul.adoc

http://cloud.spring.io/spring-cloud-consul/multi/multi_spring-cloud-consul-config.html
http://cloud.spring.io/spring-cloud-consul/multi/multi_spring-cloud-consul-discovery.html

## Initialize Vault

```bash
$ export VAULT_ADDRESS=http://127.0.0.1:8200
$ vault init
$ vault unseal // paste one unseal key
$ vault unseal // paste another unseal key
$ vault unseal // paste another unseal key
$ vault auth // paste root token
$ vault write secret/password value=my_super_secret_password
```
