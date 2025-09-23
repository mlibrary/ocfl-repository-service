package edu.umich.lib.dor.ocflrepositoryservice.domain;

import java.util.List;

public class OcflObject {
    private String identifier;
    private List<Version> versions;

    public OcflObject(String identifier, List<Version> versions) {
        this.identifier = identifier;
        this.versions = versions;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Version> getVersions() {
        return versions;
    }

}
