package com.github.henribas.filemanager;

import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public interface FileManager {

    public String upload(MultipartFormDataInput input);

    public List<FileVO> listFiles();

}
