package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

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
        Curator curator,
        String message
    ) {
        return new Update(
            repositoryClient,
            depositDir,
            objectIdentifier,
            sourcePath,
            curator,
            message
        );
    }
}
