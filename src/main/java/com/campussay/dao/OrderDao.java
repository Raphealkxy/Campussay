package com.campussay.dao;

import com.campussay.model.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface OrderDao {

    /**
     * 根据用户名获得所有订单
     * @param userID 用户id
     *
     * @return
     */
    public List<HashMap> getOrderByUserID(@Param(value = "userID")String userID, @Param(value = "state")String state,@Param(value = "start")int start, @Param(value = "end")int end);

    /**
     * 根据用户名或者状态获得订单的分页数目
     * @param userID
     * @param state
     * @return
     */
    int getOrderByUserIDPageTotal(@Param(value = "userID")String userID, @Param(value = "state")String state);

    /**
     * 根据用户及订单状态获得不同的订单
     * @param userID 用户id
     * @param state 订单状态
     * @return
     */
    public List<HashMap> getOrderByUserIDState(@Param(value = "userID")String userID, @Param(value = "state")String state, @Param(value = "start")int start, @Param(value = "end")int end);

    /**
     * 根据id，更改订单状态
     * @param order
     * @return
     */
    public int updateOrderState(Order order);

    int getMyTalkingSaleInfoCount(@Param("userId") int userId,@Param("state") Integer state);

    List<HashMap<String,Object>> getMyTalkingSaleInfo(int userId,int page,int size,@Param("state") Integer state);
    
    
    
    public void createOrder(Order createOrder);
    
    public HashMap<String,Object> selectOrderByOrderId(String orderId);
    
    //定时任务拿到所有已经创建但未付款的订单
    public List<Map<String,Object>> getAllNotPayOrders();
    
    //定时任务修改创建订单但未支付的订单的状态,认定此订单作废
    public int updateNotPayOrdersState(String order_id);
    //定时任务修改创建订单但未支付的订单对应课程的剩余人数名额,人数++
    public int updateTalkingPeopleByOrder_id(Integer talking_id);
    /**
     * 
     * @Title: paySuccess 
     * @Description: 第三方支付成功后的回调
     * @param strOrderid 订单号
     * @param strTradeNo 第三方支付平台的订单编号
     * @param payWay 第三方支付方法，1-支付宝，2-微信支付
     * @param payTime 支付时间
     * @return: void
     */
    public void paySuccess(String strOrderid, String strTradeNo, int payWay,Date payTime) ;

    /**
     * 根据order的id获取参与者信息
     */
    public HashMap<String,Object> getTalkingInfo(String orderId);


    /**
     * 
     * @Title: checkUserIsCreateOrder 
     * @Description: 检测用户是否已经下过订单
     * @param order
     * @return
     * @return: List<HashMap>
     */
    public List<HashMap>  checkUserIsCreateOrder(Order order);

    /*
    * 订单回复详细人数
    *
    * */

    List<Map> OrderTalking(int orderTalking);
    

    /*
    * 查看付款失败原因
    *
    * */
    Map OrderReason(int orderTalking);
    /**
     * 
     * @Title: selectOrderByStatus 
     * @Description: 根据不同条件查询用户订单信息
     * @param userID
     * @param state
     * @param start
     * @param end
     * @return
     * @return: List<HashMap>
     */
    public List<HashMap> selectOrderByStatus(@Param(value = "userID")String userID, @Param(value = "state")Integer state, @Param(value = "start")int start, @Param(value = "pageSize")int pageSize);
   /**
    * 
    * @Title: selectOrderByStatusPage 
    * @Description:  根据不同条件查询用户订单信息总条数
    * @param userID
    * @param state
    * @return
    * @return: int
    */
    public int selectOrderByStatusPage(@Param(value = "userID")String userID, @Param(value = "state")Integer state);

    /**
     * @Title: updateOrderState 
     * @Description: 修改订单状态
     * @param order_id
     * @param order_state
     * @param order_confirm_time 订单确认时间 可为null
     * @return: void
     */
    public void lpyUpdateOrderState(@Param(value = "order_id")String orderId, @Param(value = "order_state")Integer orderState,@Param(value = "order_confirm_time")Date orderConfirmTime,@Param(value = "order_user")String orderUser );
    
    /**
     * 
     * @Title: requestRefundOrder 
     * @Description: 申请退款
     * @param orderId
     * @param orderState
     * @param order_request_refund_reason
     * @param orderUser
     * @return: void
     */
    public void requestRefundOrder(@Param(value = "order_id")String orderId, @Param(value = "order_state")Integer orderState,@Param(value = "order_request_refund_reason")String order_request_refund_reason,@Param(value = "order_user")String orderUser );


    /**
     * 
     * @Title: selectOrderTitleAndUserName 
     * @Description: 根据订单id查询课程信息
     * @param orderId
     * @return
     * @return: HashMap
     */
   public HashMap selectOrderTitleAndUserName(String orderId);

    /*
    * 课程开始前两个小时内发送短信
    * */
    public List<HashMap> sendPhoneMessage();

    /*
    * 发送短信后修改order发送状态
    *
    * */
    public int setPhoneMessage(String orderId);

    /*
    * 根据talkingId查询用户信息
    *
    * */
    List<Map> mapTalkingLists(int talkingId);
    /**
     * 
     * @Title: updateOrderSwtateToFinish 
     * @Description: 将课程结束且支付了的订单的状态变为完成 状态：3
     * @return: void
     */
    public void updateOrderStateToFinish();

    //订单支付成功
    public Map SuccessPayOrder(String orderId);

    //退款成功
    public Map RefundSuccess(String orderId);

    //退款失败
    public Map RefundFail(String orderId);
    
    //显示购买课程
    public  List<HashMap<String,Object>> BuyOrderSpend(int order_user);
    
    //显示购买课程
    public  List<HashMap<String,Object>> cashrecord(int user_id);
    
    //是否存在订单
    public HashMap<String,Object> isorderstate(int order_user,int order_talking);

    //订单45分钟得到订单
    List<Map> getCloseOrder();
    
    public int updateCloseOrder(String orderId, int state, String closeReason);
    
    public List<HashMap> getMyTalkingSaleRecord(int userId);
    public List<HashMap> getMyOrderRecord(int userId);
    
    
}
