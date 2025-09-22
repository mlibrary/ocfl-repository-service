package edu.umich.lib.dor.ocflrepositoryservice.service;

import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;

public class Purge implements Command {
    private String objectIdentifier;
    private RepositoryClient repositoryClient;

    public Purge(
        RepositoryClient repositoryClient,
        String objectIdentifier
    ) {
        this.repositoryClient = repositoryClient;
        this.objectIdentifier = objectIdentifier;

        Boolean objectExists = repositoryClient.hasObject(objectIdentifier);
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
        repositoryClient.purgeObject(objectIdentifier);
    }
}
