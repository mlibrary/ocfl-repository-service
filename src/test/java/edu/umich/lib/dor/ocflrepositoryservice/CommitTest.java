package edu.umich.lib.dor.ocflrepositoryservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.umich.lib.dor.ocflrepositoryservice.domain.Agent;
import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;
import edu.umich.lib.dor.ocflrepositoryservice.service.Commit;
import edu.umich.lib.dor.ocflrepositoryservice.service.CommitFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.RepositoryClient;

public class CommitTest {
    Agent testAgent = new Agent("test", "test@example.edu");

    RepositoryClient clientMock;
    CommitFactory commitFactory;

    @BeforeEach
    void init() {
        this.clientMock = mock(RepositoryClient.class);
        this.commitFactory = new CommitFactory(clientMock);
    }

    @Test
    void factoryCreatesCommit() {
        when(clientMock.hasObject("A")).thenReturn(true);

        assertDoesNotThrow(() -> {
            commitFactory.create("A", testAgent, "Saving accumulated changes");
        });
    }

    @Test
    void commitFailsWhenObjectDoesNotExist() {
        when(clientMock.hasObject("?")).thenReturn(false);
    
        assertThrows(NoEntityException.class, () -> {
            commitFactory.create("?", testAgent, "Saving accumulated changes");
        });
    }

    @Test
    void commitExecutes() {
        when(clientMock.hasObject("A")).thenReturn(true);

        Commit commit = commitFactory.create("A", testAgent, "Saving accumulated changes");
        commit.execute();

        verify(clientMock).commitChanges("A", testAgent, "Saving accumulated changes");
    }
}