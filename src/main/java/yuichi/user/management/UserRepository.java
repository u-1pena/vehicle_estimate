package yuichi.user.management;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {

  @Select("SELECT * FROM users")
  List<User> findAll();

}
