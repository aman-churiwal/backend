package viteezy.db.jdbi.argument;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import viteezy.domain.blend.BlendIngredientReasonCode;

import java.sql.Types;

public class BlendIngredientReasonCodeArgument extends AbstractArgumentFactory<BlendIngredientReasonCode> {

    public BlendIngredientReasonCodeArgument() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(BlendIngredientReasonCode value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.toString());
    }
}
