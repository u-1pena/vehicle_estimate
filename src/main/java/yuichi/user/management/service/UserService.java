package yuichi.user.management.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import yuichi.user.management.controller.exception.UserDetailException.AlreadyExistsMobileNumberException;
import yuichi.user.management.controller.exception.UserDetailException.BirthdayInvalidException;
import yuichi.user.management.controller.exception.UserDetailException.UserDetailAlreadyExistsException;
import yuichi.user.management.controller.exception.UserException;
import yuichi.user.management.controller.exception.UserException.UserNotFoundException;
import yuichi.user.management.controller.exception.UserPaymentException.NotExistCardBrandException;
import yuichi.user.management.controller.exception.UserPaymentException.PaymentExpirationInvalidException;
import yuichi.user.management.controller.exception.UserPaymentException.UserPaymentAlreadyExistsException;
import yuichi.user.management.converter.UserCreateConverter;
import yuichi.user.management.converter.UserDetailCreateConverter;
import yuichi.user.management.converter.UserInformationConverter;
import yuichi.user.management.converter.UserPaymentCreateConverter;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.mapper.UserRepository;


@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /*クエリパラメーターがなければ全件取得。@RequestParamがaccount,name,kana,emailの場合
   * 条件検索へ移行するメソッド*/
  public List<UserInformationDto> searchUsersByRequestParam(String account, String name,
      String kana,
      String email) {
    String pramName = defineSearchCriteria(account, name, kana,
        email);//英語弱者のメモ：defineSearchCriteria(検索条件を定義）

    //switch文でパラメーターの可読性アップ！確かに見やすいけど、if文のほうがコードが短くなるので、どちらがいいかは微妙かも・・
    return switch (pramName) {
      case "userAccount" -> findInformationByAccountName(account);
      case "name" -> findUserInformationByName(name);
      case "kana" -> findUserInformationByFullNameKana(kana);
      case "email" -> findUserInformationByEmail(email);
      default -> findAll();
    };
  }

  /*検索条件を定義するメソッド
   * 例：accountがnullでもなく空白でもなければ"accountを返す検索条件を判定するメソッド"*/
  private String defineSearchCriteria(String account, String name, String kana, String email) {
    if (account != null && !account.isBlank()) {
      return "userAccount";
    }
    if (name != null && !name.isBlank()) {
      return "name";
    }
    if (kana != null && !kana.isBlank()) {
      return "kana";
    }
    if (email != null && !email.isBlank()) {
      return "email";
    }
    return "default";
  }

  //findAllUsersをfindAllに変更。findAllUserInformationも考えたが、シンプルに全部を取得するということでfindAllに変更
  public List<UserInformationDto> findAll() {
    List<User> users = findAllUsers();
    List<UserDetail> userDetails = findAllUserDetails();
    List<UserPayment> userPayments = findAllUserPayments();

    Map<Integer, UserDetail> detailMap = userDetails.stream()
        .collect(Collectors.toMap(UserDetail::getId, detail -> detail));

    Map<Integer, List<UserPayment>> paymentMap = userPayments.stream()
        .collect(Collectors.groupingBy(UserPayment::getUserId));

    return users.stream()
        .map(user -> {
          UserDetail detail = detailMap.get(user.getId());
          List<UserPayment> payments = paymentMap.getOrDefault(user.getId(),
              Collections.emptyList());
          return UserInformationConverter.convertToUserInformationDto(user, detail, payments);
        })
        .collect(Collectors.toList());
  }

  private List<User> findAllUsers() {
    return userRepository.findAllUsers();
  }

  private List<UserDetail> findAllUserDetails() {
    return userRepository.findAllUserDetails();
  }

  private List<UserPayment> findAllUserPayments() {
    return userRepository.findAllUserPayments();
  }

  private User findUserById(int id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> new UserNotFoundException("userAccount not found with id: " + id));
  }

  private UserDetail findUserDetailById(int id) {
    return userRepository.findUserDetailById(id)
        .orElseThrow(() -> new UserNotFoundException("userAccount not found with id: " + id));
  }

  private List<UserPayment> findUserPaymentsById(int userId) {
    return userRepository.findUserPaymentsByUserId(userId);
  }

  public List<UserInformationDto> findUserInformationById(int id) {
    User user = findUserById(id);
    UserDetail userDetail = findUserDetailById(id);
    List<UserPayment> userPayments = findUserPaymentsById(id);
    UserInformationDto userInformationDto = UserInformationConverter.convertToUserInformationDto(
        user, userDetail, userPayments);
    return List.of(userInformationDto);
  }

  private List<UserInformationDto> findUserByCriteria(List<User> users) {
    return users.stream()
        .map(this::convertToUserInformationDtoFromUser)
        .collect(Collectors.toList());
  }

  private List<UserInformationDto> findInformationByAccountName(String account) {
    List<User> users = findByAccountName(account);
    return findUserByCriteria(users);
  }

  //ユーザー情報を取得する処理 完全一致のEmailで検索しユーザー情報を照会します
  private List<UserInformationDto> findUserInformationByEmail(String email) {
    Optional<User> user = findByEmail(email);
    List<User> users = user.map(Collections::singletonList)
        .orElse(Collections.emptyList());
    return findUserByCriteria(users);
  }

  private UserInformationDto convertToUserInformationDtoFromUser(User user) {
    UserDetail userDetail = findUserDetailById(user.getId());
    List<UserPayment> userPayments = findUserPaymentsById(user.getId());
    return UserInformationConverter.convertToUserInformationDto(user, userDetail, userPayments);
  }

  private List<User> findByAccountName(String account) {
    List<User> user = userRepository.findByAccountName(account);
    return user;
  }

  private Optional<User> findByEmail(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user;
  }

  private List<UserInformationDto> findUserDetailByCriteria(List<UserDetail> userDetails) {
    return userDetails.stream()
        .map(this::convertToUserInformationDtoFromUserDetail)
        .collect(Collectors.toList());
  }

  private List<UserInformationDto> findUserInformationByName(String name) {
    List<UserDetail> userDetails = findByDetailName(name);
    return findUserDetailByCriteria(userDetails);
  }

  private List<UserInformationDto> findUserInformationByFullNameKana(String kana) {
    List<UserDetail> userDetails = findByFullNameKana(kana);
    return findUserDetailByCriteria(userDetails);
  }

  private UserInformationDto convertToUserInformationDtoFromUserDetail(UserDetail userDetail) {
    User user = findUserById(userDetail.getId());
    List<UserPayment> userPayments = findUserPaymentsById(userDetail.getId());
    return UserInformationConverter.convertToUserInformationDto(user, userDetail, userPayments);
  }

  private List<UserDetail> findByDetailName(String name) {
    List<UserDetail> UserDetail = userRepository.findByDetailName(name);
    return UserDetail;
  }

  private List<UserDetail> findByFullNameKana(String kana) {
    List<UserDetail> UserDetail = userRepository.findByFullNameKana(kana);
    return UserDetail;
  }

  public User registerUser(UserCreateRequest userCreateRequest) {
    checkAlreadyExistEmail(userCreateRequest.getEmail());
    User user = UserCreateConverter.userConvertToEntity(userCreateRequest);
    createUser(user);
    return user;
  }

  private void checkAlreadyExistEmail(String email) {
    userRepository.findByEmail(email)
        .ifPresent(user -> {
          throw new UserException.AlreadyExistsEmailException();
        });
  }

  private void createUser(User user) {
    userRepository.createUser(user);
  }

  public UserDetail registerUserDetail(int id, UserDetailCreateRequest userDetailCreateRequest) {
    User user = findUserById(id);
    checkAlreadyExistUserDetail(id);
    checkAlreadyExistMobilePhoneNumber(userDetailCreateRequest.getMobilePhoneNumber());
    checkBirthDayValid(userDetailCreateRequest.getBirthday());
    UserDetail userDetail = UserDetailCreateConverter.userDetailConvertToEntity(user,
        userDetailCreateRequest);
    createUserDetail(userDetail);
    return userDetail;
  }

  private void checkAlreadyExistUserDetail(int id) {
    userRepository.findUserDetailById(id)
        .map(userDetail -> {
          throw new UserDetailAlreadyExistsException("userDetail already exist with id: " + id);
        });
  }

  private void checkAlreadyExistMobilePhoneNumber(String mobilePhoneNumber) {
    userRepository.findByMobilePhoneNumber(mobilePhoneNumber)
        .ifPresent(userDetail -> {
          throw new AlreadyExistsMobileNumberException();
        });
  }

  private void checkBirthDayValid(String birthday) {
    LocalDate birthDay = LocalDate.parse(birthday);
    LocalDate currentDay = LocalDate.now();
    if (birthDay.isAfter(currentDay)) {
      throw new BirthdayInvalidException();
    }
  }

  private void createUserDetail(UserDetail userDetail) {
    userRepository.createUserDetail(userDetail);
  }

  public UserPayment registerUserPayment(int id,
      UserPaymentCreateRequest userPaymentCreateRequest) {
    UserDetail userDetail = findUserDetailById(id);
    checkAlreadyExistCardNumber(userPaymentCreateRequest.getCardNumber());
    checkExpirationDate(userPaymentCreateRequest.getExpirationDate());
    UserPayment userPayment = UserPaymentCreateConverter.userPaymentConvertToEntity(userDetail,
        userPaymentCreateRequest, this);
    createUserPayment(userPayment);
    return userPayment;
  }

  private void checkAlreadyExistCardNumber(String cardNumber) {
    userRepository.findByCardNumber(cardNumber)
        .ifPresent(userPayment -> {
          throw new UserPaymentAlreadyExistsException();
        });
  }

  /*
   * クレジットカードのブランドを識別するメソッド
   * 今後の修正で、クレジットカードのブランドが追加される可能性があるため、
   * 修正をしやすくするためにもクラスを別に作成してもいいかもしれない
   */
  private static final Map<String, String> cardBrands = Map.ofEntries(
      /*
       *VISA
       * ---------------------------------------------------------
       * VISAのプレフィックスは以下の通り
       * 4桁のみ：4 -> MapのcardBrands
       *
       *MasterCard
       * ---------------------------------------------------------
       * MasterCardのプレフィックスは以下の通り
       * 4桁の場合：2221-2720（こちらは追加された識別番号）
       * 1桁：5 -> MapのcardBrands
       *
       *AmericanExpress
       * ---------------------------------------------------------
       * AmericanExpressのプレフィックスは以下の通り
       * 2桁のみ：34、37 -> MapのcardBrands
       *
       *  *UnionPay
       * ---------------------------------------------------------
       * UnionPayのプレフィックスは以下の通り
       * 2桁のみ：62 -> MapのcardBrands
       *
       * その他のブランドはプレフィックスが1桁から2桁のみのため、
       * 1桁のプレフィックスがMapに登録されている場合は、
       * そのブランドを返す(VISA、AmericanExpress)
       *
       */
      Map.entry("4", "VISA"),
      Map.entry("5", "MasterCard"),
      Map.entry("34", "AmericanExpress"),
      Map.entry("37", "AmericanExpress"),
      Map.entry("62", "UnionPay")
  );

  private static final Map<String, String> cardBrandByDiners = Map.ofEntries(
      /*
       *Diners
       * ---------------------------------------------------------
       * Dinersのプレフィックスは以下の通り
       * 3桁の場合：300-305、3095 -> MapのcardBrandByDiners
       * 2桁のみ：36、38、39 -> MapのcardBrand2digits
       */
      Map.entry("36", "Diners"),
      Map.entry("38", "Diners"),
      Map.entry("39", "Diners"),
      Map.entry("300", "Diners"),
      Map.entry("301", "Diners"),
      Map.entry("302", "Diners"),
      Map.entry("303", "Diners"),
      Map.entry("304", "Diners"),
      Map.entry("305", "Diners"),
      Map.entry("3095", "Diners")
  );

  private static final Map<String, String> cardBrandByDiscover = Map.ofEntries(
      /*
       *Discover
       * ---------------------------------------------------------
       * Discoverのプレフィックスは以下の通り
       * 6桁の場合：601186-601199、601174-601179
       * 5桁の場合：60112-60114、60110
       * 3桁の場合：644-649
       * 2桁の場合：65
       */
      Map.entry("65", "Discover"),
      Map.entry("644", "Discover"),
      Map.entry("645", "Discover"),
      Map.entry("646", "Discover"),
      Map.entry("647", "Discover"),
      Map.entry("648", "Discover"),
      Map.entry("649", "Discover"),
      Map.entry("60110", "Discover"),
      Map.entry("60112", "Discover"),
      Map.entry("60113", "Discover"),
      Map.entry("60114", "Discover")
  );

  public String identifyCardBrand(String cardNumber) {
    /*
     * 6桁から1桁までの数字を取得し、ブランドを判定する
     * 特定の文字列に対してはMapで定義したブランドを返す
     * 範囲指定の場合は、範囲内に入っているかを判定し、範囲内に入っていればブランドを返す
     */
    for (int length = Math.min(cardNumber.length(), 6); length >= 1; length--) {
      String prefix = cardNumber.substring(0, length);

      if (Integer.parseInt(cardNumber.substring(0, 6)) >= 601186
          && Integer.parseInt(cardNumber.substring(0, 6)) <= 601199) {
        return "Discover";

      } else if (Integer.parseInt(cardNumber.substring(0, 6)) >= 601174
          && Integer.parseInt(cardNumber.substring(0, 6)) <= 601179) {
        return "Discover";

      } else if (Integer.parseInt(cardNumber.substring(0, 5)) >= 60112
          && Integer.parseInt(cardNumber.substring(0, 5)) <= 60114) {
        return "Discover";

      } else if (Integer.parseInt(cardNumber.substring(0, 5)) == 60110) {
        return "Discover";

      } else if (cardBrandByDiscover.containsKey(prefix)) {
        return cardBrandByDiscover.get(prefix);

        /*
         *JCB
         * ---------------------------------------------------------
         * JCBのプレフィックスは以下の通り
         * 4桁のみ：3528-3589
         */
      } else if (Integer.parseInt(cardNumber.substring(0, 4)) >= 3528
          && Integer.parseInt(cardNumber.substring(0, 4)) <= 3589) {
        return "JCB";

        /*
         *MasterCard
         * ---------------------------------------------------------
         * MasterCardのプレフィックスは以下の通り
         * 4桁の場合：2221-2720（こちらは追加された識別番号）
         */
      } else if (Integer.parseInt(cardNumber.substring(0, 4)) >= 2221
          && Integer.parseInt(cardNumber.substring(0, 4)) <= 2720) {
        return "MasterCard";

      } else if (cardBrandByDiners.containsKey(prefix)) {
        return cardBrandByDiners.get(prefix);

      } else if (cardBrands.containsKey(prefix)) {
        return cardBrands.get(prefix);
      }
    }
    //何にも該当しない場合は登録できないブランドとしてエラーを返す
    throw new NotExistCardBrandException();
  }

  private void createUserPayment(UserPayment userPayment) {
    userRepository.createUserPayment(userPayment);
  }

  //クレジットカードの有効期限をチェックするメソッド
  private void checkExpirationDate(String expirationDate) {
    YearMonth expDate = YearMonth.parse(expirationDate);
    YearMonth currentMonth = YearMonth.now();
    if (!expDate.isAfter(currentMonth) && !expDate.equals(currentMonth)) {
      throw new PaymentExpirationInvalidException();
    }
  }
}
