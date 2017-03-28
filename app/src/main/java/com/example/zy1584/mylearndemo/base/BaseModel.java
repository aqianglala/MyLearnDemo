package com.example.zy1584.mylearndemo.base;

import com.example.zy1584.mylearndemo.http.Http;
import com.example.zy1584.mylearndemo.http.HttpService;
import com.example.zy1584.mylearndemo.mvp.IModel;

public class BaseModel implements IModel {
    protected static HttpService httpService;

    //初始化httpService
    static {
        httpService = Http.getHttpService();
    }

}
