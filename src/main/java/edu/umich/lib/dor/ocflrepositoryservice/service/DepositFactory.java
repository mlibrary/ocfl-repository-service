package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class DepositFactory {
    private RepositoryClient repositoryClient;
    private DepositDirectory depositDir;

    public DepositFactory(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir
    ) {
        this.repositoryClient = repositoryClient;
        this.depositDir = depositDir;
    }

    public Deposit create(
        String objectIdentifier,
        Path sourcePath,
        Curator curator,
        String message
    ) {
        return new Deposit(
            repositoryClient,
            depositDir,
            objectIdentifier,
            sourcePath,
            curator,
            message
        );
    }
}
