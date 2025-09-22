package edu.umich.lib.dor.ocflrepositoryservice.service;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class CommitFactory {
    private RepositoryClient repositoryClient;

    public CommitFactory(RepositoryClient repositoryClient) {
        this.repositoryClient = repositoryClient;
    }

    public Commit create(String objectIdentifier, Curator curator, String message) {
        return new Commit(repositoryClient, objectIdentifier, curator, message);
    }
}
