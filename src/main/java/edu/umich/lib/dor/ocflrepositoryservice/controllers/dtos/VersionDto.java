package edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Version;

public class VersionDto {
    private long number;
    private String message;
    private AgentDto agent;
    private String created;
    private boolean isStaged;

    public VersionDto(Version version) {
        this.number = version.number();
        this.message = version.message();
        this.agent = new AgentDto(version.agent());
        this.created = version.created().toString();
        this.isStaged = version.isStaged();
    }

    public long getNumber() {
        return number;
    }

    public String getMessage() {
        return message;
    }

    public AgentDto getAgent() {
        return agent;
    }

    public String getCreated() {
        return created;
    }

    public boolean getIsStaged() {
        return isStaged;
    }

}
