package com.u1pena.estimateapi.customer.dto.request;

import com.u1pena.estimateapi.common.validator.ValidPlateRegion;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Tag(name = "VehicleUpdateRequest", description = "車両の更新リクエスト")
@Data
public class VehicleUpdateRequest {

  @Schema(description = "車両のナンバープレート地域、存在しない地域名称はValidationにて処理", example = "東京都")
  @ValidPlateRegion
  String plateRegion;

  @Schema(description = "車両のナンバープレートカテゴリ番号", example = "123")
  @NotNull
  @NotBlank
  @Pattern(regexp = "^\\d{3}$", message = "plateCategoryNumberは半角数字で3文字で入力してください")
  private String plateCategoryNumber;

  @Schema(description = "車両のナンバープレートひらがな", example = "た")
  @NotNull
  @NotBlank
  @Pattern(regexp = "^[\\u3040-\\u309F]+$", message = "plateHiraganaはひらがなで入力してください")
  private String plateHiragana;

  @Schema(description = "車両のナンバープレート番号", example = "1234")
  @NotNull
  @NotBlank(message = "空白は許可されていません")
  private String plateVehicleNumber;

  @Schema(description = "車両のメーカー", example = "toyota")
  @Pattern(regexp = "^[a-z]+$", message = "メーカーは半角英小文字で入力してください")
  private String make;

  @Schema(description = "車体番号", example = "NZE141")
  @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "車体番号は半角英小文字とハイフンで入力してください")
  private String model;

  @Schema(description = "エンジン型式", example = "1NZ-FE")
  @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "型式は半角英小文字とハイフンで入力してください")
  private String type;

  @Schema(description = "車両の年式", example = "2020-01")
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "yearはYYYY-MM形式で入力してください")
  private String year;

  @Schema(description = "車両の車検満了日", example = "2024-12-31")
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "inspectionDateはYYYY-MM-DD形式で入力してください")
  private String inspectionDate;

  @Schema(description = "車両のアクティブ状態、廃車時は非アクティブに更新", example = "true")
  private boolean active;
}
