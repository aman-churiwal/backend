package ${package}.service.quiz;

import ${package}.db.quiz.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration("servicesConfig")
@Import({
        ${package}.db.quiz.IoCTemplate.class
})
public class IoCTemplate {

    @Bean("${moduleNameCamelCase}Service")
    // TODO. Move this bean to the equivalent IoC present in the target project,
    public ${moduleNamePascalCase}Service ${moduleNameCamelCase}Service(
            ${moduleNamePascalCase}Repository ${moduleNameCamelCase}Repository
    ) {
        return new ${moduleNamePascalCase}ServiceImpl(${moduleNameCamelCase}Repository);
    }

    @Bean("${moduleNameCamelCase}AnswerService")
    // TODO. Move this bean to the equivalent IoC present in the target project,
    public ${moduleNamePascalCase}AnswerService ${moduleNameCamelCase}AnswerService(
            ${moduleNamePascalCase}AnswerRepository ${moduleNameCamelCase}AnswerRepository,
            QuizService quizService
    ) {
        return new ${moduleNamePascalCase}AnswerServiceImpl(${moduleNameCamelCase}AnswerRepository, quizService);
    }

}
