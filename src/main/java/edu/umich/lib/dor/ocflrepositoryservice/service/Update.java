package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;
import edu.umich.lib.dor.ocflrepositoryservice.exception.ObjectHasChangesException;

public class Update implements Command {
    private Agent agent;
    private String objectIdentifier;
    private String message;
    private RepositoryClient repositoryClient;
    private Package sourcePackage;

    public Update(
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

        boolean objectExists = repositoryClient.hasObject(objectIdentifier);
        if (!objectExists) {
            throw new NoEntityException(
                String.format(
                    "No object with identifier \"%s\" was found.",
                    objectIdentifier
                )
            );
        }

        boolean hasChanges = repositoryClient.hasChanges(objectIdentifier);
        if (hasChanges) {
            throw new ObjectHasChangesException(
                String.format(
                    "Object with identifier \"%s\" already has staged changes.",
                    objectIdentifier
                )
            );
        }

        this.sourcePackage = depositDir.getPackage(sourcePath);
    }

    public void execute() {
        repositoryClient.updateObjectFiles(
            objectIdentifier,
            sourcePackage,
            agent,
            message
        );
    }
}
