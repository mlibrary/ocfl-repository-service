package edu.umich.lib.dor.ocflrepositoryservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositDirectory;
import edu.umich.lib.dor.ocflrepositoryservice.service.OcflFilesystemRepositoryClient;
import edu.umich.lib.dor.ocflrepositoryservice.service.Package;
import edu.umich.lib.dor.ocflrepositoryservice.service.Stage;
import edu.umich.lib.dor.ocflrepositoryservice.service.StageFactory;

public class StageTest {
    Agent testAgent = new Agent("test", "test@example.edu");

    DepositDirectory depositDirMock;
    StageFactory stageFactory;
    OcflFilesystemRepositoryClient clientMock;
    Package sourcePackageMock;

    @BeforeEach
    void init() {
        this.depositDirMock = mock(DepositDirectory.class);
        this.clientMock = mock(OcflFilesystemRepositoryClient.class);
        this.stageFactory = new StageFactory(
            clientMock,
            depositDirMock
        );

        this.sourcePackageMock = mock(Package.class);
    }

    @Test
    void stageCanBeCreated() {
        when(depositDirMock.getPackage(Paths.get("something")))
            .thenReturn(sourcePackageMock);

        assertDoesNotThrow(() -> {
            stageFactory.create(
                testAgent,
                "A",
                Paths.get("something"),
                "we're good"
            );
        });
    }

    @Test
    void stageExecutes() {
        when(depositDirMock.getPackage(Paths.get("something")))
            .thenReturn(sourcePackageMock);

        final Stage stage = stageFactory.create(
            testAgent,
            "A",
            Paths.get("something"),
            "we're good"
        );

        stage.execute();
        verify(clientMock).stageChanges(
            "A", sourcePackageMock, testAgent, "we're good"
        );
    }
}
