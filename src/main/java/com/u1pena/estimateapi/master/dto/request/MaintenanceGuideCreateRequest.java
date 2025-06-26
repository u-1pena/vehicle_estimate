package com.u1pena.estimateapi.master.dto.request;

import com.u1pena.estimateapi.common.validator.ValidCarWashSize;
import com.u1pena.estimateapi.common.validator.ValidOilViscosity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MaintenanceGuideCreateRequest {

  @Schema(description = "車両メーカー", example = "toyota")
  @NotBlank(message = "メーカー名は必須です")
  @Pattern(regexp = "^[a-z]+$", message = "メーカーは半角英小文字で入力してください")
  private String make;
  @Schema(description = "車両名", example = "プリウス")
  @NotBlank(message = "車種名は必須です")
  private String vehicleName;
  @Schema(description = "車体番号", example = "ZVW30")
  @NotBlank(message = "車体番号は必須です")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "車体番号は半角英大文字と数字で入力してください")
  private String model;
  @Schema(description = "エンジン型式", example = "2ZR-FXE")
  @NotBlank(message = "エンジン型式は必須です")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "エンジン型式は半角英大文字と数字で入力してください")
  private String type;
  @Schema(description = "年式開始", example = "2020-01")
  @NotBlank(message = "年式は必須です")
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "yearはYYYY-MM形式で入力してください")
  private String startYear;
  @Schema(description = "年式終了", example = "2024-12")
  @NotBlank(message = "年式は必須です")
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "yearはYYYY-MM形式で入力してください")
  private String endYear;
  @Schema(description = "指定オイル粘度", example = "0w-20")
  @ValidOilViscosity
  private String oilViscosity;
  @Schema(description = "オイル量（フィルターあり）", example = "3.5")
  @Digits(integer = 2, fraction = 1, message = "オイル量は整数部2桁、小数部1桁で入力してください")
  @DecimalMin(value = "0.0", inclusive = false, message = "オイル量は0より大きい数字で入力してください")
  private double oilQuantityWithFilter;
  @Schema(description = "オイル量（フィルターなし）", example = "3.0")
  @Digits(integer = 2, fraction = 1, message = "オイル量は整数部2桁、小数部1桁で入力してください")
  @DecimalMin(value = "0.0", inclusive = false, message = "オイル量は0より大きい数字で入力してください")
  private double oilQuantityWithoutFilter;
  @Schema(description = "純正オイルフィルター型番", example = "12345-67890")
  @NotBlank(message = "純正フィルター型番を必ず入力してください")
  private String oilFilterPartNumber;
  @Schema(description = "洗車サイズ", example = "M")
  @ValidCarWashSize
  private String carWashSize;
}
