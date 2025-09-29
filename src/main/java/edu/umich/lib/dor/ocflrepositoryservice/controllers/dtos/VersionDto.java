package edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Version;

public class VersionDto {
    private long number;
    private String message;
    private CuratorDto curator;
    private String created;
    private boolean isStaged;

    public VersionDto(Version version) {
        this.number = version.number();
        this.message = version.message();
        this.curator = new CuratorDto(version.curator());
        this.created = version.created().toString();
        this.isStaged = version.isStaged();
    }

    public long getNumber() {
        return number;
    }

    public String getMessage() {
        return message;
    }

    public CuratorDto getCurator() {
        return curator;
    }

    public String getCreated() {
        return created;
    }

    public boolean getIsStaged() {
        return isStaged;
    }

}
