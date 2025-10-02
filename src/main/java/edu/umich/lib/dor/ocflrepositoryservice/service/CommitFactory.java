package edu.umich.lib.dor.ocflrepositoryservice.service;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;

public class CommitFactory {
    private RepositoryClient repositoryClient;

    public CommitFactory(RepositoryClient repositoryClient) {
        this.repositoryClient = repositoryClient;
    }

    public Commit create(String objectIdentifier, Agent agent, String message) {
        return new Commit(repositoryClient, objectIdentifier, agent, message);
    }
}
