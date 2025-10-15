# API Demo Project (Cucumber + Spring + Rest Assured)

Base project for testing the API

[API-DOCUMENTATION](https://prosurgical-billi-devastatingly.ngrok-free.dev/api-docs/#/)

---
## 1. Prerequisites
Install / have available:
- Java 17 (run `java -version` to verify)
- Maven 3.8+ (run `mvn -v` to verify)

---
## 2. Project Structure

```
api-demo-project/
  pom.xml                                             -> Maven build + dependencies
  src/main/resources/application.properties           -> Base config (URL, endpoints, payload path)
  src/main/java/.../helpers/PayloadBuilder.java       -> Reads JSON payload files
  src/main/java/.../storage/MainResponseStorage.java  -> Shared data holder during a run
  src/test/java/.../cucumber/features/*.feature       -> Your test scenarios
  src/test/java/.../cucumber/steps/*.java             -> Step definition methods
```
---
## 3. Configuration Values
`src/main/resources/application.properties` contains:
```
PAYLOAD_PATH=src/main/java/com/api/demo/project/payloads/
URL=... (base API URL)
TOKEN=/get-token
USERS=/users
USERS_BY_ID=/users/<user_id>
```
Change the base `URL` and endpoints here if the API changes.

---
## 4. Feature Files
Location: `src/test/java/.../cucumber/features/`
Example provided:
```
Scenario: Example loading the payload
  Given I prepare the request payload "auth.json"
```
You can add steps like:
```
When I send the request to the users endpoint
Then the response status code should be 200
And the response body should contain "john"
```
---
## 5. Payload Files
Place JSON payload files in the folder set by `PAYLOAD_PATH` (default: `src/main/java/com/api/demo/project/payloads/`).
Example: `auth.json` could contain a login body:
```
{
  "username": "demo",
  "password": "secret"
}
```
The step `Given I prepare the request payload "auth.json"` loads it into shared storage (`mainResponseStorage.setPayload(...)`).

---
## 6. Shared Storage (Passing Data Between Steps)
`MainResponseStorage` holds values for the duration of a test run, if you run multiple scenarios it will hold all the data. Common uses:
- Save an auth token after login
- Store created resource IDs
- Keep the last request payload

Example usage inside a step method:
```java
@Autowired
private MainResponseStorage mainResponseStorage;

mainResponseStorage.setBearerToken(token);
String id = store.getUserId();
```

---
## 7. Adding New Step Definitions

Basic template for sending a POST with the prepared payload:
```java
@When("I create a user")
public void iCreateAUser() {
  String body = store.getPayload();
  Response response = RestAssured
      .given()
        .baseUri(baseUrl) // baseUrl injected or read from properties
        .header("Content-Type", "application/json")
        .body(body)
      .when()
        .post(USERS_ENDPOINT);

  // Optionally save data
  store.setUserId(response.jsonPath().getString("id"));
}
```
(You would need to inject config or read from properties—see next section.)

---
## 8. Accessing Config Values in Steps
Create a field and use Spring's `@Value`:
```java
@Value("${URL}")
private String baseUrl;

@Value("${USERS}")
private String usersEndpoint;
```
Then build full URL: `baseUrl + usersEndpoint`.
If the endpoint has a placeholder (like `<user_id>`), replace it:
```java
String byId = usersByIdEndpoint.replace("<user_id>", store.getUserId());
```

---
## 9. Validating Responses
With Rest Assured directly:
```java
response.then().statusCode(200);
String name = response.jsonPath().getString("name");
Assert.assertEquals("john", name);
```
Or wrap via `RestResponse` (already included) to get:
```java
RestResponse<?> rest = new RestResponse<>(Object.class, response);
boolean ok = rest.isSuccessful(); // true if 2xx (200-205)
```

---
## 10. Common Tasks Cheat Sheet
| Task | Action |
|------|--------|
| Run all tests | `mvn test` |
| Run tagged tests | `mvn test -Dcucumber.filter.tags="@tag"` |
| Clean build | `mvn clean test` |
| Change base URL | Edit `application.properties` or use `-DURL=...` |
| Add payload | Drop JSON into payload folder |
| Undefined step error | Copy suggested snippet into a step class |
| See report | Open `target/cucumber-reports/cucumber-report.json` (use a plugin / online viewer) |

---
## 11. Troubleshooting
| Problem | Likely Cause | Fix |
|---------|--------------|-----|
| File not found when loading payload | Wrong filename or path | Confirm file name & location matches `PAYLOAD_PATH` |
| Undefined step | Step text has no matching Java method | Implement method in steps package |
| 401 / Unauthorized | Missing or expired token | Add login step first, store token, add header in subsequent requests |
| Property not found | Typo in `@Value` key | Match exactly the key in `application.properties` |
| Build fails on dependencies | No internet / blocked repo | Check network, retry `mvn -U clean test` |

---
## 12. Quick Start Summary
```
# 1. Ensure Java + Maven installed
java -version
mvn -v

# 2. Add a new payload
#   Put myPayload.json into payload folder
#   Add step: Given I prepare the request payload "myPayload.json"

# 3. Implement new steps
```

You're ready to extend automated API tests with minimal Java changes. Happy testing!