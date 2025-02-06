package yuichi.user.management.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@Mapper
public interface UserRepository {

  List<User> findAllUsers();

  List<UserDetail> findAllUserDetails();

  List<UserPayment> findAllUserPayments();

  Optional<User> findUserById(int id);

  Optional<UserDetail> findUserDetailById(int id);

  List<UserPayment> findUserPaymentsByUserId(int userId);

  List<User> findByAccountName(String account);

  List<UserDetail> findByFullNameKana(String kana);

  List<UserDetail> findByDetailName(String name);

  List<User> findByEmail(String email);

  Optional<UserDetail> findByMobilePhoneNumber(String mobilePhoneNumber);

  Optional<User> findExistByEmail(String email);

  Optional<UserPayment> findByCardNumber(String cardNumber);

  void createUser(User user);

  void createUserDetail(UserDetail userDetail);

  void createUserPayment(UserPayment userPayment);
}
