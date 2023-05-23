package com.example.springrestlevel3;


import com.example.springrestlevel3.payroll.Employee;
import com.example.springrestlevel3.payroll.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.springrestlevel3.payroll.Employee;
@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {

        return args -> {
            employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
            employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));


        };
    }
}
