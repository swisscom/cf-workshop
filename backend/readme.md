# Instructor
This readme will guide you through the setup for a workshop.

## Org, Spaces and Services

Create an Org, target it and execute the following commands:

```shell script
cf create-space backend
cf target -s backend
cf create-service elasticsearch free elasticsearch
cf create-service rabbitmqent free rabbitmq
```

Note that it might take some time to create the service-instances.

Sign up to receive an API key from [OpenWeatherMap](https://home.openweathermap.org/users/sign_up).

Create a Secrets Store service-instance for the API key:
```shell script
cf create-service secrets-store json openweathermap -c '{ "appId": "<API-key>" }'
```

Based on the count of teams, set the variable accordingly and share the service-instances with newly created spaces:

```shell script
export TEAMS=15
for i in $(seq -f "%02g" 0 $TEAMS); do cf create-space team-$i; done
for i in $(seq -f "%02g" 0 $TEAMS); do cf share-service rabbitmq -s team-$i; done
for i in $(seq -f "%02g" 0 $TEAMS); do cf share-service openweathermap -s team-$i; done
```

## Elastic Stack
Push the Kibana docker image using the manifest in [kibana](./kibana).

Log in to Kibana and create a Logstash Pipeline (called `logstash_pipeline`) using the `pipeline.txt` in [logstash](./logstash).
Import the dashboard in `Management > Kibana > Saved Object > Import`, pick `dashboard.ndjson`

Push the Logstash docker image using the manifest in [logstash](./logstash). 

Create a user-provided-service as a logstash-drain:
```shell script
cf cups logstash-drain -l https://random-user:changeme@cf-workshop-logstash.scapp.io
```

## Backend
Build and deploy the consumer app using `./deploy.sh` in [consumer](./consumer).

## Example Implementations
Build and deploy the two example implementations using `./deploy.sh` in the respective directories [webmvc](../team-00/webmvc) and [webflux](../team-00/webflux).  

## Final State
After you've set up everything, your org and spaces should look like this:
```shell script
$ cf t -s backend

$ cf a
name       requested state   instances   memory   disk   urls
consumer   started           1/1         768M     256M   cf-workshop-consumer.scapp.io
kibana     started           1/1         1G       2G     cf-workshop-kibana.scapp.io
logstash   started           1/1         1G       2G     cf-workshop-logstash.scapp.io

$ cf s
name             service         plan   bound apps         last operation     broker                upgrade available
elasticsearch    elasticsearch   free   kibana, logstash   create succeeded   open-service-broker   
logstash-drain   user-provided          consumer                                                    
openweathermap   secrets-store   json                      create succeeded   open-service-broker   
rabbitmq         rabbitmqent     free   consumer           create succeeded   open-service-broker

$ cf t -s team-00

$ cf a
name      requested state   instances   memory   disk   urls
webflux   started           1/1         768M     256M   cf-workshop-webflux.scapp.io
webmvc    started           1/1         768M     256M   cf-workshop-webmvc.scapp.io

$ cf s
name             service         plan   bound apps        last operation     broker                upgrade available
openweathermap   secrets-store   json   webflux, webmvc   create succeeded   open-service-broker   
rabbitmq         rabbitmqent     free   webflux, webmvc   create succeeded   open-service-broker 
```