#include <Adafruit_DHT.h>

#define DHTPIN 2
#define DHTTYPE DHT11
#define VALUES_SIZE 7
#define SEND_DEBUG false

system_tick_t nextIterationAt = 0;
system_tick_t delayMilliSeconds = 60000;
float celsiusValues[VALUES_SIZE];
float humidityValues[VALUES_SIZE];
char deviceName[16];
int currentIndex = 0;

DHT dht(DHTPIN, DHTTYPE);

void handler(const char *topic, const char *name) {
    snprintf(deviceName, sizeof(deviceName), "%s", name);
    Particle.publish("startup", deviceName, PRIVATE);
}

int compare(const void *a, const void *b) {
    return (*(float *)a - *(float *)b);
}

float getMedian(float (*arr)[VALUES_SIZE]) {
    float sortedArray[VALUES_SIZE];
    for (int n = 0; n < VALUES_SIZE; n++) {
        sortedArray[n] = (*arr)[n];
    }
    qsort(sortedArray, VALUES_SIZE, sizeof(int), compare);
    return sortedArray[VALUES_SIZE / 2];
}

void setup() {
    Particle.subscribe("particle/device/name", handler, MY_DEVICES);
    Particle.publish("particle/device/name", PRIVATE);
    dht.begin();

    for (int n = 0; n < VALUES_SIZE; n++) {
        celsiusValues[n] = 255;
        humidityValues[n] = 255;
    }
}

void loop() {
    if (nextIterationAt == 0 || millis() > nextIterationAt) {
        float celsius = dht.getTempCelcius();
        float humidity = dht.getHumidity();

        if (isnan(celsius) || celsius == 255 || isnan(humidity) || humidity == 255) {
            return;
        }

        nextIterationAt = millis() + delayMilliSeconds;

        celsiusValues[currentIndex % VALUES_SIZE] = celsius;
        humidityValues[currentIndex % VALUES_SIZE] = humidity;
        currentIndex = (currentIndex + 1) % VALUES_SIZE;

        float celsiusMedian = getMedian(&celsiusValues);
        float humidityMedian = getMedian(&humidityValues);
        if (SEND_DEBUG) {
            Particle.publish("debug", String::format("{\"celsius\":%.2f, \"humidity\":%.2f, \"celsiusMedian\":%.2f, \"humidityMedian\":%.2f, \"celsiusValues\" : [ %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f ], \"humidityValues\" : [ %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f ]}", celsius, humidity, celsiusMedian, humidityMedian, celsiusValues[0], celsiusValues[1], celsiusValues[2], celsiusValues[3], celsiusValues[4], celsiusValues[5], celsiusValues[6], humidityValues[0], humidityValues[1], humidityValues[2], humidityValues[3], humidityValues[4], humidityValues[5], humidityValues[6]), PRIVATE);
        }

        for (int n = 0; n < VALUES_SIZE; n++) {
            if (celsiusValues[n] == 255) {
                nextIterationAt -= (delayMilliSeconds * 0.85);
                return;
            }
        }

        Particle.publish("weather-data", String::format("{\"celsius\":%.2f, \"humidity\":%.2f, \"location\":\"%s\", \"coordinatesLatitude\": 46.98, \"coordinatesLongitude\": 7.48}", celsiusMedian, humidityMedian, deviceName), PRIVATE);
    }
}