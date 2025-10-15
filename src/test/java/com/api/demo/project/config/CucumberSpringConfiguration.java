package com.api.demo.project.config;

import com.api.demo.project.ApiDemoProjectApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = ApiDemoProjectApplication.class)
public class CucumberSpringConfiguration {
}
