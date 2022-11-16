/*
 * This file is generated by jOOQ.
 */
package sts.generated.enums;


import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum StsUserRole implements EnumType {

    admin("admin"),

    user("user"),

    stock("stock");

    private final String literal;

    private StsUserRole(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return null;
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public String getName() {
        return "sts_user_role";
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Lookup a value of this EnumType by its literal
     */
    public static StsUserRole lookupLiteral(String literal) {
        return EnumType.lookupLiteral(StsUserRole.class, literal);
    }
}
