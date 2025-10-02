package edu.umich.lib.dor.ocflrepositoryservice.domain;

import java.time.OffsetDateTime;

public record Version(
    long number, String message, Agent agent, OffsetDateTime created, boolean isStaged
) {};

