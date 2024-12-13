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
  List<User> findAllUsers();

  @Select("SELECT * FROM user_details")
  List<UserDetail> findAllUserDetails();

  @Select("SELECT * FROM user_payments")
  List<UserPayment> findAllUserPayments();

  /*検索取得
  idをキーにして取得*/
  @Select("SELECT * FROM users WHERE id = #{id}")
  Optional<User> findUserById(int id);

  @Select("SELECT * FROM user_details WHERE id = #{id}")
  Optional<UserDetail> findUserDetailById(int userId);

  @Select("SELECT * FROM user_payments WHERE user_id = #{userId}")
  List<UserPayment> findUserPaymentsByUserId(int userId);

  @Select("SELECT * FROM users WHERE account LIKE CONCAT('%', #{account}, '%')")
  List<User> findByAccountName(String account);

  //各種検索
  //カナ検索
  @Select("SELECT *FROM user_details WHERE CONCAT(last_name_kana, first_name_kana) LIKE CONCAT('%', #{kana}, '%')")
  List<UserDetail> findByFullNameKana(String kana);

  //ユーザー名検索
  @Select("SELECT * FROM user_details WHERE CONCAT(last_name, first_name) LIKE CONCAT('%', #{name}, '%')")
  List<UserDetail> findByDetailName(String name);

  @Select("SELECT * FROM users WHERE email = #{email}")
  List<User> findByEmail(String email);
}
