package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;

public class Stage implements Command {
    private RepositoryClient repositoryClient;
    private Package sourcePackage;

    private String objectIdentifier;
    private Agent agent;
    private String message;

    public Stage(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir,
        String objectIdentifier,
        Path sourcePath,
        Agent agent,
        String message
    ) {
        this.repositoryClient = repositoryClient;

        this.objectIdentifier = objectIdentifier;
        this.agent = agent;
        this.message = message;

        this.sourcePackage = depositDir.getPackage(sourcePath);
    }

    public void execute() {
        repositoryClient.stageChanges(
            objectIdentifier, sourcePackage, agent, message
        );
    }
}