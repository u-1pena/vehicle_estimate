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

  Optional<UserDetail> CheckAlreadyExistByMobilePhoneNumber(String mobilePhoneNumber);

  Optional<User> checkAlreadyExistByEmail(String email);

  Optional<UserPayment> checkAlreadyExistByCardNumber(String cardNumber);

  void insertUser(User user);

  void insertUserDetail(UserDetail userDetail);

  void insertUserPayment(UserPayment userPayment);
}
