package ${package}.db.jdbi.quiz;

import ${package}.db.jdbi.mapper.quiz.${moduleNamePascalCase}Mapper;
import ${package}.db.jdbi.mapper.quiz.${moduleNamePascalCase}AnswerMapper;

import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.jdbi.v3.core.Jdbi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration("jdbiDbConfig")
@EnableTransactionManagement
public class IoCTemplate {

    // ...

    @Bean("dbi")
    // TODO. Merge this method with the equivalent one present in the equivalent IoC
    public Jdbi jdbi(TransactionAwareDataSourceProxy transactionAwareDataSourceProxy) {
        Jdbi jdbi = Jdbi.create(transactionAwareDataSourceProxy);

        jdbi.registerRowMapper(new ${moduleNamePascalCase}AnswerMapper());
        jdbi.registerRowMapper(new ${moduleNamePascalCase}Mapper());

        return jdbi;
    }

    // ...

}
