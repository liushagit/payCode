/**
 * Juice
 * com.juice.orange.game.bootstrap
 * ServerBootstrap.java
 */
package com.og.platform.pay.view;

import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import bsh.Interpreter;

import com.payinfo.net.bootstrap.BootstrapProperties;
import com.payinfo.net.bootstrap.DataBaseProperties;
import com.payinfo.net.container.Container;
import com.payinfo.net.log.LoggerFactory;
import com.payinfo.net.rmi.JuiceRemoteManager;
import com.payinfo.net.rmi.RemoteConfig;
import com.payinfo.net.server.IJuiceServer;
import com.payinfo.net.server.JuiceServers;

/**
 * @author author
 * 
 *         2013-4-18
 */
public class ServerBootstrap {
	public static Logger logger = LoggerFactory
			.getLogger(ServerBootstrap.class);
	/** 获取根目录 */
	public static String ROOT_DIR = System.getProperty("user.dir");

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws Exception {
		// 读取服务器配置
		Interpreter interpreter = new Interpreter();
		interpreter.source(ROOT_DIR + File.separator + "script/juice.bsh");
		Object _database = interpreter.get("database");
		if (_database != null && _database instanceof DataBaseProperties) {
			// DataBaseProperties database = (DataBaseProperties) _database;
			// setupDataBase(database);
		}
		//
		Object _bootstrap = interpreter.get("bootstrap");
		if (_bootstrap != null && _bootstrap instanceof BootstrapProperties) {
			BootstrapProperties bootstrap = (BootstrapProperties) _bootstrap;
			setupBootstrap(bootstrap);
		}
		//
		Object _remotes = interpreter.get("remotes");
		if (_remotes != null) {
			RemoteConfig[] remotes = (RemoteConfig[]) _remotes;
			setupRemoteServer(remotes);
		}
		
		Application app = new Application();
		app.init();
		findAppPID();
	}

	// 初始化服务器启动配置
	private static void setupBootstrap(BootstrapProperties bp) throws Exception {
		logger.info("Configuration Server bootstrap params.......");
		logger.info("Server protocol:\t" + bp.getProtocol());
		logger.info("Server port:\t" + bp.getPort());
		IJuiceServer server = JuiceServers.createWebServer(bp.getPort());
		server.setTransport(bp.getProtocol());
		server.start().get();
	}

	// 初始化配置远程服务器配置
	private static void setupRemoteServer(RemoteConfig[] configs)
			throws Exception {
		logger.info("Configuration Remote Server.......");
		for (RemoteConfig config : configs) {
			logger.info("Add Remote Server - Name:" + config.getName()
					+ ";Address:" + config.getAddress() + ";Port:"
					+ config.getPort());
			Container.addConfig(config);
		}
		JuiceRemoteManager.setupRemoteServer();
	}

	private static void findAppPID() throws Exception {
		String processName = java.lang.management.ManagementFactory
				.getRuntimeMXBean().getName();
		String pid = processName.split("@")[0];
		logger.info("Application PID:\t" + pid);
		FileWriter writer = new FileWriter(ROOT_DIR + File.separator
				+ "server.pid");
		writer.write(pid);
		writer.flush();
		writer.close();
	}
}
