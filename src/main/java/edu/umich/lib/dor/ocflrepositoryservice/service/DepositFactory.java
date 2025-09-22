package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class DepositFactory {
    RepositoryClient repositoryClient;
    Path depositPath;
    DepositDirectory depositDir;

    public DepositFactory(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir
    ) {
        this.repositoryClient = repositoryClient;
        this.depositDir = depositDir;
    }

    public Deposit create(
        Curator curator,
        String objectIdentifier,
        Path sourcePath,
        String message
    ) {
        return new Deposit(
            repositoryClient,
            depositDir,
            curator,
            objectIdentifier,
            sourcePath,
            message
        );
    }
}
