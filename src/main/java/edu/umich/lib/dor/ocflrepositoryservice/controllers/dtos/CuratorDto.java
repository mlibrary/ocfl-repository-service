package edu.umich.lib.dor.ocflrepositoryservice.controllers.dtos;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class CuratorDto {
    private String username;
    private String email;

    public CuratorDto(Curator curator) {
        this.username = curator.username();
        this.email = curator.email();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
