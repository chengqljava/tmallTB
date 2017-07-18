package com.cheng.qian.model;

import java.io.Serializable;

public class GoodsIdOuterIdSpec implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String goodsId;
	private String outerId;
	private String goodsSpec;
	private int goodsCount;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getOuterId() {
		return outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	
	
	
	

}
