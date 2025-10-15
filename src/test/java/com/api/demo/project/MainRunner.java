package com.api.demo.project;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/com/api/demo/project/cucumber/features",
        glue = {"com.api.demo.project.cucumber", "com.api.demo.project.config"},
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber-report.json",
        },
        tags = ""
)
public class MainRunner {
}
