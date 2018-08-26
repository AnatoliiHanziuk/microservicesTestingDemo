package example;

import example.user.User;
import example.user.UserRepository;
import example.weather.WeatherClient;
import example.weather.WeatherResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ExampleApplicationController.class)
public class ExampleControllerApiTest {

    private static final String LATITUDE = "50.0647";
    private static final String LONGTITUDE = "19.9450";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WeatherClient weatherClient;

    @Test
    public void shouldReturnHello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(content().string("Hello!"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldReturnFullName() throws Exception {
        User user = new User("Peter", "Pan");
        given(userRepository.findByLastName("Pan")).willReturn(Optional.of(user));

        mockMvc.perform(get("/user/Pan"))
                .andExpect(content().string("Hello Peter Pan!"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldReturnCurrentWeather() throws Exception {
        WeatherResponse weatherResponse = new WeatherResponse("Krakow, 15°C raining");
        given(weatherClient.fetchWeather(LATITUDE, LONGTITUDE)).willReturn(Optional.of(weatherResponse));

        mockMvc.perform(get(String.format("/weather/%s,%s", LATITUDE, LONGTITUDE)))
                .andExpect(content().string("Krakow, 15°C raining"))
                .andExpect(status().is2xxSuccessful());
    }
}
