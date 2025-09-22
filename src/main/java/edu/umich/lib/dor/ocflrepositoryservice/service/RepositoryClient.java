package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;
import java.util.List;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;

public interface RepositoryClient {
    RepositoryClient createObject(String id, Package sourcePackage, Curator curator, String message);

    RepositoryClient readObject(String id, Path outputPath);

    RepositoryClient purgeObject(String id);

    boolean hasObject(String id);

    RepositoryClient deleteObjectFile(
        String objectId, String filePath, Curator curator, String message
    );

    RepositoryClient updateObjectFiles(
        String objectId, Package sourcePackage, Curator curator, String message
    );

    List<Path> getFilePaths(String id);

    List<Path> getStorageFilePaths(String id);
}
