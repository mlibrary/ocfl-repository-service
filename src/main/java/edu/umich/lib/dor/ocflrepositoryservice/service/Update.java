package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;

public class Update implements Command {
    private Curator curator;
    private String objectIdentifier;
    private String message;
    private RepositoryClient repositoryClient;
    private Package sourcePackage;

    public Update(
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

        boolean objectExists = repositoryClient.hasObject(objectIdentifier);
        if (!objectExists) {
            throw new NoEntityException(
                String.format(
                    "No object with identifier \"%s\" was found.",
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
            curator,
            message
        );
    }
}
