package cn.huimin.erpplat.dao.impl;

import cn.huimin.erpplat.dao.ErpOrderDao;
import cn.huimin.erpplat.utils.date.DateFormatEnum;
import cn.huimin.erpplat.utils.date.DateUtil;
import org.light.complet.model.sql.TSQL;
import org.light.data.accessor.IAccessor;
import org.light.data.base.DaoTemplate;
import org.light.util.collection.ListUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by guochun on 2016/7/5.
 */
@Service("accessorErpOrderDao")
public class ErpOrderDaoImpl implements ErpOrderDao {
    /**
     * supermarket数据库写操作
     */
    @Resource(name = "write_accessor")
    private IAccessor iAccessorW;

    /**
     * supermarket数据库读操作
     */
    @Resource(name = "read_accessor")
    private IAccessor iAccessorR;


    @Override
    public Map<String, Object> getOrderInfoById(int orderId){
        //查询订单信息（根据订单id和orderType  not in (30,31,32)）
        String  sqlPattern =   "SELECT hmsellorders.HMBO_Id, hmsellorders.HMBO_OrderNo, hmsellorders.HMBO_OrderTime, hmsellorders.HMBO_MarketId,   hmsellorders.HMBO_StoreId, hmsellorders.HMBO_PayMethod, hmsellorders.HMBO_ChkTime, hmsellorders.HMBO_Capacity, hmsellorders.HMBO_OrderPrice FROM hmsellorders WHERE hmsellorders.HMBO_Id = '{0}' AND hmsellorders.HMBO_OrderType != 30 AND hmsellorders.HMBO_OrderType != 31 AND hmsellorders.HMBO_OrderType != 32";
        String sql = MessageFormat.format(sqlPattern, orderId);
        List<Map<String, Object>> orderList = iAccessorR.query4List(new TSQL(sql));
        if(ListUtil.isEmpty(orderList)){
            return null;
        }
        return orderList.get(0);
    }

    @Override
    public Map<String, Object>  getStoreHoseInfo(int storeId){
        //查寻当前订单所属仓库对应的中心仓
        String sqlPattern = "select MyCenterStore, branch_Id from storehouse where Id='{0}' ";//HMBO_StoreId
        String sql = MessageFormat.format(sqlPattern, storeId);
        List<Map<String, Object>>  results  = iAccessorR.query4List(new TSQL(sql));
        if(ListUtil.isEmpty(results)){
            return null;
        }
        return results.get(0);
    }

    @Override
    public List<Map<String, Object>> getOrderProductsByOrderIdWithProduct(int orderId){
        //根据订单id查询订单明细
        String sqlPattern = "SELECT hmsellorderproduct.HMOP_Id,hmsellorderproduct.HMOP_BId,hmsellorderproduct.HMOP_PId,hmsellorderproduct.HMP_Unit,hmsellorderproduct.HMOP_Num,hmsellorderproduct.HMP_Norms,hmsellorderproduct.HMOP_Price,hmsellorderproduct.HMOP_Bargin,HMOP_IsWhole,hmproducts.HMP_NewType FROM hmsellorderproduct INNER JOIN hmproducts ON hmsellorderproduct.HMOP_PId = hmproducts.HMP_Id WHERE hmsellorderproduct.HMOP_BId =  {0}";
        String sql = MessageFormat.format(sqlPattern, orderId);
        return iAccessorR.query4List(new TSQL(sql));
    }


    @Override
    public List<Map<String, Object>> getOrderProductsByOrderId(int orderId){
        //根据订单id查询订单明细
        String sqlPattern = "SELECT hmsellorderproduct.HMOP_Id, hmsellorderproduct.HMOP_BId, hmsellorderproduct.HMOP_PId, hmsellorderproduct.HMP_Unit, hmsellorderproduct.HMOP_Num, hmsellorderproduct.HMP_Norms, hmsellorderproduct.HMOP_Price, hmsellorderproduct.HMOP_Bargin, HMOP_IsWhole FROM hmsellorderproduct WHERE hmsellorderproduct.HMOP_BId = {0};";
        String sql = MessageFormat.format(sqlPattern, orderId);
        return iAccessorR.query4List(new TSQL(sql));
    }


    @Override
    public Integer getIsBulkByPidAndStoreId(int bpId, int storeId){
        String sqlPattern = "SELECT storepronum.isbulk FROM storepronum WHERE storepronum.BP_Id='{0}' AND storepronum.store_Id='{1}' ";
        String sql  = MessageFormat.format(sqlPattern, bpId, storeId);
        List<Map<String, Object>> results = iAccessorR.query4List(new TSQL(sql));
        if(ListUtil.isEmpty(results)){
            return null;
        }
        Integer isBulk = (Integer)results.get(0).get("isbulk");
        return isBulk;
    }

