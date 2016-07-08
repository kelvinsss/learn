package cn.huimin.erpplat.dao.impl;

import cn.huimin.erpplat.dao.IWmsTempDao;
import cn.huimin.erpplat.utils.date.DateFormatEnum;
import cn.huimin.erpplat.utils.date.DateUtil;
import org.light.complet.model.sql.TSQL;
import org.light.data.accessor.IAccessor;
import org.light.data.base.DaoTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by guochun on 2016/7/6.
 */
@Service("accessorWmsTempDao")
public class WmsTempDaoImpl implements IWmsTempDao{

    /**
     * wmstemt数据库操作的接口
     */
    @Resource(name = "temp_accessor")
    private IAccessor tmpAccessor;

    @Override
    public void insertTmpJuan(String orderno, int t, int pid, String unit, String num, int buyprice, int sellprice, int op, String yufuname){
        String sql = "Insert into tmp_order_list_new ( tmp_order_list_new.orderno,tmp_order_list_new.type,tmp_order_list_new.productid,tmp_order_list_new.unit,tmp_order_list_new.num,tmp_order_list_new.buy_price,tmp_order_list_new.sell_price,OperationType,updatetime,yufuname)   values ('" + orderno + "','" + t + "','" + pid + "','" + unit + "','" + num + "','" + buyprice + "','" + sellprice + "','" + op + "','" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','" + yufuname + "')";
        tmpAccessor.execute(new TSQL(sql));
    }


    //更新临时表状态为完成
    @Override
    public void UpdateTmpOutOrder(int oid, String orderno){
        String sql = "update tmp_outstoreorder set isok=1 where HMBO_Id='" + oid + "' AND HMBO_OrderNo='" + orderno + "'";
        tmpAccessor.execute(new TSQL(sql));
    }

    @Override
    public void UpdateTransferIn(Integer tid, String transferno){
        String sql = "update tmp_transferin set isok=1 where transferid='" + tid + "' AND transferno='" + transferno + "'";
        tmpAccessor.execute(new TSQL(sql));
    }

    @Override
    public void InsertTmpTransferIn(int id, String transferno, String ctime, int outstoreid, int instoreid, int op, int isok, int ttype){
        String sql = "insert into tmp_transferin (tmp_transferin.transferid,tmp_transferin.transferno,tmp_transferin.createtime,tmp_transferin.outstoreid,tmp_transferin.instoreid,tmp_transferin.OperationType,tmp_transferin.updatetime,isok,transfertype) values   (" + id + ",'" + transferno + "','" + ctime + "'," + outstoreid + "," + instoreid + "," + op + ",'" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','" + isok + "','" + ttype + "')";
        tmpAccessor.execute(new TSQL(sql));
    }

    //修改临时库调拨单完成状态
    @Override
    public void UpdateTmpSaleTransf(int tid, int oid){
        String sql = "update tmp_saletransfer set isok=1 where transferid='" + tid + "' AND hmboid='" + oid + "'";
        tmpAccessor.execute(new TSQL(sql));
    }


    @Override
    public int insertAndReId(String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        DaoTemplate daoTemplate = tmpAccessor.getDaoTemplate();
        daoTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return generatedId.intValue();
    }

    @Override
    public void insertTmpSaletransfer(int outId, String orderNo, int outSid, int storeId, int orderId) {
        //临时库写销售调拨单
        String sql = "insert into tmp_saletransfer (tmp_saletransfer.transferid,tmp_saletransfer.transferno,tmp_saletransfer.createtime,tmp_saletransfer.outstoreid,tmp_saletransfer.instoreid, tmp_saletransfer.OperationType,tmp_saletransfer.updatetime,tmp_saletransfer.isok,tmp_saletransfer.transfertype,tmp_saletransfer.hmboid)   values ('" + outId + "','" + orderNo + "','" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','" + outSid + "','" + storeId + "','1','" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','0','2','" + orderId + "')";
        tmpAccessor.execute(new TSQL(sql));
    }

