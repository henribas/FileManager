package com.github.henribas.filemanager;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public final class FileResponse {
    private String mensagem;
    private List<FileVO> files;

    private FileResponse(String mensagem, List<FileVO> files) {
        this.mensagem = mensagem;
        this.files = files;
    }

    static FileResponse of(String mensagem, List<FileVO> files) {
        return new FileResponse(mensagem, files);
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<FileVO> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "FileResponse [files=" + files + ", mensagem=" + mensagem + "]";
    }

}
