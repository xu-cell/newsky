package com.sky.controller.user;


import com.alibaba.fastjson.JSON;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xujj
 */
@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户下单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private WebSocketServer webSocketServer;
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单，参数为：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);

//        // 测试来订单提醒
//        //通过websocket向客户端浏览器推送消息 type orderId content
//        Map map = new HashMap();
//        map.put("type",1); // 1表示来单提醒 2表示客户催单
//        map.put("orderId",orderSubmitVO.getId());
//        map.put("content","订单号：" + "支付成功可以派送");
//
//        String json = JSON.toJSONString(map);
//        webSocketServer.sendToAllClient(json);

        return Result.success(orderSubmitVO);
    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);

//
//        // 业务处理，修改订单状态，来单提醒
//        // 因为无法调用微信支付接口，所以模拟
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> getHistoryOrders(int page, int pageSize, Integer status) {

         PageResult pageResult = orderService.list(page, pageSize, status);
         return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> getOrderDetails(@PathVariable("id") Long id) {
        OrderVO order = orderService.getOrderDetails(id);
        return Result.success(order);
    }

    /**
     * 用户取消订单
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }


    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 客户催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("客户催单")
    public Result reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }


}
