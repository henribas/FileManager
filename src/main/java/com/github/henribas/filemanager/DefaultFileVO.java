package com.github.henribas.filemanager;

import java.net.URI;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public final class DefaultFileVO implements FileVO {

    private String name;
    private String url;
    private String absolutePath;
    private long sizeInBytes;

    private DefaultFileVO(String name, String url, String absolutePath, long sizeInBytes) {
        this.name = name;
        this.url = url;
        this.absolutePath = absolutePath;
        this.sizeInBytes = sizeInBytes;
    }

    static FileVO of(String name, URI uri, String absolutePath, long sizeInBytes) {
        return new DefaultFileVO(name, uri.toString(), absolutePath, sizeInBytes);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public String toString() {
        return "DefaultFileVO [absolutePath=" + absolutePath + ", name=" + name + ", sizeInBytes=" + sizeInBytes
                + ", url=" + url + "]";
    }

}
