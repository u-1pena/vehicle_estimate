package yuichi.user.management.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

/*
 * YearMonth型をDBに保存するためのTypeHandler
 * YearMonth型をStringに変換して保存する
 */
@MappedTypes(YearMonth.class)
public class YearMonthTypeHandler extends BaseTypeHandler<YearMonth> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, YearMonth parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.toString());
  }

  @Override
  public YearMonth getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return value != null ? YearMonth.parse(value) : null;
  }

  @Override
  public YearMonth getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return value != null ? YearMonth.parse(value) : null;
  }

  @Override
  public YearMonth getNullableResult(java.sql.CallableStatement cs, int columnIndex)
      throws SQLException {
    String value = cs.getString(columnIndex);
    return value != null ? YearMonth.parse(value) : null;
  }
}
