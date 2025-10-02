package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;

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
        Agent agent,
        String message
    ) {
        return new Deposit(
            repositoryClient,
            depositDir,
            objectIdentifier,
            sourcePath,
            agent,
            message
        );
    }
}
