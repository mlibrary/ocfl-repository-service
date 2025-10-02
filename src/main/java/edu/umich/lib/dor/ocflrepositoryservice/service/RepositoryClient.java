package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;
import java.util.List;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.domain.Version;

public interface RepositoryClient {
    RepositoryClient createObject(String id, Package sourcePackage, Agent agent, String message);

    RepositoryClient readObject(String id, Path outputPath);

    RepositoryClient purgeObject(String id);

    boolean hasObject(String id);

    RepositoryClient deleteObjectFile(
        String objectId, String filePath, Agent agent, String message
    );

    RepositoryClient updateObjectFiles(
        String objectId, Package sourcePackage, Agent agent, String message
    );

    List<Path> getFilePaths(String id);

    List<Path> getStorageFilePaths(String id);

    List<Version> getVersions(String objectId);

    RepositoryClient stageChanges(
        String objectId, Package sourcePackage, Agent agent, String message
    );

    RepositoryClient commitChanges(String id, Agent agent, String message);

    RepositoryClient purgeChanges(String id);

    boolean hasChanges(String id);
}
