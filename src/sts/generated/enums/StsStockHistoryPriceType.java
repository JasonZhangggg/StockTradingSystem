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
public enum StsStockHistoryPriceType implements EnumType {

    open("open"),

    close("close"),

    normal("normal");

    private final String literal;

    private StsStockHistoryPriceType(String literal) {
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
        return "sts_stock_history_price_type";
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Lookup a value of this EnumType by its literal
     */
    public static StsStockHistoryPriceType lookupLiteral(String literal) {
        return EnumType.lookupLiteral(StsStockHistoryPriceType.class, literal);
    }
}
