package com.example.contract_master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ContractMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractMasterApplication.class, args);
    }
}
