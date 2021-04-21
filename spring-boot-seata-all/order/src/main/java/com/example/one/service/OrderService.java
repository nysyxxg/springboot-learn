package com.example.one.service;

import com.example.one.dao.TblOneDao;
import com.example.one.dao.TblOrderDao;
import com.example.one.entity.TblOne;
import com.example.one.entity.TblOrder;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


/**
 * @author yueyi2019
 */
@Service
public class OrderService {
	
	@Autowired
	TblOneDao mapper;

	@Autowired
	TblOrderDao tblOrderDao;
	
	@Autowired
	private RestTemplate restTemplate;

	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	public String addOrder() {


		TblOrder tblOrder = new TblOrder();
		tblOrder.setOrderId(1);
		tblOrder.setBuyer("马士兵");
		
		tblOrderDao.insert(tblOrder);

		restTemplate.getForEntity("http://inventory/reduce?goodId=1", null);
		System.out.println(1/0);
		
		return "";
	}
	
	public String rm2Update() {
		rm2UpdateRemote();

		TblOne o = new TblOne();
		o.setId(2);
		o.setName("rm2");
		mapper.insertSelective(o);
//		int i = 1/0;
		return "";
	}
	
	private void rm2() {
		restTemplate.getForEntity("http://two/rm2", null);
	}
	
	private void rm2UpdateRemote() {
		restTemplate.getForEntity("http://two/rm2-update", null);
	}
	
	private void rm3() {
		restTemplate.getForEntity("http://three/rm3", null);
	}
}