    @Override
    public void insertTmpOutstoreOrder(int hmboId, String orderno, String orderTime, int marketId, int storeId, int payMethod, int op, String chktime, int tid, float paym, double orderPrice, int orderType, String aname, String aphone) {
        String sql = "insert into tmp_outstoreorder (HMBO_Id,HMBO_OrderNo,HMBO_OrderTime,HMBO_MarketId,HMBO_StoreId,HMBO_PayMethod,OperationType,updatetime,isok,adminname,adminphone,HMBO_ChkTime,transferinID,transferoutID,HMBO_PayMoney,HMBO_OrderPrice,ordertype)  values (" + hmboId + ",'" + orderno + "','" + orderTime + "'," + marketId + "," + storeId + "," + payMethod + "," + op + ",'" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','0','" + aname + "','" + aphone + "','" + chktime + "','" + tid + "','" + tid + "','" + paym + "','" + orderPrice + "','" + orderType + "')";
        tmpAccessor.execute(new TSQL(sql));
    }

    @Override
    public void insertTmpSaleTransferDetail(int hmopId, int bid,int pid,int unitid,int num, String norm, double sprice, String suitname, int op,String isbulk, int suitnum, int tid,int tdid) {
        String sql = "Insert into tmp_saletransferdetail (HMOP_Id,HMOP_BId,HMOP_PId,BP_SellUnit,HMOP_Num,HMP_Norms,HMOP_Price,HMOP_SuitName,OperationType,updatetime,isbulk,HMOP_SuitNum,transferid,transferdetailid) values (" + hmopId + "," + bid + "," + pid + "," + unitid + "," + num + ",'" + norm + "'," + sprice + ",'" + suitname + "'," + op + ",'" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','" + isbulk + "','" + suitnum + "','" + tid + "','" + tdid + "')";
        tmpAccessor.execute(new TSQL(sql));
    }


    public void InsertTmpTransferInDetails(int id, int orderid, int proid, String prounit, int pronum, String norms, double proprice, int op, int unitid){
        String sql = "insert into tmp_transferindetail (tmp_transferindetail.transferindetailid,tmp_transferindetail.transferinid,tmp_transferindetail.transferproid,tmp_transferindetail.prounitid,tmp_transferindetail.pronum,tmp_transferindetail.pronorms,tmp_transferindetail.proprice,tmp_transferindetail.OperationType,tmp_transferindetail.updatetime) values " +
                " (" + id + "," + orderid + "," + proid + "," + unitid + "," + pronum + ",'" + norms + "'," + proprice + "," + op + ",'" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "')";
        tmpAccessor.execute(new TSQL(sql));
    }

    @Override
    public void insertTmpOutStoreDetail(int hmopId, int bid,int pid,int unitid,int num, String norm, double sprice, String suitname, int op, String isbulk, int suitnum, int quannum, int yufunum, String yufuname, String sub_store, int skulevel, int specialid, int ordertype) {
        String sql = "insert into tmp_outstoredetail (HMOP_Id,HMOP_BId,HMOP_PId,BP_SellUnit,HMOP_Num,HMP_Norms,HMOP_Price,HMOP_SuitName,OperationType,updatetime,isbulk,HMOP_SuitNum,quannum,yufunum,yufuname,pickstore,skulevel,specialid,ordertype) values (" + hmopId + "," + bid + "," + pid + "," + unitid + "," + num + ",'" + norm + "'," + sprice + ",'" + suitname + "'," + op + ",'" + DateUtil.getDate(DateFormatEnum.yyyy_MM_dd_HH_mm_ss) + "','" + isbulk + "','" + suitnum + "','" + quannum + "','" + yufunum + "','" + yufuname + "','" + sub_store + "','" + skulevel + "','" + specialid + "','" + ordertype + "')";
        tmpAccessor.execute(new TSQL(sql));
    }

}
