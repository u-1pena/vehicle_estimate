package example.maintenance.estimate.customer.dto.request.master;

import example.maintenance.estimate.customer.dto.request.Validator.ValidCarWashSize;
import example.maintenance.estimate.customer.dto.request.Validator.ValidOilViscosity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MaintenanceGuideCreateRequest {

  @NotBlank(message = "メーカー名は必須です")
  @Pattern(regexp = "^[a-z]+$", message = "メーカーは半角英小文字で入力してください")
  private String make;

  @NotBlank(message = "車種名は必須です")
  private String vehicleName;

  @NotBlank(message = "車体番号は必須です")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "車体番号は半角英大文字と数字で入力してください")
  private String model;

  @NotBlank(message = "エンジン型式は必須です")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "エンジン型式は半角英大文字と数字で入力してください")
  private String type;

  @NotBlank(message = "年式は必須です")
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "yearはYYYY-MM形式で入力してください")
  private String startYear;

  @NotBlank(message = "年式は必須です")
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "yearはYYYY-MM形式で入力してください")
  private String endYear;

  @ValidOilViscosity
  private String oilViscosity;

  @Digits(integer = 2, fraction = 1, message = "オイル量は整数部2桁、小数部1桁で入力してください")
  @DecimalMin(value = "0.0", inclusive = false, message = "オイル量は0より大きい数字で入力してください")
  private double oilQuantityWithFilter;

  @Digits(integer = 2, fraction = 1, message = "オイル量は整数部2桁、小数部1桁で入力してください")
  @DecimalMin(value = "0.0", inclusive = false, message = "オイル量は0より大きい数字で入力してください")
  private double oilQuantityWithoutFilter;

  @NotBlank(message = "純正フィルター型番を必ず入力してください")
  private String oilFilterPartNumber;

  @ValidCarWashSize
  private String carWashSize;
}
