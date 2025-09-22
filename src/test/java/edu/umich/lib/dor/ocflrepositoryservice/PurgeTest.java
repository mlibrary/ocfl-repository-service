package edu.umich.lib.dor.ocflrepositoryservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.umich.lib.dor.ocflrepositoryservice.exception.NoEntityException;
import edu.umich.lib.dor.ocflrepositoryservice.service.Purge;
import edu.umich.lib.dor.ocflrepositoryservice.service.PurgeFactory;
import edu.umich.lib.dor.ocflrepositoryservice.service.RepositoryClient;

public class PurgeTest {
    RepositoryClient clientMock;
    PurgeFactory purgeFactory;

    @BeforeEach
    void init() {
        this.clientMock = mock(RepositoryClient.class);
        this.purgeFactory = new PurgeFactory(clientMock);
    }

    @Test
    void factoryCreatesPurge() {
        when(clientMock.hasObject("A")).thenReturn(true);

        assertDoesNotThrow(() -> {
            purgeFactory.create("A");
        });
    }

    @Test
    void purgeFailsWhenObjectDoesNotExist() {
        when(clientMock.hasObject("?")).thenReturn(false);
    
        assertThrows(NoEntityException.class, () -> {
            purgeFactory.create("?");
        });
    }

    @Test
    void purgeExecutes() {
        when(clientMock.hasObject("A")).thenReturn(true);

        Purge purge = purgeFactory.create("A");
        purge.execute();

        verify(clientMock).purgeObject("A");
    }
}