    @Override
    public List<Map<String, Object>> getListNewInfoByOrderNo(String orderNo){
        String sqlPattern = "SELECT hm_order_list_new.orderid,hm_order_list_new.type,hm_order_list_new.productid,hm_order_list_new.unit,hm_order_list_new.num,hm_order_list_new.buy_price,hm_order_list_new.sell_price FROM hm_order_list_new WHERE type>0 AND state=1 AND hm_order_list_new.orderid='{0}' ";
        String sql = MessageFormat.format(sqlPattern, orderNo);
        List<Map<String, Object>> results = iAccessorR.query4List(new TSQL(sql));
        return results;
    }

    @Override
    public Map<String, Object> getAllotStorageById(int id){
        String sqlPattern = "SELECT allotstorage.id,allotstorage.allot_id,allotstorage.allot_time,allotstorage.outstorage_id,allotstorage.instorage_id,transftype FROM allotstorage WHERE allotstorage.id = {0}";
        String sql = MessageFormat.format(sqlPattern, id);
        List<Map<String, Object>> results = iAccessorR.query4List(new TSQL(sql));
        if(ListUtil.isEmpty(results)){
            return null;
        }
        return results.get(0);
    }

    @Override
    public List<Map<String, Object>> getAllotProductByAllotId(String allotId){
        String sqlPattern = "SELECT allotproducts.id,allotproducts.allot_id,allotproducts.HMP_id,allotproducts.HMP_Unit,allotproducts.allotNums,allotproducts.HMP_SellNorms,allotproducts.HMP_BuyPrice FROM allotproducts WHERE allotproducts.allot_id =  '{0}'";
        String sql = MessageFormat.format(sqlPattern, allotId);
        List<Map<String, Object>> results = iAccessorR.query4List(new TSQL(sql));
        return results;
    }

    public int GetQuannum(String orderno, int proid){
        String sql = "SELECT count(hm_order_list_new.productid) as pnum FROM hm_order_list_new WHERE state=1 AND hm_order_list_new.orderid='" + orderno + "' AND hm_order_list_new.productid='" + proid + "' AND hm_order_list_new.type = 2";
        return iAccessorR.query4Int(sql);
    }


    //获取sub_store
    public String getProSub_store(int storeid, int proid){
        String sql = "SELECT storepronum.sub_Store_id FROM storepronum WHERE storepronum.BP_Id='" + proid + "' AND storepronum.store_Id='" + storeid + "' ";
        return iAccessorR.query4Object(sql, String.class);
    }



