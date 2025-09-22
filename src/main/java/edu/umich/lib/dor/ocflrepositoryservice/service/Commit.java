package edu.umich.lib.dor.ocflrepositoryservice.service;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;

public class Commit implements Command {
    private RepositoryClient repositoryClient;
    private String objectIdentifier;
    private Curator curator;
    private String message;

    public Commit(
        RepositoryClient repositoryClient,
        String objectIdentifier,
        Curator curator,
        String message
    ) {
        this.repositoryClient = repositoryClient;

        this.objectIdentifier = objectIdentifier;
        this.curator = curator;
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
    }

    public void execute() {
        repositoryClient.commitChanges(objectIdentifier, curator, message);
    }
}