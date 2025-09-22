package edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos;

public class ObjectDto {
    private String identifier;

    public ObjectDto(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}