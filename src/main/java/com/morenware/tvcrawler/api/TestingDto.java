package com.morenware.tvcrawler.api;

/**
 * Created by david on 16/05/15.
 */
public class TestingDto {

    private String testValue;
    private FileTestDto file;

    public String getTestValue() {
        return testValue;
    }

    public FileTestDto getFile() {
        return file;
    }

    public void setFile(FileTestDto file) {
        this.file = file;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;

    }
}
