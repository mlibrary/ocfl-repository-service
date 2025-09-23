package edu.umich.lib.dor.ocflrepositoryservice.domain;

import java.time.OffsetDateTime;

public record Version(
    long number, String message, Curator curator, OffsetDateTime created
) {};

