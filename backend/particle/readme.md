# IoT
This example isn't really part of the workshop but the consumer-app also exposes a [REST endpoint](../consumer/src/main/java/com/swisscom/cloud/controllers/rest/WeatherDataController.java) to _post_ messages to.

## Particle
If you're in possession of a [Particle Argon](https://docs.particle.io/argon/) together with a [Grove shield](https://www.seeedstudio.com/Grove-Shield-for-Particle-Mesh-p-4080.html) and a temperature- and humidity-[sensor](http://wiki.seeedstudio.com/Grove-TemperatureAndHumidity_Sensor/), you can flash the code from this directory to the Argon to instruct it to periodically send temperature- and humidity-data to the [Particle Cloud](https://www.particle.io/device-cloud/). 

## Webhook
The code itself only ensures that the data ends up in the Particle Cloud. How the measurements are forwarded from there to the consumer app is configured in the Console using a [Webhook](https://console.particle.io/integrations/webhooks).
Use this template to forward all events of type `weather-data` to the consumer-app (don't forget to insert the actual URI):
```json
{
    "event": "weather-data",
    "url": "https://<consumer-app>.scapp.io/api/weather-data",
    "requestType": "POST",
    "noDefaults": false,
    "rejectUnauthorized": true,
    "headers": {
        "content-type": "application/json"
    },
    "body": "{ \"value\": {{{PARTICLE_EVENT_VALUE}}}, \"assetId\": \"{{{PARTICLE_DEVICE_ID}}}\", \"publishedAt\": \"{{{PARTICLE_PUBLISHED_AT}}}\" }"
}
```

## Kibana Dashboard
Import the dashboard in Kibana: `Management > Kibana > Saved Object > Import`, pick `dashboard-particle.ndjson` from this [subfolder](./kibana).