package service;

import dataaccess.DataAccess;

import javax.xml.crypto.Data;

public class RegisterService {
    private DataAccess dataAccess;

    public RegisterService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
}
