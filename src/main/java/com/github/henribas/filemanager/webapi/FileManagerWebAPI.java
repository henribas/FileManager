package com.github.henribas.filemanager.webapi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.github.henribas.filemanager.FileManager;

@Path("/api/file-manager")
@RequestScoped
public class FileManagerWebAPI {

	@Inject
	FileManager fileManager;

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response upload(@MultipartForm MultipartFormDataInput input) {
		try {

			return Response.ok().entity(fileManager.upload(input)).build();

		} catch (WebApplicationException e) {
			return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
		}
	}

}
