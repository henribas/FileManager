package com.github.henribas.filemanager;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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
                .statusCode(Response.Status.OK.getStatusCode());
    }

    private MultiPartSpecification getMultiPart() {
        return new MultiPartSpecBuilder("This content will be inside the test.txt file".getBytes())
            .fileName("test.txt")
            .controlName("file")
            .mimeType("text/plain")
            .build();
    }

    @Test
    void testFileAlreadySent() {
        given()
            .multiPart(getMultiPart())
            .when().post("/api/file-manager/upload")
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(is("This file has already been uploaded."));
    }

    @Test
    void mustInformInput() {
        FileManager fileManager = new DefaultFileManager();
        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            fileManager.upload(null);
        });
    
        String expectedMessage = "Please select a file to upload.";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage) && Response.Status.BAD_REQUEST.getStatusCode() == exception.getResponse().getStatus());
    }

    @Test
    void fileSizeMustBeLessThan5MB() {
        given()
            .multiPart(getMultiPartBiggerThan5MB())
            .when().post("/api/file-manager/upload")
            .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(is("The file size must be less than 5MB."));
    }

    private MultiPartSpecification getMultiPartBiggerThan5MB() {
        byte[] sixMB = new byte[6 * 1024 * 1024];
        return new MultiPartSpecBuilder(sixMB)
            .fileName("book.txt")
            .controlName("file")
            .mimeType("text/plain")
            .build();
    }

}
