/**
 * OGPaySystem
 */
package com.og.platform.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.og.platform.pay.common.domain.MMUnpopularity;
import com.og.platform.pay.common.domain.MsgCenter;
import com.og.platform.pay.common.domain.OGPayDirect;
import com.og.platform.pay.common.domain.ZYHDInfo;
import com.og.platform.pay.common.vo.ProritySMSInfoVO;
import com.og.platform.pay.common.vo.SMSInfoVO;
import com.payinfo.net.database.ConnectionResource;
import com.payinfo.net.database.IJuiceDBHandler;

/**
 * @author ogplayer.com
 *
 * 2013年11月3日
 */
public class CommonDAO extends ConnectionResource {
	private static final IJuiceDBHandler<MsgCenter> HANDLER_CENTER = new IJuiceDBHandler<MsgCenter>() {
		@Override
		public MsgCenter handler(ResultSet rs) throws SQLException {
			MsgCenter info = new MsgCenter();
			info.setCenterNO(rs.getString("center_no"));
			info.setCity(rs.getInt("city"));
			info.setCountry(rs.getString("country"));
			return info;
		}
	};
	private static final IJuiceDBHandler<OGPayDirect> HANDLER_DIRECT = new IJuiceDBHandler<OGPayDirect>() {
		@Override
		public OGPayDirect handler(ResultSet rs) throws SQLException {
			OGPayDirect info = new OGPayDirect();
			info.setAppName(rs.getString("app_name"));
			info.setBillId(rs.getString("bill_id"));
			return info;
		}
	};
	
	public MsgCenter queryCenter(String centerNO) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_message_center where center_no like '"+centerNO+"%'");
		return queryForObject(sql.toString(), HANDLER_CENTER);
	}
	
	public OGPayDirect queryBill(String appName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_ogpay_direct where app_name=?");
		return queryForObject(sql.toString(), HANDLER_DIRECT,appName);
	}
	
	public void addInstruction(ProritySMSInfoVO psmsList) {
		SMSInfoVO vo = psmsList.getSmsList().get(0);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_instruction_info(bill_id, company, order_id, mno, price, ")
		.append("create_time, update_time) values(?,?,?,?,?,now(),now())");
		saveOrUpdate(sql.toString(), psmsList.getBillId(), vo.getCompany(), vo.getOrderNO(), 
				psmsList.getCodeType(), psmsList.getPrice());
	}
	
	private static final IJuiceDBHandler<MMUnpopularity> HANDLER_UNPOP = new IJuiceDBHandler<MMUnpopularity>() {
		@Override
		public MMUnpopularity handler(ResultSet rs) throws SQLException {
			MMUnpopularity info = new MMUnpopularity();
			info.setId(rs.getInt("id"));
			info.setImei(rs.getString("imei"));
			info.setImsi(rs.getString("imsi"));
			return info;
		}
	};
	
	public MMUnpopularity queryUnpopularityInfo(String imsi, String imei) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_unpopularity_info ");
		return queryForObject(sql.toString(), HANDLER_UNPOP);
	}
	
	public void addUnpopularityInfo(String imsi, String imei) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_unpopularity_info(imsi, imei, ")
			.append("create_time, update_time) values(?,?,now(),now())");
		saveOrUpdate(sql.toString(), imsi, imei);
	}
	
	
	
	private static final IJuiceDBHandler<ZYHDInfo> HANDLER_ZYHD_INFO = new IJuiceDBHandler<ZYHDInfo>() {
		@Override
		public ZYHDInfo handler(ResultSet rs) throws SQLException {
			ZYHDInfo info = new ZYHDInfo();
			info.setId(rs.getInt("id"));
			info.setExtData(rs.getString("ext_data"));
			return info;
		}
	};
	
	public int addZYHDInfo(String extData){
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_zyhd_info(ext_data,create_time,update_time) ")
		.append("values(?,now(),now())");
		saveOrUpdate(sql.toString(), extData);
		
		sql = new StringBuilder();
		sql.append("select * from t_zyhd_info where ext_data=?");
		
		ZYHDInfo info = queryForObject(sql.toString(), HANDLER_ZYHD_INFO,extData);
		int id = -1;
		if(info != null){
			id = info.getId();
		}
		return id;
	}
}
