package cn.huimin.erpplat.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by guochun on 2016/7/5.
 */
public interface ErpOrderDao {

    /**
     * 根据订单id查新订单信息,实现内有特殊处理：orderType  not in (30,31,32)
     * @param orderId 订单id
     * @return 封装了订单信息的map  key是字段名  value是字段值
     * @throws Exception
     */
    Map<String, Object> getOrderInfoById(int orderId);

    /**
     * 根据仓库id查询对应的 storehouse信息
     * @param storeId 仓库id
     * @return storehouse信息,目前只包括MyCenterStore,branch_Id两个字段
     * @throws Exception
     */
    Map<String, Object> getStoreHoseInfo(int storeId);

    /**
     * 根据订单id查询订单明细信息
     * @param orderId 订单id
     * @return 订单明细信息的list  list的元素是封装了订单明细信息的map   key是字段名  value是字段值
     * @throws Exception
     */
    List<Map<String, Object>>  getOrderProductsByOrderId(int orderId);

    /**
     * 根据商品id和仓库id查询该商品是整货还是散货
     * @param bpId 商品id
     * @param storeId 仓库id
     * @return null：没有该商品，1：整货，2：散货
     * @throws Exception
     */
    Integer  getIsBulkByPidAndStoreId(int bpId, int storeId);

    /**
     * 根据订单编号查询优惠信息(hm_order_list_new),其他条件  type>0 AND state=1
     * @param orderNo 订单编号
     * @return 优惠信息的list  list的元素是封装了优惠信息的map   key是字段名  value是字段值
     * @throws Exception
     */
    List<Map<String, Object>>  getListNewInfoByOrderNo(String orderNo);

    Map<String, Object>  getAllotStorageById(int id);

    List<Map<String,Object>> getAllotProductByAllotId(String allotId);

    int GetQuannum(String orderno, int proid);

    String getProSub_store(int storeid, int proid);

    int insertAndReId(String sql);

    String GetCTStoreid(int storeid);

    List<Map<String , Object>>  getProductsByBid(int bid);

    List<Map<String, Object>>  getPackProductByPid(Integer pid);

    List<Map<String, Object>>  getPresentByPid(Integer pid);

    List<Map<String, Object>>  getHmPrizeByMarketId(int marketId);

    int  insertAllotstorageAndReId(String orderNo, int outSide, int storeId, int branchId, int orderId);

    int insertTransferDetail(int tid, int pid, String proname, String norms, String unit, int num, double buyprice, int cv);

    String  getAllotIdById(int id);

    String GetSaleAdminInfo(int marketid);

    List<Map<String, Object>> getOrderProductsByOrderIdWithProduct(int orderId);

    int GetUintId(String unitname);

}
