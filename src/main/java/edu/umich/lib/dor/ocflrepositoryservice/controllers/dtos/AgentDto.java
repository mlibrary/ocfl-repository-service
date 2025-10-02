package edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;

public class AgentDto {
    private String username;
    private String email;

    public AgentDto(Agent agent) {
        this.username = agent.username();
        this.email = agent.email();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
