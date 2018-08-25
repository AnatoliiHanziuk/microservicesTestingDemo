import models.User;
import models.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import weather.WeatherClient;
import weather.WeatherResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplicationControllerTest {

    private static final String LATITUDE = "50.0647";
    private static final String LONGTITUDE = "19.9450";

    private ApplicationController subject;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeatherClient weatherClient;

    @Before
    public void setUp() {
        initMocks(this);
        subject = new ApplicationController(userRepository, weatherClient);
    }

    @Test
    public void shouldGreet() {
        assertThat(subject.hello()).isEqualTo("Hello World!");
    }

    @Test
    public void shouldReturnFullNameOfUser() {
        User user = new User("Peter", "Pan");
        given(userRepository.findByLatName("Pan")).willReturn(Optional.of(user));

        String greeting = subject.hello("Pan");

        assertThat(greeting).isEqualTo("Hello Peter Pan!");
    }

    @Test
    public void shouldTellUserIsUNknown() {
        given(userRepository.findByLatName("Pan")).willReturn(Optional.empty());

        String greeting = subject.hello("Pan");

        assertThat(greeting).isEqualTo("User with last name Pan is not found");
    }

    @Test
    public void shouldReturnWeather() {
        WeatherResponse weatherResponse = new WeatherResponse("Krakow, 15°C raining");
        given(weatherClient.fetchWeather(LATITUDE, LONGTITUDE)).willReturn(Optional.of(weatherResponse));

        String weather = subject.getWeather(LATITUDE, LONGTITUDE);

        assertThat(weather).isEqualTo("Krakow, 15°C raining");
    }

    @Test
    public void shouldReturnLocationNotFoundError() {
        given(weatherClient.fetchWeather(LATITUDE, LONGTITUDE)).willReturn(Optional.empty());

        String weather = subject.getWeather(LATITUDE, LONGTITUDE);

        assertThat(weather).isEqualTo("Sorry, I couldn't fetch the weather for you :(");
    }
}
