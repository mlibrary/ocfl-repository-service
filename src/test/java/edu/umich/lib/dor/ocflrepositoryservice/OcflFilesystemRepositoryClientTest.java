package edu.umich.lib.dor.ocflrepositoryservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import io.ocfl.api.MutableOcflRepository;
import io.ocfl.api.OcflObjectUpdater;
import io.ocfl.api.OcflOption;
import io.ocfl.api.model.FileDetails;
import io.ocfl.api.model.ObjectDetails;
import io.ocfl.api.model.ObjectVersionId;
import io.ocfl.api.model.User;
import io.ocfl.api.model.VersionDetails;
import io.ocfl.api.model.VersionInfo;
import io.ocfl.api.model.VersionNum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.domain.Version;
import edu.umich.lib.dor.ocflrepositoryservice.service.OcflFilesystemRepositoryClient;
import edu.umich.lib.dor.ocflrepositoryservice.service.Package;

public class OcflFilesystemRepositoryClientTest {
    MutableOcflRepository ocflRepositoryMock;
    OcflFilesystemRepositoryClient repositoryClient;
    Package sourcePackageMock;
    OffsetDateTime now = OffsetDateTime.now(ZoneId.of("UTC"));

    @BeforeEach
    public void init() {
        ocflRepositoryMock = mock(MutableOcflRepository.class);
        repositoryClient = new OcflFilesystemRepositoryClient(ocflRepositoryMock);
        sourcePackageMock = mock(Package.class);
    }

    private ObjectDetails createObjectDetails() {
        var fileaDetails = new FileDetails();
        fileaDetails.setPath("test.txt");
        fileaDetails.setStorageRelativePath("storage/A/test.txt");

        var fileMap = new HashMap<String, FileDetails>();
        fileMap.put("A", fileaDetails);
        var versionDetails = new VersionDetails();
        versionDetails.setFileMap(fileMap).setMutable(false);
        VersionInfo versionInfo = new VersionInfo()
            .setCreated(now)
            .setMessage("Some message")
            .setUser("test", "mailto:test@example.edu");
        versionDetails.setVersionInfo(versionInfo);

        var versionMap = new HashMap<VersionNum, VersionDetails>();
        var versionNum = new VersionNum(1);
        versionMap.put(versionNum, versionDetails);
        var details = new ObjectDetails()
            .setId("A")
            .setHeadVersionNum(versionNum)
            .setVersions(versionMap);
        return details;
    }

    @Test
    public void clientWithInjectedRepositoryWorks() {
        new OcflFilesystemRepositoryClient(ocflRepositoryMock);
    }

    @Test
    public void clientCreatesObject() {
        when(sourcePackageMock.getRootPath()).thenReturn(Paths.get("some/path/deposit_A"));

        repositoryClient.createObject(
            "A",
            sourcePackageMock,
            new Agent("test", "test@example.edu"),
            "adding images and metadata"
        );

        verify(ocflRepositoryMock).putObject(
            ObjectVersionId.head("A"),
            Paths.get("some/path/deposit_A"),
            new VersionInfo().setUser(
                "test", "test@example.edu"
            ).setMessage("adding images and metadata")
        );
    }

    @Test
    public void clientUpdatesObject() {
        when(sourcePackageMock.getRootPath()).thenReturn(Paths.get("some/path/update_A"));
        when(sourcePackageMock.getFilePaths()).thenReturn(List.of(
            Paths.get("A.txt"), Paths.get("B/C.txt")
        ));

        when(ocflRepositoryMock.updateObject(
            any(ObjectVersionId.class),
            any(VersionInfo.class),
            ArgumentMatchers.<Consumer<OcflObjectUpdater>>any()
        ))
            .then((invocationOnMock) -> {
                var args = invocationOnMock.getArguments();

                ObjectVersionId versionId = (ObjectVersionId) args[0];
                assertEquals("A", versionId.getObjectId());

                VersionInfo versionInfo = (VersionInfo) args[1];
                assertEquals("Updating files", versionInfo.getMessage());
                User user = versionInfo.getUser();
                assertEquals("test", user.getName());
                assertEquals("test@example.edu", user.getAddress());

                Consumer<OcflObjectUpdater> updaterLambda = invocationOnMock.getArgument(2);
                OcflObjectUpdater updaterMock = mock(OcflObjectUpdater.class);
                updaterLambda.accept(updaterMock);

                verify(updaterMock).addPath(
                    Paths.get("some/path/update_A/A.txt"),
                    "A.txt",
                    OcflOption.OVERWRITE
                );
                verify(updaterMock).addPath(
                    Paths.get("some/path/update_A/B/C.txt"),
                    "B/C.txt",
                    OcflOption.OVERWRITE
                );
                return versionId;
            });

        repositoryClient.updateObjectFiles(
            "A",
            sourcePackageMock,
            new Agent("test", "test@example.edu"),
            "Updating files"
        );
    }

