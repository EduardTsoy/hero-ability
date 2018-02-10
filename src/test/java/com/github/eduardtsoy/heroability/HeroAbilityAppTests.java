package com.github.eduardtsoy.heroability;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HeroAbilityApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class HeroAbilityAppTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/api/heros",
                String.class);
        assertThat(entity.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testHeroList() {
        ResponseEntity<String> entity = this.restTemplate
                .getForEntity("/api/heros", String.class);
        assertThat(entity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(entity.getBody(), equalTo("OK"));
    }

    @Test
    public void actuatorStatus() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/actuator/health",
                String.class);
        assertThat(entity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(entity.getBody(), equalTo("{\"status\":\"UP\"}"));
    }

}
