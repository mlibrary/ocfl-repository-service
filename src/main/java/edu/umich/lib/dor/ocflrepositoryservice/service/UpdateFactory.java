package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;

public class UpdateFactory {
    private RepositoryClient repositoryClient;
    private DepositDirectory depositDir;

    public UpdateFactory(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir
    ) {
        this.repositoryClient = repositoryClient;
        this.depositDir = depositDir;
    }

    public Update create(
        String objectIdentifier,
        Path sourcePath,
        Agent agent,
        String message
    ) {
        return new Update(
            repositoryClient,
            depositDir,
            objectIdentifier,
            sourcePath,
            agent,
            message
        );
    }
}
