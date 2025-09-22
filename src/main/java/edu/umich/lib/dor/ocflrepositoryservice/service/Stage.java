package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public class Stage implements Command {
    private RepositoryClient repositoryClient;
    private Package sourcePackage;

    private String objectIdentifier;
    private Curator curator;
    private String message;

    public Stage(
        RepositoryClient repositoryClient,
        DepositDirectory depositDir,
        String objectIdentifier,
        Path sourcePath,
        Curator curator,
        String message
    ) {
        this.repositoryClient = repositoryClient;

        this.objectIdentifier = objectIdentifier;
        this.curator = curator;
        this.message = message;

        this.sourcePackage = depositDir.getPackage(sourcePath);
    }

    public void execute() {
        repositoryClient.stageChanges(
            objectIdentifier, sourcePackage, curator, message
        );
    }
}