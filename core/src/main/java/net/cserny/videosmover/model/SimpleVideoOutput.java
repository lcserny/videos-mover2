package net.cserny.videosmover.model;

public class SimpleVideoOutput {
    private String name;
    private Integer year;
    private String output;

    public SimpleVideoOutput(String name, Integer year, String output) {
        this.name = name;
        this.year = year;
        this.output = output;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public String getOutput() {
        return output;
    }
}
