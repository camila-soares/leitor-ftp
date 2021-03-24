package com.microservicemigration.leitorftp.gateway.gson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileUUIDJson {

    private String uuid;
}
