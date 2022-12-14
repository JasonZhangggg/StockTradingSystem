/*
 * This file is generated by jOOQ.
 */
package sts.generated.tables;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function11;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row11;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import sts.generated.Indexes;
import sts.generated.Keys;
import sts.generated.Mydev;
import sts.generated.enums.StsStockOrderExecType;
import sts.generated.enums.StsStockOrderOrderStatus;
import sts.generated.tables.records.StsStockOrderRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StsStockOrder extends TableImpl<StsStockOrderRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>mydev.sts_stock_order</code>
     */
    public static final StsStockOrder STS_STOCK_ORDER = new StsStockOrder();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StsStockOrderRecord> getRecordType() {
        return StsStockOrderRecord.class;
    }

    /**
     * The column <code>mydev.sts_stock_order.user_id</code>.
     */
    public final TableField<StsStockOrderRecord, Integer> USER_ID = createField(DSL.name("user_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>mydev.sts_stock_order.ticker</code>.
     */
    public final TableField<StsStockOrderRecord, String> TICKER = createField(DSL.name("ticker"), SQLDataType.CHAR(4), this, "");

    /**
     * The column <code>mydev.sts_stock_order.order_status</code>.
     */
    public final TableField<StsStockOrderRecord, StsStockOrderOrderStatus> ORDER_STATUS = createField(DSL.name("order_status"), SQLDataType.VARCHAR(6).asEnumDataType(sts.generated.enums.StsStockOrderOrderStatus.class), this, "");

    /**
     * The column <code>mydev.sts_stock_order.expiration</code>.
     */
    public final TableField<StsStockOrderRecord, Integer> EXPIRATION = createField(DSL.name("expiration"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>mydev.sts_stock_order.limit_price</code>.
     */
    public final TableField<StsStockOrderRecord, BigDecimal> LIMIT_PRICE = createField(DSL.name("limit_price"), SQLDataType.DECIMAL(13, 2), this, "");

    /**
     * The column <code>mydev.sts_stock_order.exec_price</code>.
     */
    public final TableField<StsStockOrderRecord, BigDecimal> EXEC_PRICE = createField(DSL.name("exec_price"), SQLDataType.DECIMAL(13, 2), this, "");

    /**
     * The column <code>mydev.sts_stock_order.shares</code>.
     */
    public final TableField<StsStockOrderRecord, Integer> SHARES = createField(DSL.name("shares"), SQLDataType.INTEGER.defaultValue(DSL.inline("0", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>mydev.sts_stock_order.order_time</code>.
     */
    public final TableField<StsStockOrderRecord, LocalDateTime> ORDER_TIME = createField(DSL.name("order_time"), SQLDataType.LOCALDATETIME(0), this, "");

    /**
     * The column <code>mydev.sts_stock_order.exec_time</code>.
     */
    public final TableField<StsStockOrderRecord, LocalDateTime> EXEC_TIME = createField(DSL.name("exec_time"), SQLDataType.LOCALDATETIME(0), this, "");

    /**
     * The column <code>mydev.sts_stock_order.exec_type</code>.
     */
    public final TableField<StsStockOrderRecord, StsStockOrderExecType> EXEC_TYPE = createField(DSL.name("exec_type"), SQLDataType.VARCHAR(11).asEnumDataType(sts.generated.enums.StsStockOrderExecType.class), this, "");

    /**
     * The column <code>mydev.sts_stock_order.market_day</code>.
     */
    public final TableField<StsStockOrderRecord, Integer> MARKET_DAY = createField(DSL.name("market_day"), SQLDataType.INTEGER, this, "");

    private StsStockOrder(Name alias, Table<StsStockOrderRecord> aliased) {
        this(alias, aliased, null);
    }

    private StsStockOrder(Name alias, Table<StsStockOrderRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>mydev.sts_stock_order</code> table reference
     */
    public StsStockOrder(String alias) {
        this(DSL.name(alias), STS_STOCK_ORDER);
    }

    /**
     * Create an aliased <code>mydev.sts_stock_order</code> table reference
     */
    public StsStockOrder(Name alias) {
        this(alias, STS_STOCK_ORDER);
    }

    /**
     * Create a <code>mydev.sts_stock_order</code> table reference
     */
    public StsStockOrder() {
        this(DSL.name("sts_stock_order"), null);
    }

    public <O extends Record> StsStockOrder(Table<O> child, ForeignKey<O, StsStockOrderRecord> key) {
        super(child, key, STS_STOCK_ORDER);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Mydev.MYDEV;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.STS_STOCK_ORDER_MARKET_DAY, Indexes.STS_STOCK_ORDER_TICKER);
    }

    @Override
    public List<ForeignKey<StsStockOrderRecord, ?>> getReferences() {
        return Arrays.asList(Keys.STS_STOCK_ORDER_IBFK_1, Keys.STS_STOCK_ORDER_IBFK_2);
    }

    private transient StsStock _stsStock;
    private transient StsMarketHistory _stsMarketHistory;

    /**
     * Get the implicit join path to the <code>mydev.sts_stock</code> table.
     */
    public StsStock stsStock() {
        if (_stsStock == null)
            _stsStock = new StsStock(this, Keys.STS_STOCK_ORDER_IBFK_1);

        return _stsStock;
    }

    /**
     * Get the implicit join path to the <code>mydev.sts_market_history</code>
     * table.
     */
    public StsMarketHistory stsMarketHistory() {
        if (_stsMarketHistory == null)
            _stsMarketHistory = new StsMarketHistory(this, Keys.STS_STOCK_ORDER_IBFK_2);

        return _stsMarketHistory;
    }

    @Override
    public StsStockOrder as(String alias) {
        return new StsStockOrder(DSL.name(alias), this);
    }

    @Override
    public StsStockOrder as(Name alias) {
        return new StsStockOrder(alias, this);
    }

    @Override
    public StsStockOrder as(Table<?> alias) {
        return new StsStockOrder(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StsStockOrder rename(String name) {
        return new StsStockOrder(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StsStockOrder rename(Name name) {
        return new StsStockOrder(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StsStockOrder rename(Table<?> name) {
        return new StsStockOrder(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<Integer, String, StsStockOrderOrderStatus, Integer, BigDecimal, BigDecimal, Integer, LocalDateTime, LocalDateTime, StsStockOrderExecType, Integer> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function11<? super Integer, ? super String, ? super StsStockOrderOrderStatus, ? super Integer, ? super BigDecimal, ? super BigDecimal, ? super Integer, ? super LocalDateTime, ? super LocalDateTime, ? super StsStockOrderExecType, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function11<? super Integer, ? super String, ? super StsStockOrderOrderStatus, ? super Integer, ? super BigDecimal, ? super BigDecimal, ? super Integer, ? super LocalDateTime, ? super LocalDateTime, ? super StsStockOrderExecType, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
