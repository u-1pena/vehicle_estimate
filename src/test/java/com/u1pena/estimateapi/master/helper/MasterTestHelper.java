package com.u1pena.estimateapi.master.helper;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.master.dto.request.MaintenanceGuideCreateRequest;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class MasterTestHelper {

  /**
   * MasterTestHelperクラスは、主にMaster関係のヘルパークラスです。
   *
   * <p>このクラスは、メンテナンスガイドの製品やmaintenanceGuideを作成するためのモックデータを提供します。
   */


  /**
   * メンテナンスガイドを作成する
   * <p>このメソッドは、メンテナンスガイドのモックデータを作成します。</p>
   *
   * @return メンテナンスガイドのリスト
   */
  public List<MaintenanceGuide> maintenanceGuideMock() {
    return List.of(
        new MaintenanceGuide(1, "toyota", "アクア",
            "TEST1234", "EG-1234", YearMonth.parse("2000-10"),
            YearMonth.parse("2020-01"), "0w-20", 3.5,
            3.0, "12345",
            CarWashSize.S),
        new MaintenanceGuide(2, "nissan", "ノート",
            "ABC123-456789", "1AB-CD", YearMonth.parse("2020-03"),
            YearMonth.parse("2024-03"), "5w-30", 4.0,
            3.5, "67890",
            CarWashSize.M),
        new MaintenanceGuide(3, "honda", "フィット",
            "XYZ987-654321", "1AB-CD", YearMonth.parse("2010-01"),
            YearMonth.parse("2015-12"), "0w-16", 4.5,
            4.0, "54321",
            CarWashSize.L));
  }

  public MaintenanceGuideCreateRequest maintenanceGuideCreateRequestMock() {
    MaintenanceGuideCreateRequest maintenanceGuideCreateRequestMock = new MaintenanceGuideCreateRequest();
    maintenanceGuideCreateRequestMock.setMake("toyota");
    maintenanceGuideCreateRequestMock.setVehicleName("Corolla");
    maintenanceGuideCreateRequestMock.setModel("TEST-123");
    maintenanceGuideCreateRequestMock.setType("AZ-FE");
    maintenanceGuideCreateRequestMock.setStartYear("2020-01");
    maintenanceGuideCreateRequestMock.setEndYear("2023-01");
    maintenanceGuideCreateRequestMock.setOilViscosity("0w-20");
    maintenanceGuideCreateRequestMock.setOilQuantityWithFilter(4.5);
    maintenanceGuideCreateRequestMock.setOilQuantityWithoutFilter(4.0);
    maintenanceGuideCreateRequestMock.setOilFilterPartNumber("12345");
    maintenanceGuideCreateRequestMock.setCarWashSize("L");
    return maintenanceGuideCreateRequestMock;
  }

  /**
   * メンテナンスガイドの製品を作成する
   * <p>このメソッドは、オイルの粘度に基づく製品を作成します。</p>
   *
   * @return メンテナンスガイドのオイル製品リスト
   */
  public List<Product> productOilMock() {
    return List.of(
        new Product(
            1, 1, "ハイグレードオイル_0w-20",
            "化学合成油_0w-20", "0w-20", BigDecimal.valueOf(2800.0)),
        new Product(
            2, 1, "ハイグレードオイル_5w-30",
            "化学合成油_5w-30", "5w-30", BigDecimal.valueOf(2800.0)),
        new Product(
            3, 1, "ハイグレードオイル_5w-40",
            "化学合成油_5w-40", "5w-40", BigDecimal.valueOf(2800.0)),
        new Product(
            4, 1, "ハイグレードオイル_0w-16",
            "化学合成油_0w-16", "0w-16", BigDecimal.valueOf(2800.0)));
  }

  /**
   * メンテナンスガイドの製品を作成する
   * <p>このメソッドは、洗車のサイズに基づく製品を作成します。</p>
   *
   * @return メンテナンスガイドの洗車製品リスト
   */

  public List<Product> productCarWashMock() {
    //
    return List.of(
        new Product(5, 3, "手洗い洗車",
            "手洗い洗車SSサイズ", "SS", BigDecimal.valueOf(1900.0)),
        new Product(6, 3, "手洗い洗車",
            "手洗い洗車Sサイズ", "S", BigDecimal.valueOf(2900.0)),
        new Product(7, 3, "手洗い洗車",
            "手洗い洗車Mサイズ", "M", BigDecimal.valueOf(3900.0)),
        new Product(8, 3, "手洗い洗車",
            "手洗い洗車Lサイズ", "L", BigDecimal.valueOf(4900.0)),
        new Product(9, 3, "手洗い洗車",
            "手洗い洗車LLサイズ", "LL", BigDecimal.valueOf(5900.0)),
        new Product(10, 3, "手洗い洗車",
            "手洗い洗車XLサイズ", "XL", BigDecimal.valueOf(6900.0)));
  }

  /**
   * メンテナンスガイドの製品を作成する
   * <p>このメソッドは、オイルフィルターの品番に基づく製品を作成します。</p>
   *
   * @return メンテナンスガイドのオイルフィルター製品リスト
   */
  public List<Product> productOilFilterMock() {
    return List.of(
        new Product(11, 2, "オイルフィルター",
            "オイルフィルター品番12345", "12345", BigDecimal.valueOf(1000.0)),
        new Product(12, 2, "オイルフィルター",
            "オイルフィルター品番67890", "67890", BigDecimal.valueOf(1200.0)));
  }

}