    @Test
    public void clientGetsPackagePaths() {
        when(ocflRepositoryMock.describeObject("A"))
            .thenReturn(createObjectDetails());
        var filePaths = repositoryClient.getFilePaths("A");
        assertIterableEquals(List.of(Paths.get("test.txt")), filePaths);
    }

    @Test
    public void clientGetsStoragePackagePaths() {
        when(ocflRepositoryMock.describeObject("A"))
            .thenReturn(createObjectDetails());
        var filePaths = repositoryClient.getStorageFilePaths("A");
        assertIterableEquals(List.of(Paths.get("/storage/A/test.txt")), filePaths);
    }

    @Test
    public void clientGetsVersions() {
        when(ocflRepositoryMock.describeObject("A"))
            .thenReturn(createObjectDetails());
        List<Version> versions = repositoryClient.getVersions("A");
        Version expectedVersion = new Version(
            1, "Some message", new Agent("test", "test@example.edu"), now, false
        );
        assertIterableEquals(List.of(expectedVersion), versions);
    }

    @Test
    public void clientPurgesObject() {
        repositoryClient.purgeObject("A");
        verify(ocflRepositoryMock).purgeObject("A");
    }

    @Test
    public void clientStagesChanges() {
        when(sourcePackageMock.getRootPath()).thenReturn(Paths.get("some/path/update_A"));
        when(sourcePackageMock.getFilePaths()).thenReturn(List.of(
            Paths.get("A.txt"), Paths.get("B/C.txt")
        ));

        when(ocflRepositoryMock.stageChanges(
            any(ObjectVersionId.class),
            any(VersionInfo.class),
            ArgumentMatchers.<Consumer<OcflObjectUpdater>>any()
        ))
            .then((invocationOnMock) -> {
                var args = invocationOnMock.getArguments();

                ObjectVersionId versionId = (ObjectVersionId) args[0];
                assertEquals("A", versionId.getObjectId());

                VersionInfo versionInfo = (VersionInfo) args[1];
                assertEquals("Staging some files", versionInfo.getMessage());
                User user = versionInfo.getUser();
                assertEquals("test", user.getName());
                assertEquals("test@example.edu", user.getAddress());

                Consumer<OcflObjectUpdater> updaterLambda = invocationOnMock.getArgument(2);
                OcflObjectUpdater updaterMock = mock(OcflObjectUpdater.class);
                updaterLambda.accept(updaterMock);

                verify(updaterMock).addPath(
                    Paths.get("some/path/update_A/A.txt"),
                    "A.txt",
                    OcflOption.OVERWRITE
                );
                verify(updaterMock).addPath(
                    Paths.get("some/path/update_A/B/C.txt"),
                    "B/C.txt",
                    OcflOption.OVERWRITE
                );
                return versionId;
            });

        repositoryClient.stageChanges(
            "A",
            sourcePackageMock,
            new Agent("test", "test@example.edu"),
            "Staging some files"
        );
    }

    @Test
    public void clientCommitsChanges() {
        repositoryClient.commitChanges(
            "A",
            new Agent("test", "test@example.edu"),
            "Adding version based on changes"
        );
        verify(ocflRepositoryMock).commitStagedChanges(
            "A",
            new VersionInfo().setUser(
                "test", "test@example.edu"
            ).setMessage("Adding version based on changes")
        );
    }

    @Test
    public void clientIndicatesItHasChanges() {
        repositoryClient.hasChanges("A");
        verify(ocflRepositoryMock).hasStagedChanges("A");
    }

    @Test
    public void clientPurgesChanges() {
        repositoryClient.purgeChanges("A");
        verify(ocflRepositoryMock).purgeStagedChanges("A");
    }
}
