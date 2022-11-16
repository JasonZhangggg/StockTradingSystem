/*
 * This file is generated by jOOQ.
 */
package sts.generated.tables;


import java.math.BigDecimal;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import sts.generated.Keys;
import sts.generated.Mydev;
import sts.generated.tables.records.StsStockRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StsStock extends TableImpl<StsStockRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>mydev.sts_stock</code>
     */
    public static final StsStock STS_STOCK = new StsStock();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StsStockRecord> getRecordType() {
        return StsStockRecord.class;
    }

    /**
     * The column <code>mydev.sts_stock.ticker</code>.
     */
    public final TableField<StsStockRecord, String> TICKER = createField(DSL.name("ticker"), SQLDataType.CHAR(4).nullable(false), this, "");

    /**
     * The column <code>mydev.sts_stock.company</code>.
     */
    public final TableField<StsStockRecord, String> COMPANY = createField(DSL.name("company"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>mydev.sts_stock.shares</code>.
     */
    public final TableField<StsStockRecord, Integer> SHARES = createField(DSL.name("shares"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>mydev.sts_stock.init_price</code>.
     */
    public final TableField<StsStockRecord, BigDecimal> INIT_PRICE = createField(DSL.name("init_price"), SQLDataType.DECIMAL(13, 2), this, "");

    /**
     * The column <code>mydev.sts_stock.is_listed</code>.
     */
    public final TableField<StsStockRecord, Byte> IS_LISTED = createField(DSL.name("is_listed"), SQLDataType.TINYINT, this, "");

    private StsStock(Name alias, Table<StsStockRecord> aliased) {
        this(alias, aliased, null);
    }

    private StsStock(Name alias, Table<StsStockRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>mydev.sts_stock</code> table reference
     */
    public StsStock(String alias) {
        this(DSL.name(alias), STS_STOCK);
    }

    /**
     * Create an aliased <code>mydev.sts_stock</code> table reference
     */
    public StsStock(Name alias) {
        this(alias, STS_STOCK);
    }

    /**
     * Create a <code>mydev.sts_stock</code> table reference
     */
    public StsStock() {
        this(DSL.name("sts_stock"), null);
    }

    public <O extends Record> StsStock(Table<O> child, ForeignKey<O, StsStockRecord> key) {
        super(child, key, STS_STOCK);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Mydev.MYDEV;
    }

    @Override
    public UniqueKey<StsStockRecord> getPrimaryKey() {
        return Keys.KEY_STS_STOCK_PRIMARY;
    }

    @Override
    public StsStock as(String alias) {
        return new StsStock(DSL.name(alias), this);
    }

    @Override
    public StsStock as(Name alias) {
        return new StsStock(alias, this);
    }

    @Override
    public StsStock as(Table<?> alias) {
        return new StsStock(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StsStock rename(String name) {
        return new StsStock(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StsStock rename(Name name) {
        return new StsStock(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StsStock rename(Table<?> name) {
        return new StsStock(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, Integer, BigDecimal, Byte> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super String, ? super String, ? super Integer, ? super BigDecimal, ? super Byte, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super String, ? super String, ? super Integer, ? super BigDecimal, ? super Byte, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}