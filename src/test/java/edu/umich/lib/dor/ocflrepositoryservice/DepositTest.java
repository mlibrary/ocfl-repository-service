package edu.umich.lib.dor.ocflrepositoryservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Curator;
import edu.umich.lib.dor.ocflrepositoryservice.exception.EntityAlreadyExistsException;
import edu.umich.lib.dor.ocflrepositoryservice.service.Deposit;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositDirectory;
import edu.umich.lib.dor.ocflrepositoryservice.service.DepositFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.OcflFilesystemRepositoryClient;
import edu.umich.lib.dor.ocflrepositoryservice.service.Package;

public class DepositTest {
    Curator testCurator = new Curator("test", "test@example.edu");

    DepositDirectory depositDirMock;
    DepositFactory depositFactory;
    OcflFilesystemRepositoryClient clientMock;
    Package sourcePackageMock;

    @BeforeEach
    void init() {
        this.depositDirMock = mock(DepositDirectory.class);
        this.clientMock = mock(OcflFilesystemRepositoryClient.class);
        this.depositFactory = new DepositFactory(
            clientMock,
            depositDirMock
        );

        this.sourcePackageMock = mock(Package.class);
    }

    @Test
    void depositCanBeCreated() {
        when(clientMock.hasObject("A")).thenReturn(false);
        when(depositDirMock.getPackage(Paths.get("something")))
            .thenReturn(sourcePackageMock);

        assertDoesNotThrow(() -> {
            depositFactory.create(
                "A",
                Paths.get("something"),
                testCurator,
                "we're good"
            );
        });
    }

    @Test
    void depositFailsWhenObjectAlreadyExists() {
        when(clientMock.hasObject("A")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            depositFactory.create(
                "A",
                Paths.get("/something"),
                testCurator,
                "did I already add this?"
            );
        });
    }

    @Test
    void depositExecutes() {
        when(clientMock.hasObject("A")).thenReturn(false);
        when(depositDirMock.getPackage(Paths.get("something")))
            .thenReturn(sourcePackageMock);

        final Deposit deposit = depositFactory.create(
            "A",
            Paths.get("something"),
            testCurator,
            "we're good"
        );

        deposit.execute();
        verify(clientMock).createObject(
            "A", sourcePackageMock, testCurator, "we're good"
        );
    }
}
