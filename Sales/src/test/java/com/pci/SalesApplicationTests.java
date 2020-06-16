package com.pci;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pci.security.UserAccountService;

@SpringBootTest
public class SalesApplicationTests {

	@Autowired
	private UserAccountService sv;
	
	@Test
	public void contextLoads() {
		sv.registerAdmin("u001","システム管理者","admin",true);
		sv.registerManager("u002","江戸　一郎","user",true);
		sv.registerStaff("u003","平川　太郎","user",true);
		sv.registerStaff("u004","大手　次郎","user",true);
		sv.registerStaff("u005","桔梗　花子","user",true);
		sv.registerStaff("u006","坂下　幸子","user",true);
		sv.registerStaff("u007","桜田　三郎","user",true);
		sv.registerStaff("u008","田安　健一","user",true);
		sv.registerStaff("u009","清水　緑","user",true);
	}
}
