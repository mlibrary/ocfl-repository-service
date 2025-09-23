package edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos;

import java.util.List;

import edu.umich.lib.dor.ocflrepositoryservice.domain.OcflObject;

public class OcflObjectDto {
    private String identifier;
    private List<VersionDto> versions;

    public OcflObjectDto(OcflObject ocflObject) {
        this.identifier = ocflObject.getIdentifier();
        this.versions = ocflObject.getVersions()
            .stream()
            .map(version -> { return new VersionDto(version); })
            .toList();
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<VersionDto> getVersions() {
        return versions;
    }

}