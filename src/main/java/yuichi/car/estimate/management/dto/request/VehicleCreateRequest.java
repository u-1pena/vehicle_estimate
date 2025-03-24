package yuichi.car.estimate.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import yuichi.car.estimate.management.dto.request.Validator.ValidPlateRegion;

@Data
public class VehicleCreateRequest {

  @ValidPlateRegion
  String plateRegion;
  @NotNull
  @NotBlank
  @Pattern(regexp = "^\\d{3}$", message = "plateCategoryNumberは半角数字で3文字で入力してください")
  private String plateCategoryNumber;
  @NotNull
  @NotBlank
  @Pattern(regexp = "^[\\u3040-\\u309F]+$", message = "plateHiraganaはひらがなで入力してください")
  private String plateHiragana;
  @NotNull
  @NotBlank(message = "空白は許可されていません")
  private String plateVehicleNumber;
  @Pattern(regexp = "^[a-z]+$", message = "メーカーは半角英小文字で入力してください")
  private String make;
  @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "車体番号は半角英小文字とハイフンで入力してください")
  private String model;
  @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "型式は半角英小文字とハイフンで入力してください")
  private String type;
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "yearはYYYY-MM形式で入力してください")
  private String year;
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "inspectionDateはYYYY-MM-DD形式で入力してください")
  private String inspectionDate;
}
