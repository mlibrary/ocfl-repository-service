package edu.umich.lib.dor.ocflrepositoryservice.service;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;

public class Commit implements Command {
    private RepositoryClient repositoryClient;
    private String objectIdentifier;
    private Agent agent;
    private String message;

    public Commit(
        RepositoryClient repositoryClient,
        String objectIdentifier,
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
    }

    public void execute() {
        repositoryClient.commitChanges(objectIdentifier, agent, message);
    }
}