package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.blend.BlendIngredientReasonCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BlendIngredientReasonMapper implements RowMapper<BlendIngredientReason> {

    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String DESCRIPTION = "description";
    private static final String CREATION_TIMESTAMP = "creation_timestamp";
    private static final String LAST_MODIFICATION_TIMESTAMP = "last_modification_timestamp";

    @Override
    public BlendIngredientReason map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong(ID);
        final String codeStringified = rs.getString(CODE);
        final BlendIngredientReasonCode code = BlendIngredientReasonCode.valueOf(codeStringified);
        final String description = rs.getString(DESCRIPTION);
        final LocalDateTime creationTimestamp = rs.getTimestamp(CREATION_TIMESTAMP).toLocalDateTime();
        final LocalDateTime lastModificationTimestamp = rs.getTimestamp(LAST_MODIFICATION_TIMESTAMP).toLocalDateTime();
        return new BlendIngredientReason(id, code, description, creationTimestamp, lastModificationTimestamp);
    }
}