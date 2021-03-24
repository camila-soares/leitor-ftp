package com.microservicemigration.leitorftp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "FILES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Files {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "PATH", nullable = false )
    private String path;

    @Column(name = "STATUS", nullable = false )
    private String status;
}
