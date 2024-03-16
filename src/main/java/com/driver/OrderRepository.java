package com.driver;

import java.util.*;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,deliveryPartner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            HashSet<String> partnersOrder=partnerToOrderMap.get(partnerId);
            partnersOrder.add(orderId);
            partnerToOrderMap.put(partnerId,partnersOrder);
            //increase order count of partner
            DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
            //assign partner to this order
            orderToPartnerMap.put(orderId,partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        List<String> orders=new ArrayList<>();
        orders.addAll(partnerToOrderMap.get(partnerId));
        return orders;
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        List<String> orders=new ArrayList<>();
        orders.addAll(orderMap.keySet());
        return orders;
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        partnerMap.remove(partnerId);
        for(String orderId:partnerToOrderMap.get(partnerId)){
            orderToPartnerMap.remove(orderId);
        }
        partnerToOrderMap.remove(partnerId);
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderMap.remove(orderId);
        HashSet<String> orders=partnerToOrderMap.get((orderToPartnerMap.get(orderId)));
        orders.remove(orderId);
        partnerToOrderMap.put(orderToPartnerMap.get(orderId),orders);
        orderToPartnerMap.remove(orderId);
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        return orderMap.size()-orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        Integer count=0;
        int currentTime= Integer.parseInt(timeString.substring(0,2))*60+Integer.parseInt(timeString.substring(3,5));
        for(String orderId:partnerToOrderMap.get(partnerId)){
            if(orderMap.get(orderId).getDeliveryTime()>currentTime)
                count++;
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int time=0;
        for(String orderId:partnerToOrderMap.get(partnerId)){
            if(orderMap.get(orderId).getDeliveryTime()>time)
                time=orderMap.get(orderId).getDeliveryTime();
        }
        if(time==0)
            return null;
        int hh=time/60;
        int mm=time%60;
        String lastTime=hh+":"+mm;
        return lastTime;
    }
}