package edu.umich.lib.dor.ocflrepositoryservice.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.ocfl.api.MutableOcflRepository;
import io.ocfl.api.OcflOption;
import io.ocfl.api.model.FileDetails;
import io.ocfl.api.model.ObjectDetails;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.api.model.VersionInfo;
import io.ocfl.core.OcflRepositoryBuilder;
import io.ocfl.core.extension.storage.layout.config.HashedNTupleLayoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.domain.Version;

public class OcflFilesystemRepositoryClient implements RepositoryClient {
    private static final Logger log = LoggerFactory
        .getLogger(OcflFilesystemRepositoryClient.class);

    private MutableOcflRepository repo;

    public OcflFilesystemRepositoryClient(Path rootPath, Path workPath) {
        this.repo = new OcflRepositoryBuilder()
            .prettyPrintJson()
            .defaultLayoutConfig(new HashedNTupleLayoutConfig())
            .storage(storage -> storage.fileSystem(rootPath))
            .workDir(workPath)
            .buildMutable();
    }

    public OcflFilesystemRepositoryClient(MutableOcflRepository repository) {
        this.repo = repository;
    }

    private VersionInfo createNewVersion(Curator curator, String message) {
        return new VersionInfo().setUser(curator.username(), curator.email()).setMessage(message);
    }

    public RepositoryClient createObject(
        String id, Package sourcePackage, Curator curator, String message
    ) {
        repo.putObject(
            ObjectVersionId.head(id),
            sourcePackage.getRootPath(),
            createNewVersion(curator, message)
        );
        return this;
    }

    public RepositoryClient readObject(String id, Path outputPath) {
        repo.getObject(ObjectVersionId.head(id), outputPath);
        return this;
    }

    public RepositoryClient purgeObject(String id) {
        repo.purgeObject(id);
        return this;
    }

    public boolean hasObject(String id) {
        return repo.containsObject(id);
    }

    private Collection<FileDetails> getFiles(String id) {
        ObjectDetails objectDetails = repo.describeObject(id);
        var files = objectDetails
            .getHeadVersion()
            .getFiles();
        return files;
    }

    public List<Path> getStorageFilePaths(String id) {
        var filePaths = getFiles(id)
            .stream()
            .map(fileDetails -> fileDetails.getStorageRelativePath())
            .map(p -> Paths.get(p))
            .toList();
        log.debug(filePaths.toString());
        return filePaths;
    }

    public List<Path> getFilePaths(String id) {
        var filePaths = getFiles(id)
            .stream()
            .map(fileDetails -> fileDetails.getPath())
            .map(p -> Paths.get(p))
            .toList();
        log.debug(filePaths.toString());
        return filePaths;
    }

    public List<Version> getVersions(String objectId) {
        List<Version> versions = new ArrayList<>();
        var versionMap = repo.describeObject(objectId).getVersionMap();
        versionMap.forEach((versionNum, versionDetails) -> {
            var versionInfo = versionDetails.getVersionInfo();
            var user = versionInfo.getUser();
            versions.add(new Version(
                versionNum.getVersionNum(),
                versionInfo.getMessage(),
                new Curator(user.getName(), user.getAddress().replace("mailto:", "")),
                versionInfo.getCreated()
            ));
        });
        return versions;
    }

    public RepositoryClient deleteObjectFile(
        String objectId, String filePath, Curator curator, String message
    ) {
        repo.updateObject(
            ObjectVersionId.head(objectId),
            createNewVersion(curator, message),
            updater -> { updater.removeFile(filePath); }
        );
        return this;
    }

    public RepositoryClient updateObjectFiles(
        String objectId, Package sourcePackage, Curator curator, String message
    ) {
        Path rootPackagePath = sourcePackage.getRootPath();
        repo.updateObject(
            ObjectVersionId.head(objectId),
            createNewVersion(curator, message),
            updater -> {
                for (Path inputPath : sourcePackage.getFilePaths()) {
                    updater.addPath(
                        rootPackagePath.resolve(inputPath),
                        inputPath.toString(),
                        OcflOption.OVERWRITE
                    );
                }
            }
        );
        return this;
    }

    public RepositoryClient stageChanges(
        String objectId, Package sourcePackage, Curator curator, String message
    ) {
        Path rootPackagePath = sourcePackage.getRootPath();
        repo.stageChanges(
            ObjectVersionId.head(objectId),
            createNewVersion(curator, message),
            updater -> {
                for (Path inputPath : sourcePackage.getFilePaths()) {
                    updater.addPath(
                        rootPackagePath.resolve(inputPath),
                        inputPath.toString(),
                        OcflOption.OVERWRITE
                    );
                }
            }
        );
        return this;
    }

    public RepositoryClient commitChanges(
        String objectId, Curator curator, String message
    ) {
        repo.commitStagedChanges(objectId, createNewVersion(curator, message));
        return this;
    }

    public boolean hasChanges(String objectId) {
        return repo.hasStagedChanges(objectId);
    }

    public RepositoryClient purgeChanges(String objectId) {
        repo.purgeStagedChanges(objectId);
        return this;
    }
}