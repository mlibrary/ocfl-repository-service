package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.exception.EntityAlreadyExistsException;

public class Deposit implements Command {

    private Curator curator;
    private String objectIdentifier;
    String message;

    RepositoryClient repositoryClient;
    Package sourcePackage;

    public Deposit(
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

        boolean objectExists = this.repositoryClient.hasObject(objectIdentifier);

        if (objectExists) {
            throw new EntityAlreadyExistsException(
                String.format(
                    "An object with identifier \"%s\" already exists.",
                    objectIdentifier
                )
            );
        }

        this.sourcePackage = depositDir.getPackage(sourcePath);
    }

    public void execute() {
        repositoryClient.createObject(
            objectIdentifier, sourcePackage, curator, message
        );
    }
}
