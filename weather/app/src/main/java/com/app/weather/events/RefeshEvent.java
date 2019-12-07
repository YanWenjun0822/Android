package com.app.weather.events;

/**
 * @author: 105032016090颜文君
 * @description: 下拉刷新事件
 * @data: 2019/5/26
 */
public class RefeshEvent {
    private int postion;//位置
    private String city;//城市

    public RefeshEvent(int postion, String city) {
        this.postion = postion;
        this.city = city;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "RefeshEvent{" +
                "postion=" + postion +
                ", city='" + city + '\'' +
                '}';
    }
}
