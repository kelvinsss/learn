package cn.huimin.erpplat.service.impl;


import cn.huimin.erpplat.dao.ErpOrderDao;
import cn.huimin.erpplat.dao.IWmsTempDao;
import cn.huimin.erpplat.service.IDataSynService;
import cn.huimin.erpplat.utils.LogUtil;
import cn.huimin.erpplat.utils.date.DateFormatEnum;
import cn.huimin.erpplat.utils.date.DateUtil;
import org.light.complet.model.sql.TSQL;
import org.light.data.accessor.IAccessor;
import org.light.util.collection.ListUtil;
import org.light.util.collection.MapUtil;
import org.light.util.lang.StringUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
@Service("defaultDataSynService")
public class DataSynServiceImpl implements IDataSynService {

    @Resource(name = "accessorErpOrderDao")
    private ErpOrderDao erpOrderDao;

    @Resource(name = "accessorWmsTempDao")
    private IWmsTempDao wmsTempDao;


    //销售出库单
	@Override
    public void getOutOrderInfo(int sellorderid){
        int tid = 0;
        Map<String, Object> orderInfo = erpOrderDao.getOrderInfoById(sellorderid);
        if(MapUtil.isEmpty(orderInfo)){
            LogUtil.info("sellorderid为 " + sellorderid +" 的订单不存在!");
            return ;
        }
        Integer hmboId = (Integer) orderInfo.get("HMBO_Id");
        String orderNo = (String) orderInfo.get("HMBO_OrderNo");
        String orderTime = (orderInfo.get("HMBO_OrderTime")).toString();
        Integer marketId = (Integer) orderInfo.get("HMBO_MarketId");
        Integer storeId = (Integer) orderInfo.get("HMBO_StoreId");
        Integer payMethod = (Integer) orderInfo.get("HMBO_PayMethod");
        String chkTime = ( orderInfo.get("HMBO_ChkTime")).toString();
        Float payMoney = (Float) orderInfo.get("HMBO_Capacity");
        Double orderPrice = (Double) orderInfo.get("HMBO_OrderPrice");

        Map<String,Object> storeHoseInfo = erpOrderDao.getStoreHoseInfo(storeId);

        int centerStore = 0;
        if(!MapUtil.isEmpty(storeHoseInfo)){
            centerStore = (Integer) storeHoseInfo.get("MyCenterStore");
        }
        //考虑整散混合仓
        if (centerStore > 0){
            boolean b = ChkTransfer(hmboId, storeId, marketId);//判断该销售单是否存在散货
            if (b){//销售单中存在散货
                //根据订单ID及仓库创建调拨单据到散货中心仓
                tid = CreateSaleTranf(hmboId, storeId);
            }
        }
        float  paymoney = payMoney != null ? payMoney : 0f;
        double orderprice = orderPrice != null ? orderPrice : 0d;

        String tmpinfo = "";
        tmpinfo = erpOrderDao.GetSaleAdminInfo(marketId);
        String aname = "";
        String aphone = "";
        if (tmpinfo != null && tmpinfo.length() > 0){
            aname = tmpinfo.split("#")[0];
            aphone = tmpinfo.split("#")[1];
        }
        wmsTempDao.insertTmpOutstoreOrder(hmboId, orderNo, orderTime, marketId, storeId, payMethod, 1, chkTime, tid, paymoney, orderprice, 120, aname, aphone);
        List<Map<String, Object>> detailList =   erpOrderDao.getOrderProductsByOrderIdWithProduct(sellorderid);
        if (ListUtil.isEmpty(detailList)) {
            return ;
        }
        String suitname = "";

        for (int i = 0; i < detailList.size(); i++){
            Map<String, Object> detail = detailList.get(i);
            Integer hmopId = (Integer) detail.get("HMOP_Id");
            Integer hmopBid = (Integer) detail.get("HMOP_BId");
            Integer hmopPid = (Integer) detail.get("HMOP_PId");
            String hmpUnit = (String) detail.get("HMP_Unit");
            Integer hmopNum = (Integer) detail.get("HMOP_Num");
            String hmopNorms = (String) detail.get("HMP_Norms");
            Double hmopPrice = (Double) detail.get("HMOP_Price");
            Integer hmopBargin = (Integer) detail.get("HMOP_Bargin");
            Integer hmopIsWhole = (Integer) detail.get("HMOP_IsWhole");
            Integer hmpNewType = (Integer) detail.get("HMP_NewType");

            int pnum = hmopNum;
            int quannum = 0;
            int yufunum = 0;
            String yufuname = "";
            if (hmopBargin != 99){
                //将明细中添加预付款和券
                quannum = erpOrderDao.GetQuannum(String.valueOf(hmboId), hmopPid);
                hmopNorms = hmopNorms != null ? hmopNorms : "";
                InsertTmpOutstoredetail(hmopId, hmopBid, hmopPid, hmpUnit, hmopNum, hmopNorms, hmopPrice, suitname, 1, storeId, hmopNum, tid,
                        quannum, yufunum, yufuname, hmpNewType, 0, 0, 120);
            }
        }
        GetJuan(orderNo);
        //修改标识明细已经写完
        wmsTempDao.UpdateTmpOutOrder(hmboId, orderNo);
        wmsTempDao.UpdateTmpSaleTransf(tid, hmboId);
        GetInTransferInfos(tid);
        return ;
    }



