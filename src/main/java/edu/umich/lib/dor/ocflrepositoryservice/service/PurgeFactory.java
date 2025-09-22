package edu.umich.lib.dor.ocflrepositoryservice.service;

public class PurgeFactory {
    private RepositoryClient repositoryClient;

    public PurgeFactory(RepositoryClient repositoryClient) {
        this.repositoryClient = repositoryClient;
    }

    public Purge create(String packageIdentifier) {
        return new Purge(repositoryClient, packageIdentifier);
    }
}
