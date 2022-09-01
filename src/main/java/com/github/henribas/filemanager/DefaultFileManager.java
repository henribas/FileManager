package com.github.henribas.filemanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
class DefaultFileManager implements FileManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultFileManager.class);

    private static final long MAX_FILE_SIZE_IN_BYTES = 5242880l; // 5MB = 5 * 1024 * 1024

    @ConfigProperty(name = "app.directory")
	String appDirectory;

    @Override
    public String upload(MultipartFormDataInput input) throws WebApplicationException {
        validateInput(input);

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<String> fileNames = new ArrayList<>();
        List<InputPart> inputParts = uploadForm.get("file");
        String fileName = null;
        StringBuilder fullPathFileName = new StringBuilder();

        for (InputPart inputPart : inputParts) {
            try {
                fileName = getFileName(inputPart);
                fileNames.add(fileName);

                byte[] bytes = IOUtils.toByteArray(inputPart.getBody(InputStream.class, null));
                validateFileSize(bytes);

                fullPathFileName.append(getDefaultDirectory().getAbsolutePath()).append(File.separator).append(fileName);
                Files.write(Paths.get(fullPathFileName.toString()), bytes, StandardOpenOption.CREATE_NEW);
            } catch(FileAlreadyExistsException e) {
                log.error(e.getMessage(), e);
                throw new WebApplicationException("This file has already been uploaded.", Response.Status.BAD_REQUEST);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new WebApplicationException("There was an error saving the file to the server.", Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        
        return "All files: " + String.join(", ", fileNames) + " have been successfully uploaded.";
    }

    private File getDefaultDirectory() {
        File directory = new File(appDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    private String getFileName(InputPart inputPart) throws WebApplicationException {
		String[] contentDisposition = inputPart.getHeaders().getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				return name[1].trim().replace("\"", "");
			}
		}
		throw new WebApplicationException("Invalid filename.", Response.Status.BAD_REQUEST);
	}

    private void validateInput(MultipartFormDataInput input) throws WebApplicationException {
        if (input == null) {
            throw new WebApplicationException("Please select a file to upload.", Response.Status.BAD_REQUEST);
        }
    }

    private void validateFileSize(byte[] bytes) {
        if (bytes.length > MAX_FILE_SIZE_IN_BYTES) {
            throw new WebApplicationException("The file size must be less than 5MB.", Response.Status.BAD_REQUEST);
        }
    }
    
}
