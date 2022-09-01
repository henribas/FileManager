package com.github.henribas.filemanager;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public interface FileManager {

    public String upload(MultipartFormDataInput input);

}
