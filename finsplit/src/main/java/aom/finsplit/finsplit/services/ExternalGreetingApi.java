package aom.finsplit.finsplit.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aom.finsplit.finsplit.DTO.DTO2;

class ResponseFormat {
    private int temperature;

    public ResponseFormat(int temperature) {
        this.temperature = temperature;
    }

    public String getMessage() {
        if (temperature <= 0) {
            return "It's freezing! Don't forget your coat!";
        } else if (temperature <= 20) {
            return "It's a bit chilly, wear a jacket!";
        } else if (temperature <= 30) {
            return "It's a nice day, enjoy the weather!";
        }
        return "It's hot outside, stay hydrated!";
    }
}

@Service
public class ExternalGreetingApi {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://api.weatherapi.com/v1/current.json?key=KEY&q=CITY";
    String KEY = "478010c3e16a4807a9d64300250307";

    // This method is a placeholder for an external API call to get a greeting
    // message.
    public String getGreeting(String CITY) {
        try {
            String processedUrl = url.replace("KEY", KEY).replace("CITY", CITY);
            ResponseEntity<DTO2> res = restTemplate.exchange(processedUrl, HttpMethod.GET, null, DTO2.class);
            DTO2 resp = res.getBody();
            if (resp == null) {
                throw new Exception("No response from weather API");
            }
            System.out.println(resp);
            ResponseFormat response = new ResponseFormat((int) resp.getCurrent().getTempC());
            return response.getMessage();
        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return "Error fetching weather data. Please try again later.";
        }
    }

    public void generateDub(String messageToDub) throws JsonMappingException, JsonProcessingException {
        url = "https://api.murf.ai/v1/speech/generate";
        KEY = "ap2_50cbf394-db87-4163-8243-b40976e2bd76";

        // Constructing Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", KEY);

        // Constructing Request Body
        Map<String, String> body = new HashMap<>();
        body.put("text", messageToDub);
        body.put("voiceId", "en-US-natalie");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    new URI(url),
                    HttpMethod.POST,
                    request,
                    String.class);
            String audioUrl = new ObjectMapper().readTree(response.getBody()).get("audioFile").asText();
            System.out.println("Audio URL: " + audioUrl);
        } catch (RestClientException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}