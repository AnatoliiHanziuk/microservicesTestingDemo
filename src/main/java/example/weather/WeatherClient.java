package example.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class WeatherClient {
    private final RestTemplate restTemplate;
    private final String weatherUrl;
    private final String weatherServiceApiKey;

    @Autowired
    public WeatherClient(final RestTemplate restTemplate,
                         @Value("${weather.url}") final String weatherUrl,
                         @Value("${weather.api_secret}") final String weatherServiceApiKey) {
        this.restTemplate = restTemplate;
        this.weatherUrl = weatherUrl;
        this.weatherServiceApiKey = weatherServiceApiKey;
    }

    public Optional<WeatherResponse> fetchWeather(final String latitude, final String longitude) {
        String url = String.format("%s/%s/%s,%s", weatherUrl, weatherServiceApiKey, latitude, longitude);

        return Optional.ofNullable(restTemplate.getForObject(url, WeatherResponse.class));
    }

}
