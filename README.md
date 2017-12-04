
http://www.jorgehernandezramirez.com/2017/09/02/spring-cloud-consul-ribbon-feign/
https://github.com/JorgeHernandezRamirez/SpringCloudConsul
https://github.com/spring-cloud/spring-cloud-consul/blob/master/docs/src/main/asciidoc/spring-cloud-consul.adoc

http://cloud.spring.io/spring-cloud-consul/multi/multi_spring-cloud-consul-config.html
http://cloud.spring.io/spring-cloud-consul/multi/multi_spring-cloud-consul-discovery.html

/config/application/data

Consul Requires that YAML is suffixed by 'data' key to work properly
/ config / [MICROSERVICE_NAME], [PROFILE] / data
/ config / [MICROSERVICE_NAME] / data
/ config / application, [PROFILE] / data
/ config / application / data


#Running this example

#Start Docker Container for Consul (runs on port 8500)
1. docker-compose up
# Run Git2Consul Command to populate Consul KV Store
2. git2consul --endpoint localhost --port 8500 --config-file /Users/johnmorgan/Documents/dev/spring-cloud/SpringConsulDemo/src/main/resources/git2consul/Git2Consul-SpringConsulDemo-master.json


#This command will work as long as
spring.cloud.consul.config.format = yaml | properties




#Useful Links
#Consul
http://localhost:8500
#Application Health Check
http://localhost:8080/health

#Application Controller Tests
# Returns the String "Alive!"
http://localhost:8080/ping

# Returns the String "Alive!" via standard RestTemplate mapped to localhost
http://localhost:8080/ping/rest
# Returns the String "Alive!" via Ribbon load balanced RestTemplate mapped to springcloudwebtest (service discovery)
http://localhost:8080/ping/rest/ribbon
# Returns the String "Alive!" via Ribbon load balanced feign client
http://localhost:8080/ping/rest/feign

#This endpoint will display the properties that were resolved from the Consul KV store
http://localhost:8080/property

TODO:  Need to configure a Git2Consul example
https://github.com/jmorgan0825/Git2Consul
https://github.com/breser/git2consul


https://bitbucket.org/bettercloud/bettercloud-properties/src/03363ff028ab906fb2d349127b8dc230d2cd2a0d/config/?at=master
- Needs to support a directory structure approach, all within continuous/master branch
  - Resolve global configs (application), Pillar (profile), Infrastructure (profile), App (spring.application.name), Profiles
  - Profiles:
    Environments: Local, Holding, Staging, Prod, LoadTest
    Infrastructure: MySQL, Kafka, Redis, etc.
    Pillar: Directory, Workflow, CIC
    Deployment Modes: REST, Kafka, Shadow Migration, Autobot, etc

    determine if Git2Consul will support yaml embedded profiles
    https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-change-configuration-depending-on-the-environment

    Global
    Environment.Infrastructure.[cluster id] - ex. holding-kafka, holding-mysql-auditlog
    Pillar
    Application

TODO: Need to figure out how local development will work
what about local dev?  DO I need to run consul, can I have local config?
http://cloud.spring.io/spring-cloud-static/spring-cloud.html#overriding-bootstrap-properties
https://stackoverflow.com/questions/43800256/how-spring-cloud-config-use-local-property-override-remote-property
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-multi-profile-yaml
https://stackoverflow.com/questions/43636201/spring-cloud-config-in-local-dev-mode
https://stackoverflow.com/questions/23617831/what-is-the-order-of-precedence-when-there-are-multiple-springs-environment-pro
https://stackoverflow.com/questions/32832734/applying-multiple-spring-profiles-to-a-single-configuration-class

#Good tip for disabling Bootstrap, once you disable bootstrap then service discovery is diabled hence no feign @Loadbalanced annotated endpoint will fail
#Also any name-resolved rest templates will not work.  We would need to make more dynamic configuration to support disabling bootstrap.
https://github.com/spring-cloud/spring-cloud-consul/issues/147
#More Details on Bootstrap context
http://cloud.spring.io/spring-cloud-static/spring-cloud.html#_the_bootstrap_application_context

#Stackoverflow
#https://stackoverflow.com/questions/38251693/correct-way-to-load-values-properties-from-spring-cloud-consul

TODO: Git2Consul will need to be able to talk to a private repo
    - how to connect, application account
    - how will it be secure

TODO: This will need to play with Vault

TODO:  Would like to include zuul and hystrix in this example


