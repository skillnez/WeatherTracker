package integtation;

import com.skillnez.weathertracker.config.TestConfig;
import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.WeatherApiResponseDto;
import com.skillnez.weathertracker.exception.ApiException;
import com.skillnez.weathertracker.service.WeatherService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WeatherApiServiceTest {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private MockWebServer mockWebServer;

    private static final String TEST_CITY = "Moscow";
    private static final String TEST_COUNTRY = "RU";
    private static final BigDecimal TEST_LATITUDE = BigDecimal.valueOf(55.7504461);
    private static final BigDecimal TEST_LONGITUDE = BigDecimal.valueOf(37.6174943);

    @Test
    void makeGeoCodingRequestByName() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [
                        	{
                        		"name": "Moscow",
                        		"local_names": {
                        			"en": "Moscow"
                        		},
                        		"lat": 55.7504461,
                        		"lon": 37.6174943,
                        		"country": "RU",
                        		"state": "Moscow"
                        	},
                        	{
                        		"name": "Moscow",
                        		"local_names": {
                        			"en": "Moscow"
                        		},
                        		"lat": 46.7323875,
                        		"lon": -117.0001651,
                        		"country": "US",
                        		"state": "Idaho"
                        	},
                        	{
                        		"name": "Moscow",
                        		"lat": 45.071096,
                        		"lon": -69.891586,
                        		"country": "US",
                        		"state": "Maine"
                        	},
                        	{
                        		"name": "Moscow",
                        		"lat": 35.0619984,
                        		"lon": -89.4039612,
                        		"country": "US",
                        		"state": "Tennessee"
                        	},
                        	{
                        		"name": "Moscow",
                        		"lat": 39.5437014,
                        		"lon": -79.0050273,
                        		"country": "US",
                        		"state": "Maryland"
                        	}
                        ]
                        """).setHeader("Content-Type", "application/json"));
        List<LocationResponseDto> locationResponseDtoList = weatherService.getGeoByCityName(TEST_CITY);
        assertEquals(TEST_CITY, locationResponseDtoList.getFirst().getName());
        assertEquals(5, locationResponseDtoList.size());
    }

    @Test
    void makeWeatherRequestByCoordinates() {
        mockWebServer.enqueue(new MockResponse().setBody("""
                {
                	"coord": {
                		"lon": 37.6187,
                		"lat": 55.7487
                	},
                	"weather": [
                		{
                			"id": 803,
                			"main": "Clouds",
                			"description": "broken clouds",
                			"icon": "04d"
                		}
                	],
                	"base": "stations",
                	"main": {
                		"temp": 21.9,
                		"feels_like": 20.93,
                		"temp_min": 21.29,
                		"temp_max": 21.9,
                		"pressure": 1013,
                		"humidity": 30,
                		"sea_level": 1013,
                		"grnd_level": 995
                	},
                	"visibility": 10000,
                	"wind": {
                		"speed": 2.5,
                		"deg": 324,
                		"gust": 5.64
                	},
                	"clouds": {
                		"all": 59
                	},
                	"dt": 1749056696,
                	"sys": {
                		"type": 2,
                		"id": 2094500,
                		"country": "RU",
                		"sunrise": 1748998230,
                		"sunset": 1749060331
                	},
                	"timezone": 10800,
                	"id": 524901,
                	"name": "Moscow",
                	"cod": 200
                }
                """).setHeader("Content-Type", "application/json"));
        WeatherApiResponseDto weatherApiResponseDto = weatherService.getWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE);
        assertNotNull(weatherApiResponseDto);
        assertEquals(TEST_COUNTRY, weatherApiResponseDto.getCountry());
    }

    @Test
    void shouldThrowApiExceptionOn400StatusCode() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        assertThrows(ApiException.class, () ->
                weatherService.getWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE));
    }

    @Test
    void shouldThrowApiExceptionOn500StatusCode() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        assertThrows(ApiException.class, () ->
                weatherService.getWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE));
    }

    @AfterAll
    void tearDownMockServer() throws IOException {
        mockWebServer.shutdown();
    }


}
