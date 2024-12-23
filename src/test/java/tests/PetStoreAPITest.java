package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetStoreAPITest {

    private static final String BASE_URL = ConfigReader.getPetBaseUrl();

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test(testName = "CREATE (POST) Pet Test - Positive Scenario")
    public void testCreateNewPet() {
        Map<String, Object> petPayload = new HashMap<>();
        petPayload.put("id", 10);
        petPayload.put("name", "Fluffy");

        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "Dogs");
        petPayload.put("category", category);

        petPayload.put("status", "available");

        given()
                .contentType(ContentType.JSON)
                .body(petPayload)
                .when()
                .post("/pet")
                .then()
                .log().all() // loglama işlemi
                .statusCode(200)
                .body("name", equalTo("Fluffy"))
                .body("status", equalTo("available"));
    }

    @Test(testName = "CREATE (POST) Pet Test - Negative Scenario (Invalid Input)")
    public void testCreatePetWithInvalidData() {
        Map<String, Object> invalidPayload = new HashMap<>();
        invalidPayload.put("id", "invalid");

        given()
                .contentType(ContentType.JSON)
                .body(invalidPayload)
                .when()
                .post("/pet")
                .then()
                .log().all()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Test(testName = "READ (GET) Pet Test - Positive Scenario")
    public void testGetPetById() {
        // create new pet
        int petId = 10;
        given()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("name", equalTo("Fluffy"));
    }

    @Test(testName = "READ (GET) Pet Test - Negative Scenario (Non-existent Pet)")
    public void testGetNonExistentPet() {
        int nonExistentPetId = 999999;

        try {
            given()
                    .pathParam("petId", nonExistentPetId)
                    .when()
                    .get("/pet/{petId}")
                    .then()
                    .statusCode(404);
        } catch (Exception e) {
            System.err.println("Expected status code 404, but got not an exception");
        }
    }

    @Test(testName = "UPDATE (PUT) Pet Test - Positive Scenario")
    public void testUpdatePet() {
        Map<String, Object> updatedPetPayload = new HashMap<>();
        updatedPetPayload.put("id", 10);
        updatedPetPayload.put("name", "FluffyUpdated");
        updatedPetPayload.put("status", "sold");

        given()
                .contentType(ContentType.JSON)
                .body(updatedPetPayload)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .body("name", equalTo("FluffyUpdated"))
                .body("status", equalTo("sold"));
    }

    @Test(testName = "UPDATE (PUT) Pet Test - Negative Scenario (Invalid Update)")
    public void testUpdatePetWithInvalidData() {
        Map<String, Object> invalidUpdatePayload = new HashMap<>();
        invalidUpdatePayload.put("id", "invalid");

        given()
                .contentType(ContentType.JSON)
                .body(invalidUpdatePayload)
                .when()
                .put("/pet")
                .then()
                .statusCode(400);
    }

    @Test(testName = "DELETE Pet Test - Positive Scenario")
    public void testDeletePet() {
        int petId = 10;

        given()
                .pathParam("petId", petId)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(200);
    }

    @Test(testName = "DELETE Pet Test - Negative Scenario (Non-existent Pet)")
    public void testDeleteNonExistentPet() {
        int nonExistentPetId = 999999;

        given()
                .pathParam("petId", nonExistentPetId)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(anyOf(is(404), is(400)));
    }

    @Test(testName = "Find Pets by Status Test - Positive Scenario")
    public void testFindPetsByStatus() {
        String status = "available";

        given()
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("status", everyItem(equalTo(status)));
    }
}
