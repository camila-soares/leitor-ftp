package com.microservicemigration.leitorftp.gateway.repositories;

import com.microservicemigration.leitorftp.domain.Files;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilesRepository extends CrudRepository< Files, UUID > {
}
