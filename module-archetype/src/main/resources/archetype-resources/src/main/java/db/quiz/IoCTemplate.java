package ${package}.db.quiz;

import ${package}.db.jdbi.quiz.${moduleNamePascalCase}RepositoryImpl;
import ${package}.db.jdbi.quiz.${moduleNamePascalCase}AnswerRepositoryImpl;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration("dbConfig")
@Import({
        ${package}.db.jdbi.quiz.IoCTemplate.class
})
public class IoCTemplate {

    @Bean("${moduleNameCamelCase}Repository")
    // TODO. Move this bean to the equivalent IoC present in the target project,
    public ${moduleNamePascalCase}Repository ${moduleNameCamelCase}Repository(Jdbi jdbi) {
        return new ${moduleNamePascalCase}RepositoryImpl(jdbi);
    }

    @Bean("${moduleNameCamelCase}AnswerRepository")
    // TODO. Move this bean to the equivalent IoC present in the target project,
    public ${moduleNamePascalCase}AnswerRepository ${moduleNameCamelCase}AnswerRepository(Jdbi jdbi) {
        return new ${moduleNamePascalCase}AnswerRepositoryImpl(jdbi);
    }

}
