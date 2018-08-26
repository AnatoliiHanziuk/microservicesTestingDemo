package example;

import example.user.User;
import example.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import example.weather.WeatherClient;
import example.weather.WeatherResponse;

import java.util.Optional;

@RestController
public class ExampleApplicationController {

    private final UserRepository userRepository;
    private final WeatherClient weatherClient;

    @Autowired
    public ExampleApplicationController(final UserRepository userRepository, final WeatherClient weatherClient) {
        this.userRepository = userRepository;
        this.weatherClient = weatherClient;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/user/{lastName}")
    public String hello(@PathVariable final String lastName) {
        Optional<User> foundUser = userRepository.findByLastName(lastName);

        return foundUser
                .map(user -> String.format("Hello %s %s!", user.getFirstName(), user.getLastName()))
                .orElse(String.format("User with last name %s is not found", lastName));
    }

    @GetMapping("/weather/{latitude},{longtitude:.+}")
    public String getWeather(@PathVariable final String latitude, @PathVariable final String longtitude) {
        return weatherClient.fetchWeather(latitude, longtitude).map(WeatherResponse::getSummary)
                .orElse("Sorry, I couldn't fetch the weather for you :(");
    }


}
