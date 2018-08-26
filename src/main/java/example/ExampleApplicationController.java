package example;

import example.user.User;
import example.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello!";
    }

    @RequestMapping(value = "/user/{lastName}", method = RequestMethod.GET)
    public String hello(@PathVariable final String lastName) {
        Optional<User> foundUser = userRepository.findByLastName(lastName);

        return foundUser
                .map(user -> String.format("Hello %s %s!", user.getFirstName(), user.getLastName()))
                .orElse(String.format("User with last name %s is not found", lastName));
    }

    @RequestMapping(value = "/weather/{latitude},{longtitude:.+}", method = RequestMethod.GET)
    public String getWeather(@PathVariable final String latitude, @PathVariable final String longtitude) {
        return weatherClient.fetchWeather(latitude, longtitude).map(WeatherResponse::getSummary)
                .orElse("Sorry, I couldn't fetch the weather for you :(");
    }


}
