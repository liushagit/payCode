/**
 * OGPaySystem
 */
package com.og.platform.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.og.platform.pay.common.domain.OGMMPayConf;
import com.og.platform.pay.common.domain.OGPayDirect;
import com.og.platform.pay.common.domain.OGPayInfo;
import com.payinfo.net.database.ConnectionResource;
import com.payinfo.net.database.IJuiceDBHandler;

/**
 * @author ogplayer.com
 *
 * 2013年10月29日
 */
public class OGPayDAO extends ConnectionResource {
	private static IJuiceDBHandler<OGPayInfo> HANDLER_OGPAY = new IJuiceDBHandler<OGPayInfo>() {
		@Override
		public OGPayInfo handler(ResultSet rs) throws SQLException {
			OGPayInfo info = new OGPayInfo();
			info.setBillId(rs.getString("bill_id"));
			info.setBillType(rs.getInt("bill_type"));
			info.setCodeNO(rs.getString("code_no"));
			info.setCodeType(rs.getInt("code_type"));
			info.setPrice(rs.getString("price"));
			info.setUrl(rs.getString("url"));
			info.setUrlNO(rs.getInt("url_no"));
			info.setPriority(rs.getInt("priority"));
			info.setOpen(rs.getBoolean("is_open"));
			info.setProvinceType(rs.getInt("province_type"));
			info.setProvinceHide(rs.getString("province_hide"));
			info.setProvinceOpen(rs.getString("province_open"));
			info.setExtData(rs.getString("extData"));
			info.setCompany(rs.getString("company"));
			return info;
		}
	};
	
	public OGPayInfo getUrlPayInfo(String billId, int codeType) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_ogpay_info where code_type=? and bill_id=? ");
		return queryForObject(sql.toString(), HANDLER_OGPAY, codeType, billId);
	}

	public OGPayInfo getPayInfo(String billId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_ogpay_info where bill_id=?");
		return queryForObject(sql.toString(), HANDLER_OGPAY, billId);
	}
	
	public List<OGPayInfo> getAllBillInfo(int codeType) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_ogpay_info where code_type=? and is_open>0");
		return queryForList(sql.toString(), HANDLER_OGPAY, codeType);
	}
	
	
	private static IJuiceDBHandler<OGMMPayConf> HANDLER_OGMMPAY = new IJuiceDBHandler<OGMMPayConf>() {
		@Override
		public OGMMPayConf handler(ResultSet rs) throws SQLException {
			OGMMPayConf info = new OGMMPayConf();
			info.setId(rs.getInt("id"));
			info.setAppName(rs.getString("app_name"));
			info.setMemo(rs.getString("memo"));
			info.setCodeNo(rs.getString("code_no"));
			info.setCodeType(rs.getInt("code_type"));
			info.setUrl(rs.getString("url"));
			info.setIsOpen(rs.getInt("is_open"));
			info.setProvinceType(rs.getInt("province_type"));
			info.setProvinceHide(rs.getString("province_hide"));
			info.setProvinceOpen(rs.getString("province_open"));
			info.setExtData(rs.getString("extData"));
			return info;
		}
	};
	
	public OGMMPayConf checkMMPayChannel(String appName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_ogmmpay_conf where app_name=? and is_open>0");
		return queryForObject(sql.toString(), HANDLER_OGMMPAY, appName);
	}
	
	
	private static IJuiceDBHandler<OGPayDirect> HANDLER_OGPAY_DIRECT = new IJuiceDBHandler<OGPayDirect>() {
		@Override
		public OGPayDirect handler(ResultSet rs) throws SQLException {
			OGPayDirect info = new OGPayDirect();
			info.setAppName(rs.getString("app_name"));
			info.setBillId(rs.getString("bill_id"));
			return info;
		}
	};
	
	public OGPayDirect getOGPayDirect(String appName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_ogpay_direct where app_name=?");
		return queryForObject(sql.toString(), HANDLER_OGPAY_DIRECT, appName);
	}
}
