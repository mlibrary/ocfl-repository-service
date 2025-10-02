package edu.umich.lib.dor.ocflrepositoryservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;
import edu.umich.lib.dor.ocflrepositoryservice.exception.ObjectHasChangesException;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositDirectory;
import edu.umich.lib.dor.ocflrepositoryservice.service.OcflFilesystemRepositoryClient;
import edu.umich.lib.dor.ocflrepositoryservice.service.Package;
import edu.umich.lib.dor.ocflrepositoryservice.service.Update;
import edu.umich.lib.dor.ocflrepositoryservice.service.UpdateFactory;

public class UpdateTest {
    Agent testAgent = new Agent("test", "test@example.edu");

    DepositDirectory depositDirMock;
    UpdateFactory updateFactory;
    OcflFilesystemRepositoryClient clientMock;
    Package sourcePackageMock;

    @BeforeEach
    void init() {
        this.depositDirMock = mock(DepositDirectory.class);
        this.clientMock = mock(OcflFilesystemRepositoryClient.class);
        this.updateFactory = new UpdateFactory(
            clientMock,
            depositDirMock
        );
        this.sourcePackageMock = mock(Package.class);
    }

    @Test
    void updateCanBeCreated() {
        when(clientMock.hasObject("A")).thenReturn(true);
        when(clientMock.hasChanges("A")).thenReturn(false);
        when(depositDirMock.getPackage(Paths.get("update_A")))
            .thenReturn(sourcePackageMock);
    
        assertDoesNotThrow(() -> {
            updateFactory.create(
                "A",
                Paths.get("update_A"),
                testAgent,
                "we're good"
            );
        });
    }

    @Test
    void updateFailsWhenObjectDoesNotExist() {
        when(clientMock.hasObject("A")).thenReturn(false);

        assertThrows(NoEntityException.class, () -> {
            updateFactory.create(
                "A",
                Paths.get("update_A"),
                testAgent,
                "did I not add this yet?"
            );
        });
    }

    @Test
    void updateFailsWhenObjectHasChanges() {
        when(clientMock.hasObject("A")).thenReturn(true);
        when(clientMock.hasChanges("A")).thenReturn(true);

        assertThrows(ObjectHasChangesException.class, () -> {
            updateFactory.create(
                "A",
                Paths.get("update_A"),
                testAgent,
                "did I stage something already?"
            );
        });
    }

    @Test
    void updateExecutes() {
        when(clientMock.hasObject("A")).thenReturn(true);
        when(clientMock.hasChanges("A")).thenReturn(false);
        when(depositDirMock.getPackage(Paths.get("update_A")))
            .thenReturn(sourcePackageMock);

        final Update update = updateFactory.create(
            "A",
            Paths.get("update_A"),
            testAgent,
            "we're good"
        );

        update.execute();
        verify(clientMock).updateObjectFiles(
            "A",
            sourcePackageMock,
            testAgent,
            "we're good"
        );
    }
}
