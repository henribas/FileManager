package com.github.henribas.filemanager.webapi;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;

@QuarkusTest
class FileManagerWebAPITest {

    @Test
    void testUploadFile() {
        given()
            .multiPart(getMultiPart())
            .when().post("/api/file-manager/upload")
            .then()
                .statusCode(200);
    }

    private MultiPartSpecification getMultiPart() {
        return new MultiPartSpecBuilder("Test-Content-In-File".getBytes())
            .fileName("book.txt")
            .controlName("file")
            .mimeType("text/plain")
            .build();
    }

}