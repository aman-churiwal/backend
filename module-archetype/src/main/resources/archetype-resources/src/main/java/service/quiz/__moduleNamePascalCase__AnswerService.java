package ${package}.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ${package}.domain.quiz.${moduleNamePascalCase}Answer;
import ${package}.domain.dto.CategorizedAnswer;

import java.util.Optional;
import java.util.UUID;

public interface ${moduleNamePascalCase}AnswerService {

    Either<Throwable, Optional<${moduleNamePascalCase}Answer>> find(Long id);

    Either<Throwable, Optional<${moduleNamePascalCase}Answer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ${moduleNamePascalCase}Answer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ${moduleNamePascalCase}Answer> update(CategorizedAnswer categorizedAnswer);

}