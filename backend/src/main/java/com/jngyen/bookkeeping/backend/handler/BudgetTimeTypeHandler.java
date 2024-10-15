package com.jngyen.bookkeeping.backend.handler;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BudgetTimeType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class BudgetTimeTypeHandler extends BaseTypeHandler<BudgetTimeType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BudgetTimeType parameter, JdbcType jdbcType) 
            throws SQLException {
        ps.setString(i, parameter.name());  // 使用枚举名
    }

    @Override
    public BudgetTimeType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : BudgetTimeType.valueOf(value); // 从列名获取值并转为枚举
    }

    @Override
    public BudgetTimeType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : BudgetTimeType.valueOf(value); // 从列索引获取值并转为枚举
    }

    @Override
    public BudgetTimeType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : BudgetTimeType.valueOf(value); // 从存储过程结果获取值
    }
}
