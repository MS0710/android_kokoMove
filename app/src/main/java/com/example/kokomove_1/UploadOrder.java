package com.example.kokomove_1;

public class UploadOrder {
    private String idKey;
    private String name,thing,size,category,sendMethod,receiveAddress,sendAddress;
    private String orderNumber;
    private String receivePeople,receiveTime,sendPeople,sendTime;

    public UploadOrder(String _idKey,String _name,String _thing,String _size,String _category,String _sendMethod,
                       String _receiveAddress,String _sendAddress,String _orderNumber,String _receivePeople,
                         String _receiveTime,String _sendPeople,String _sendTime){
        this.idKey = _idKey;
        this.name = _name;
        this.thing = _thing;
        this.size = _size;
        this.category = _category;
        this.sendMethod = _sendMethod;
        this.receiveAddress = _receiveAddress;
        this.sendAddress = _sendAddress;
        this.orderNumber = _orderNumber;
        this.receivePeople = _receivePeople;
        this.receiveTime = _receiveTime;
        this.sendPeople = _sendPeople;
        this.sendTime = _sendTime;
    }

    public String getIdKey() {
        return idKey;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public String getSendMethod() {
        return sendMethod;
    }

    public String getSize() {
        return size;
    }

    public String getThing() {
        return thing;
    }

    public String getReceivePeople() {
        return receivePeople;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public String getSendTime() {
        return sendTime;
    }
}
