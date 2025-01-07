package yuichi.user.management.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@Mapper
public interface UserRepository {

  //全件取得
  List<User> findAllUsers();

  List<UserDetail> findAllUserDetails();

  List<UserPayment> findAllUserPayments();

  /*検索取得
  idをキーにして取得*/
  Optional<User> findUserById(int id);

  Optional<UserDetail> findUserDetailById(int userId);

  List<UserPayment> findUserPaymentsByUserId(int userId);

  List<User> findByAccountName(String account);

  List<UserDetail> findByFullNameKana(String kana);

  List<UserDetail> findByDetailName(String name);

  List<User> findByEmail(String email);
}
