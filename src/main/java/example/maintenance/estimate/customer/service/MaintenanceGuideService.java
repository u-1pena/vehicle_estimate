package example.maintenance.estimate.customer.service;

import example.maintenance.estimate.customer.controller.exception.MasterException;
import example.maintenance.estimate.customer.controller.exception.MasterException.MaintenanceGuideAlreadyExistsException;
import example.maintenance.estimate.customer.converter.master.MaintenanceGuideCreateConverter;
import example.maintenance.estimate.customer.converter.master.ProductCategoryCreateConverter;
import example.maintenance.estimate.customer.converter.master.ProductCreateConverter;
import example.maintenance.estimate.customer.dto.request.master.MaintenanceGuideCreateRequest;
import example.maintenance.estimate.customer.dto.request.master.ProductCategoryCreateRequest;
import example.maintenance.estimate.customer.dto.request.master.ProductCreateRequest;
import example.maintenance.estimate.customer.entity.master.GuideProductPermission;
import example.maintenance.estimate.customer.entity.master.MaintenanceGuide;
import example.maintenance.estimate.customer.entity.master.Product;
import example.maintenance.estimate.customer.entity.master.ProductCategory;
import example.maintenance.estimate.customer.mapper.MasterRepository;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceGuideService {

  MasterRepository masterRepository;

  public MaintenanceGuideService(MasterRepository masterRepository) {
    this.masterRepository = masterRepository;
  }

  /**
   * 指定されたリクエスト情報をもとにメンテナンスガイドを作成し、 関連するガイド用製品パーミッションも登録します。
   *
   * <p>この処理は以下のステップを行います:
   * <ul>
   *   <li>メンテナンスガイドのエンティティ作成と登録</li>
   *   <li>オイル粘度に基づく製品パーミッションの作成</li>
   *   <li>洗車サイズに基づく製品パーミッションの作成</li>
   *   <li>オイルフィルター品番に基づく製品パーミッションの作成</li>
   * </ul>
   *
   * @param maintenanceGuideCreateRequest メンテナンスガイド作成リクエスト
   * @return 作成された {@link MaintenanceGuide} エンティティ
   */
  public MaintenanceGuide registerMaintenanceGuide(
      MaintenanceGuideCreateRequest maintenanceGuideCreateRequest) {
    MaintenanceGuide maintenanceGuide = MaintenanceGuideCreateConverter
        .maintenanceBaseCreateConvertToEntity(maintenanceGuideCreateRequest);
    maintenanceGuideAlreadyExists(maintenanceGuide);
    createMaintenanceGuide(maintenanceGuide);
    registerProductPermission(maintenanceGuide);
    return maintenanceGuide;
  }

  /**
   * MaintenanceGuideを作成する
   *
   * @param maintenanceGuide MaintenanceGuide
   */
  private void createMaintenanceGuide(MaintenanceGuide maintenanceGuide) {
    masterRepository.createMaintenanceGuide(maintenanceGuide);
  }

  /**
   * メンテナンスガイドが既に存在するか確認する。 存在する場合は、ExistAlreadyMasterExceptionをスローする。
   *
   * @param maintenanceGuide MaintenanceGuide
   * @description 検索はMake、Model、Type、Yearで行われます。
   */
  private void maintenanceGuideAlreadyExists(MaintenanceGuide maintenanceGuide) {
    masterRepository.findMaintenanceGuideByMakeAndModelAndTypeAndYear(maintenanceGuide)
        .ifPresent(existingGuide -> {
          throw new MaintenanceGuideAlreadyExistsException();
        });
  }

  /**
   * 商品の権限を登録する
   *
   * @param maintenanceGuide MaintenanceGuide
   */
  private void registerProductPermission(MaintenanceGuide maintenanceGuide) {
    createGuideProductPermission(maintenanceGuide,
        g -> findProductByOilViscosity(g.getOilViscosity()));
    createGuideProductPermission(maintenanceGuide,
        g -> masterRepository.findProductByCarWashSize(g.getCarWashSize()));
    createGuideProductPermission(maintenanceGuide,
        g -> findProductByOilFilterPartNumber(g.getOilFilterPartNumber()));
  }

  /**
   * 商品カテゴリを作成する
   *
   * @param productCategoryCreateRequest 商品カテゴリ作成リクエスト
   * @return 作成した商品カテゴリ
   */
  public ProductCategory registerProductCategory(ProductCategoryCreateRequest
      productCategoryCreateRequest) {

    ProductCategory productCategory = ProductCategoryCreateConverter
        .productCategoryConvertToEntity(productCategoryCreateRequest);
    productCategoryAlreadyExists(productCategory);
    createProductCategory(productCategory);
    return productCategory;
  }

  private void createProductCategory(ProductCategory productCategory) {
    masterRepository.createProductCategory(productCategory);
  }

  private void productCategoryAlreadyExists(ProductCategory productCategory) {
    masterRepository.findProductCategoryByName(productCategory.getCategoryName())
        .ifPresent(existingCategory -> {
          throw new MasterException
              .ProductCategoryAlreadyExistsException();
        });
  }

  /**
   * 商品を作成する
   *
   * @param productCreateRequest 商品作成リクエスト
   * @return 作成した商品
   */
  public Product registerProduct(ProductCreateRequest productCreateRequest) {
    Product product = ProductCreateConverter
        .productCreateConvertToEntity(productCreateRequest);
    createProduct(product);
    return product;
  }

  private void createProduct(Product product) {
    masterRepository.createProduct(product);
  }

  /**
   * 商品をオイル粘度で取得する
   *
   * @param oilViscosity オイル粘度
   * @return オイル粘度に対応する商品リスト
   */
  private List<Product> findProductByOilViscosity(String oilViscosity) {
    List<Product> product = masterRepository.findProductByOilViscosity(oilViscosity);
    return product;
  }

  /**
   * 商品をoilFilterPartNumberで取得する
   *
   * @param oilFilterPartNumber オイルフィルター純正品番
   * @return 対応するオイルフィルターリスト
   */
  private List<Product> findProductByOilFilterPartNumber(String oilFilterPartNumber) {
    return masterRepository.findProductByOilFilterPartNumber(oilFilterPartNumber);
  }

  /**
   * 中間テーブルを作成する。商品の権限を登録する。
   *
   * @param maintenanceGuide メンテナンスガイド
   */
  private void createGuideProductPermission(MaintenanceGuide maintenanceGuide,
      Function<MaintenanceGuide, List<Product>> productFetcher) {
    List<Product> products = productFetcher.apply(maintenanceGuide);
    products.stream()
        .map(product -> new GuideProductPermission
            (maintenanceGuide.getMaintenanceId(),
                product.getCategoryId(), product.getProductId()))
        .forEach(permission -> masterRepository
            .createGuideProductPermission(maintenanceGuide.getMaintenanceId()
                , permission.getCategoryId(), permission.getProductId()));
  }
}