    //根据销售订单创建销售调拨单
    public int CreateSaleTranf(int orderid, int storeid){
        int outId = 0;
        Random random = new Random();
        String orderNo = "ST" + orderid + DateUtil.getDate(DateFormatEnum.yyyyMMddHHmmss) + random.nextInt(10);//销售调拨单编号
        String tmpstr = erpOrderDao.GetCTStoreid(storeid);
        if (!StringUtil.isEmpty(tmpstr)) {
            int outsid = Integer.parseInt(tmpstr.split("#")[0]);
            int branchId = Integer.parseInt(tmpstr.split("#")[1]);
            //写ERP库
            outId = erpOrderDao.insertAllotstorageAndReId(orderNo, outsid,storeid, branchId, orderid);
            //临时库写销售调拨单
            wmsTempDao.insertTmpSaletransfer(outId,orderNo,outsid,storeid,orderid);
        }
        return outId;
    }



    //根据订单ID判断该订单是否有散货
    public boolean ChkTransfer(int b2borderid, int storeid, int marketid){
            List<Map<String, Object>> detailList = erpOrderDao.getProductsByBid(b2borderid);
            if (!ListUtil.isEmpty(detailList)){
                for (int i = 0; i < detailList.size(); i++){
                	Map<String, Object> detail = detailList.get(i);
                	Integer pid = (Integer) detail.get("HMOP_PId");
                	Integer bargin = (Integer) detail.get("HMOP_Bargin");

                    if (bargin != 99){
                        if ("2".equals(GetProIsbulk(storeid, pid))){
                            return true;
                        }
                    }else{
                        List<Map<String, Object>> packageproductList = erpOrderDao.getPackProductByPid(pid);
                        if (!ListUtil.isEmpty(packageproductList)) {
                            for (int k = 0; k < packageproductList.size(); k++){
                            	Map<String, Object> tmptc = packageproductList.get(k);
                            	Integer tmptc_0 = (Integer) tmptc.get("ProductId");
                                pid = tmptc_0;
                                if ("2".equals(GetProIsbulk(storeid, pid))){
                                    return true;
                                }
                            }
                        }
                        //分解套餐赠品
                        List<Map<String, Object>> presentproList = erpOrderDao.getPresentByPid(pid);
                        if (!ListUtil.isEmpty(presentproList)){
                            for (int j = 0; j < presentproList.size(); j++){
                            	Map<String, Object> tmptcp = presentproList.get(j);
                            	Integer tmptcp_0 = (Integer) tmptcp.get("proid");
                                pid = tmptcp_0;
                                if ("2".equals(GetProIsbulk(storeid, pid))){
                                   return true;
                                }
                            }
                        }
                    }
                }
            }
            //检测该客户是否有积分商品
            List<Map<String, Object>> hmsellorderprizeList = erpOrderDao.getHmPrizeByMarketId(marketid);
            if (!ListUtil.isEmpty(hmsellorderprizeList)) {
                for (int l = 0; l < hmsellorderprizeList.size(); l++) {
                    Map<String, Object> tmpjf = hmsellorderprizeList.get(l);
                    Integer pid = (Integer) tmpjf.get("proid");
                    if ("2".equals(GetProIsbulk(storeid, pid))) {
                        return  true;
                    }
                }
            }
        return false;
    }


