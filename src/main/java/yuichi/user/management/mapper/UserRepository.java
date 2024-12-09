package yuichi.user.management.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@Mapper
public interface UserRepository {

  //全件取得
  @Select("SELECT * FROM users")
  List<User> findUser();


  @Select("SELECT * FROM user_details")
  List<UserDetail> findUserDetail();

  @Select("SELECT * FROM user_payments")
  List<UserPayment> findUserPayment();

  /*検索取得
  idをキーにして取得*/
  @Select("SELECT * FROM users WHERE id = #{id}")
  Optional<User> findUserById(int id);

  @Select("SELECT * FROM user_details WHERE id = #{id}")
  Optional<UserDetail> findUserDetailById(int userId);

  @Select("SELECT * FROM user_payments WHERE user_id = #{userId}")
  List<UserPayment> findAllPaymentsByUserId(int userId);

  @Select("SELECT * FROM users WHERE account LIKE CONCAT('%', #{account}, '%')")
  List<User> searchByAccountName(String account);

  //各種検索
  //カナ検索
  @Select("SELECT *FROM user_details WHERE CONCAT(last_name_kana, first_name_kana) LIKE CONCAT('%', #{kana}, '%')")
  List<UserDetail> searchByFullNameKana(String kana);

  //ユーザー名検索
  @Select("SELECT * FROM user_details WHERE CONCAT(last_name, first_name) LIKE CONCAT('%', #{name}, '%')")
  List<UserDetail> searchByDetailName(String name);

  @Select("SELECT * FROM users WHERE email = #{email}")
  List<User> searchByEmail(String email);

}