    @Override
    public int insertAndReId(String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        DaoTemplate daoTemplate = iAccessorW.getDaoTemplate();
        daoTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement( Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return generatedId.intValue();
    }


    //查询对应中心库
    public String GetCTStoreid(int storeid){
        String result = "";
        String sql = "SELECT storehouse.MyCenterStore,storehouse.branch_Id FROM storehouse WHERE storehouse.Id='" + storeid + "'";
        List<Map<String, Object>> storehouseList = iAccessorR.query4List(new TSQL(sql));
        if (ListUtil.isEmpty(storehouseList)){return  result;}
        Map<String, Object> storehouse = storehouseList.get(0);
        String storehouse_0 = (String) storehouse.get("MyCenterStore");
        String storehouse_1 = (String) storehouse.get("branch_Id");
        result = storehouse_0 + "#" + storehouse_1;
        return result;
    }

    @Override
    public List<Map<String, Object>> getProductsByBid(int bid) {
        String sql = "SELECT hmsellorderproduct.HMOP_Id,hmsellorderproduct.HMOP_BId, hmsellorderproduct.HMOP_PId,hmsellorderproduct.HMP_Unit, hmsellorderproduct.HMOP_Num,hmsellorderproduct.HMP_Norms, hmsellorderproduct.HMOP_Price,hmsellorderproduct.HMOP_Bargin, HMOP_IsWhole FROM hmsellorderproduct WHERE hmsellorderproduct.HMOP_BId = " + bid;
        return iAccessorR.query4List(new TSQL(sql));
    }

    @Override
    public List<Map<String, Object>> getPackProductByPid(Integer pid) {
        //分解套餐商品
        String sqlt = "SELECT packageproduct.ProductId,package.PackageName,branchproducts.BP_SellUnit,branchproducts.BP_SellNorms,packageproduct.ProductNum,package.PackagePrice FROM packageproduct   Inner Join package ON packageproduct.PackageId = package.PackageId   Inner Join branchproducts ON package.BranchId = branchproducts.branch_Id AND packageproduct.ProductId = branchproducts.HMP_Id AND branchproducts.HMP_Bargain = 0 AND branchproducts.BP_IsWhole != 2   WHERE packageproduct.PackageId = " + pid;
        return  iAccessorR.query4List(new TSQL(sqlt));
    }

    @Override
    public List<Map<String, Object>> getPresentByPid(Integer pid) {
        //分解套餐赠品
        String sqltp = "SELECT present.proid,package.PackageName,present.unit,present.norms,presentpro.presentnum FROM presentpro   Inner Join present ON presentpro.presentId = present.pid   Inner Join package ON presentpro.PackageId = package.PackageId   WHERE presentpro.PackageId = " + pid;
        return iAccessorR.query4List(new TSQL(sqltp));
    }

    @Override
    public List<Map<String, Object>> getHmPrizeByMarketId(int marketId) {
        //检测该客户是否有积分商品
        String sqljf = "SELECT hmprize.proid,hmprize.prizeName,hmprize.prizeUnit,hmprize.prizeNorms,hmsellorderprize.prizenum FROM hmsellorderprize   Inner Join hmprize ON hmsellorderprize.hmprizeid = hmprize.id WHERE hmsellorderprize.Marketid = " + marketId + " AND   hmsellorderprize.prizeState = 1  AND hmsellorderprize.peihuo = -1 ";
        return iAccessorR.query4List(new TSQL(sqljf));
    }

    @Override
    public int insertAllotstorageAndReId(String orderNo, int outSide, int storeId, int branchId, int orderId) {
        String sql = "Insert  into allotstorage (allot_id,allot_time,allot_Summoney,outstorage_id,instorage_id,branch_Id,allotMan_id,allot_state,transftype,saleorderid) values('" + orderNo + "','" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "'" + ", 0 ," + outSide + "," + storeId + "," + branchId + ",'-139',1,2,'" + orderId + "')";
        return insertAndReId(sql);
    }

    @Override
    public int insertTransferDetail(int tid, int pid, String proname, String norms, String unit, int num, double buyprice, int cv) {
        String allotId = getAllotIdById(tid);
        String sql = "Insert  into allotproducts (allot_id,HMP_id,HMP_name,HMP_SellNorms,HMP_Unit,allotNums,HMP_BuyPrice,RealityInstock,RealityOutstock,CGUnitConvert) values('" + allotId + "','" + pid + "'" + ",'" + proname + "','" + norms + "','" + unit + "'," + num + ",'" + buyprice + "',0,'" + num + "','" + cv + "')";
        return insertAndReId(sql);
    }

    @Override
    public String getAllotIdById(int id) {
        String sql = "SELECT allotstorage.allot_id FROM allotstorage WHERE allotstorage.id='" + id + "'";
        return  iAccessorR.query4Object(sql, String.class);
    }

    @Override
    public String GetSaleAdminInfo(int marketid) {
        String result = "";
        String sql = "SELECT supermarket.s_lineId FROM supermarket WHERE supermarket.S_Id='" + marketid + "'";
        int lineid = 0;
        List<Map<String, Object>> list = iAccessorR.query4List(new TSQL(sql));
        if(!ListUtil.isEmpty(list)){
            lineid = (Integer) list.get(0).get("s_lineId");
        }
        if (lineid <= 0){return  "";}
        sql = "SELECT admin.AdminName,admin.AdminTelphone FROM admin INNER JOIN sellline ON admin.AdminId = sellline.adminid WHERE sellline.lineid='" + lineid + "'";
        List<Map<String, Object>> adminList =  iAccessorR.query4List(new TSQL(sql));
        if (ListUtil.isEmpty(adminList)){return "";}
        Map<String, Object> tmp = adminList.get(0);
        String tmp_0 = (String) tmp.get("AdminName");
        String tmp_1 = (String) tmp.get("AdminTelphone");
        result += tmp_0 + "#" + tmp_1;
        return result;
    }



    //根据单位名称调用存储过程得到单位编码
    @SuppressWarnings("unchecked")
    public int GetUintId(String unitname){
        int unitid = 0;//返回的值
        unitid = (Integer) iAccessorR.getDaoTemplate().execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call getUnitId(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, unitname);// 设置输入参数的值
                        return cs;
                    }
                }, new CallableStatementCallback<Object>() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getInt(2);// 获取输出参数的值
                    }
                });
        return unitid;
    }


}