    //插入订单明细行
    //20160614更新sub_store
    public int InsertTmpOutstoredetail(int hmopId, int bid, int pid, String unitname, int num, String norm, double sprice, String suitname, int op, int storeid, int suitnum, int tid,
                                       int quannum, int yufunum, String yufuname, int newtype, int skulevel, int specialid, int ordertype){
        int result = 0;
        int tdid = 0;
        int unitid = erpOrderDao.GetUintId(unitname);//用ERP单位找对应的ID码
        String isbulk = "1";
        isbulk = GetProIsbulk(storeid, pid);//根据商品ID及所属仓库查找对应整单
        String sub_store = erpOrderDao.getProSub_store(storeid, pid);
        if(StringUtil.isEmpty(sub_store)){
            sub_store = String.valueOf(storeid);
        }
        if ("0".equals(isbulk.trim())){
            isbulk = "1";
        }
        switch (newtype){
            //蔬菜
            case 20: isbulk = "3"; break;
            case 39: isbulk = "3"; break;
            case 45: isbulk = "3"; break;
            //彩票
            // case 27: isbulk = "3"; break;
        }
        wmsTempDao.insertTmpOutStoreDetail(hmopId,bid,pid,unitid,num,norm,sprice,suitname,op,isbulk,suitnum,quannum,yufunum,yufuname,sub_store,skulevel,specialid,ordertype);
        if ("2".equals(isbulk) && tid > 0){
            tdid = erpOrderDao.insertTransferDetail(tid, pid, "", norm, unitname, num, 0, 0);
            wmsTempDao.insertTmpSaleTransferDetail(hmopId,bid,pid,unitid,num,norm,sprice,suitname,op,isbulk,suitnum,tid,tdid);
        }
        return result;
    }

    //根据销售订单编号获取电子卷信息
    public void GetJuan(String orderno){
        List<Map<String, Object>> hm_order_list_newList = erpOrderDao.getListNewInfoByOrderNo(orderno);
        if (ListUtil.isEmpty(hm_order_list_newList)){    return;    }
        for (int i = 0; i < hm_order_list_newList.size(); i++){
                Map<String, Object> tmp = hm_order_list_newList.get(i);
                String orderId = (String)tmp.get("orderid");
                Integer type = (Integer)tmp.get("type");
                Integer productId = (Integer)tmp.get("productid");
                String unit = (String)tmp.get("unit");
                String num = (String)tmp.get("num");
                Integer buyPrice = (Integer) tmp.get("buy_price");
                Integer sellPrice = (Integer) tmp.get("sell_price");
                String yufuname = "";
                if (type == 1 || type == 4){
                    yufuname = unit;
                } else {
                    yufuname = "";
                }
            wmsTempDao.insertTmpJuan(orderId, type, productId, unit, num, buyPrice, sellPrice, 1, yufuname);
        }
    }


	//调拨入
	public void GetInTransferInfos(int id) {
       Map<String, Object> allotstorage = erpOrderDao.getAllotStorageById(id);
        if (MapUtil.isEmpty(allotstorage)) { return; }
        Integer ID = (Integer) allotstorage.get("id");
        String allotId = (String) allotstorage.get("allot_id");
        String allotTime = (String) allotstorage.get("allot_time");
        Integer outStorageId = (Integer) allotstorage.get("outstorage_id");
        Integer inStorageId = (Integer) allotstorage.get("instorage_id");
        Integer transfType = (Integer) allotstorage.get("transftype");
        int ttype = 0;
        ttype = transfType != null ? transfType : 0;
        wmsTempDao.InsertTmpTransferIn(ID, allotId, allotTime, outStorageId, inStorageId, 1, 0, ttype);
        List<Map<String, Object>> allotproductsList = erpOrderDao.getAllotProductByAllotId(allotId);
        if (ListUtil.isEmpty(allotproductsList)){ return; }
        for (int i = 0; i < allotproductsList.size(); i++){
            Map<String, Object> allotproducts = allotproductsList.get(i);
            Integer productId = (Integer) allotproducts.get("id");
            Integer hmpId = (Integer) allotproducts.get("HMP_id");
            String hmpUnit = (String) allotproducts.get("HMP_Unit");
            Integer allotNums = (Integer) allotproducts.get("allotNums");
            String hmpSellNorms = (String) allotproducts.get("HMP_SellNorms");
            Double hmpByPrice = (Double) allotproducts.get("HMP_BuyPrice");
            hmpUnit = (!StringUtil.isEmpty(hmpUnit)) ? hmpUnit : "";
            hmpSellNorms = (!StringUtil.isEmpty(hmpSellNorms)) ? hmpSellNorms : "";
            int unitid = erpOrderDao.GetUintId(hmpUnit);
            wmsTempDao.InsertTmpTransferInDetails(productId, id, hmpId, hmpUnit, allotNums, hmpSellNorms, hmpByPrice, 1, unitid);
        }
        wmsTempDao.UpdateTransferIn(ID, allotId);
    }


    //获取整散
    public String GetProIsbulk(int storeid, int proid){
        Integer isBulk = erpOrderDao.getIsBulkByPidAndStoreId(proid,storeid);
        return isBulk.toString();
    }


}
