package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class UpdateFactory {
    RepositoryClient repositoryClient;
    Path depositPath;
    DepositDirectory depositDir;

    public UpdateFactory(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir
    ) {
        this.repositoryClient = repositoryClient;
        this.depositDir = depositDir;
    }

    public Update create(
        Curator curator,
        String objectIdentifier,
        Path sourcePath,
        String message
    ) {
        return new Update(
            repositoryClient,
            depositDir,
            curator,
            objectIdentifier,
            sourcePath,
            message
        );
    }
}
