/**
 * LuckySDKServer
 */
package com.og.platform.pay.data.utils;

import java.io.File;
import java.io.FileWriter;

import com.og.platform.pay.common.utils.HttpTool;

/**
 * @author ogplayer.com
 *
 *         2013年11月2日
 */
public class TestHttp {
	public static void main(String[] args) throws Exception {
		StringBuilder smses = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			IMSIInfo info = DataUtils.getInstance().nextIMSI();
			String reqUrl = URLHelperSZSDT.pieceURL(
					"http://127.0.0.1:25494/if_mtk/service",
					"30000834957501", info.getImei(), info.getImsi(),
					"test@test@test");
			String result = HttpTool.sendHttp(reqUrl, "");
			try {
				String content = URLHelperSZSDT.parseJosn(result, "1", "1")
						.get(0).getContent();
				smses.append(content).append("\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File file1 = new File("C:\\Users\\Administrator\\Desktop\\雷电战机1.txt");
		if (!file1.exists())
			file1.createNewFile();
		FileWriter writer = new FileWriter(file1);
		writer.write(smses.toString());
		writer.flush();
		writer.close();
	}
}
