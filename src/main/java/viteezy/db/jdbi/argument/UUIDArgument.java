package viteezy.db.jdbi.argument;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;
import java.util.UUID;

public class UUIDArgument extends AbstractArgumentFactory<UUID> {

    public UUIDArgument() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(UUID value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.toString());
    }
}
