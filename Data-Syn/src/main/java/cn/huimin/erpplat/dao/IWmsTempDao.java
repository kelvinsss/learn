package cn.huimin.erpplat.dao;

/**
 * Created by guochun on 2016/7/6.
 */
public interface IWmsTempDao {


    void insertTmpJuan(String orderno, int t, int pid, String unit, String num, int buyprice, int sellprice, int op, String yufuname);

    void UpdateTmpOutOrder(int oid, String orderno);

    void UpdateTransferIn(Integer tid, String transferno);

    void InsertTmpTransferIn(int id, String transferno, String ctime, int outstoreid, int instoreid, int op, int isok, int ttype);

    void UpdateTmpSaleTransf(int tid, int oid);

    int insertAndReId(String sql);

    void insertTmpSaletransfer(int outId, String orderNo, int outSid, int storeId, int orderId);

    void insertTmpOutstoreOrder(int hmboId, String orderno, String orderTime, int marketId, int storeId, int payMethod, int op, String chktime, int tid, float paym, double orderPrice, int orderType, String  aname, String aphone);

    void insertTmpSaleTransferDetail(int hmopId, int bid,int pid,int unitid,int num, String norm, double sprice, String suitname, int op,String isbulk, int suitnum, int tid,int tdid);

    void InsertTmpTransferInDetails(int id, int orderid, int proid, String prounit, int pronum, String norms, double proprice, int op, int unitid);

    void insertTmpOutStoreDetail(int hmopId, int bid,int pid,int unitid,int num, String norm, double sprice, String suitname, int op, String isbulk, int suitnum, int quannum, int yufunum, String yufuname, String sub_store, int skulevel, int specialid, int ordertype);
}
