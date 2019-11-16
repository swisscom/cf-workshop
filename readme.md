# About
This repository contains resources for a Cloud Foundry workshop. It consists of the two directories `backend` and `team-00`. An instructor needs to create an Organization and deploy the apps and services as described in `backend`. The apps in `team-00` are example implementations.   

# Instructor
Please follow the instructions in the [instructor's directory](./backend/readme.md).

# Participants
As a participant, your goal is to learn the basic concepts of Cloud Foundry. Therefore, the instructor has already created a dedicated Space for you or your team and granted you access to it.

Your task is to fetch weather information from a location of your choice, format the values in a given structure and send it to the instructor's application through a [shared](https://docs.developer.swisscom.com/devguide/services/sharing-instances.html) [RabbitMQ](https://docs.developer.swisscom.com/service-offerings/rabbitmqent.html) [service instance](https://docs.developer.swisscom.com/devguide/services/).

If you struggle, check out the [example implementation](./team-00). It contains two already working apps: One is a Spring Boot app targeting Java 8 and using the WebMVC stack. The second app is a Spring Boot app targeting Java 11 and using the Webflux stack.

## Fetch Data
You can fetch weather information from any source you'd like. If you're looking for a free service, check out [OpenWeatherMap](https://openweathermap.org/current). To access the API, you need a key though. Your instructor has already created an API-key for OpenWeatherMap and shared it with your team in a service called [Secrets Store](https://docs.developer.swisscom.com/service-offerings/secretsstore.html). [Bind](https://docs.developer.swisscom.com/tutorial-java/bind-service.html) its instance `openweathermap` to your app and you'll find the key stored as `vcap.services.openweathermap.credentials.appId` (or `VCAP_SERVICES_OPENWEATHERMAP_CREDENTIALS_APP_ID`) in the environment. 
 
You now can access the API like this: `http://api.openweathermap.org/data/2.5/weather?q=Ittigen,ch&units=metric&APPID=<appId>` to receive the following response:
 
```json
{
  "coord": {
    "lon": 7.48,
    "lat": 46.98
  },
  "weather": [
    {
      "id": 500,
      "main": "Rain",
      "description": "light rain",
      "icon": "10d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 6.33,
    "pressure": 1009,
    "humidity": 70,
    "temp_min": 3.89,
    "temp_max": 9
  },
  "visibility": 10000,
  "wind": {
    "speed": 2.1,
    "deg": 300
  },
  "rain": {
    "1h": 0.25
  },
  "snow": {
    "1h": 0.0
  },
  "clouds": {
    "all": 40
  },
  "dt": 1573914958,
  "sys": {
    "type": 1,
    "id": 6937,
    "country": "CH",
    "sunrise": 1573886056,
    "sunset": 1573919732
  },
  "timezone": 3600,
  "id": 6424338,
  "name": "Ittigen",
  "cod": 200
}
```

Parse this response and extract the interesting fields `name`, `main.temp`, `main.humidity`, `rain.1h`, `snow.1h`, `coord.lat` and `coord.lon`.

## Store Data
With the fetched data at hand, create a new object following this structure: 
```json
{
    "location" : "name",
    "celsius" : 15.3,
    "humidity" : 60.2,
    "rain" : 0.0,
    "snow" : 0.0,
    "coordinatesLatitude" : 47.4,
    "coordinatesLongitude" : 8.54
}
```

Bind the shared RabbitMQ service-instance called `rabbitmq` to your app and send your object as a message using this routing-key: `weather-data`. If you've done everything correctly, the consumer on the other side implemented by your instructor will receive and process the message. Ask him or her to refresh the dashboard to verify your message has reached its destination. 

### Local Development
For local development, use a Dockerized RabbitMQ by creating a `docker-compose.yml` with this content: 
```yaml
version: "2"
services:
  rabbitmq:
    image: rabbitmq:3.7.8-management
    ports:
      - 5672:5672
      - 15672:15672
```
Start it with `docker-compose up`. 

## Deployment
This command will [push](https://docs.developer.swisscom.com/devguide/deploy-apps/deploy-app.html) your built jar to the Application Cloud: `cf push -p path-to-app.jar --random-route`. However, it's recommended to use a manifest to specify further properties. If you use the following `manifest.yml`, you can push your app simply with `cf push`:
```yaml
applications:
  - name: myapp
    instances: 1
    disk_quota: 256M
    memory: 768M
    path: build/libs/myapp-1.0.0.jar
    health-check-type: http
    health-check-http-endpoint: /actuator/health
    buildpacks:
      - java_buildpack
    random-route: true
    services:
      - openweathermap
      - rabbitmq
```

As you can see, the manifest also binds the available service-instances `openweathermap` and `rabbitmq` to you app. 
 