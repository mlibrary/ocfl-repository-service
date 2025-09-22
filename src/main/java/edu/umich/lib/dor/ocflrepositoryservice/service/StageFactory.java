package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class StageFactory {
    RepositoryClient repositoryClient;
    Path depositPath;
    DepositDirectory depositDir;

    public StageFactory(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir
    ) {
        this.repositoryClient = repositoryClient;
        this.depositDir = depositDir;
    }

    public Stage create(
        Curator curator,
        String objectIdentifier,
        Path sourcePath,
        String message
    ) {
        return new Stage(
            repositoryClient,
            depositDir,
            objectIdentifier,
            sourcePath,
            curator,
            message
        );
    }
}
