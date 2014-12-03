package org.wucl.kddcup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.wucl.util.DiscreteMap;

public class KddCup99 {
	public static final String LEVEL1 = "1";
	public static final String LEVEL2 = "2";
	public static final String LEVEL3 = "3";
	public static final String LEVEL4 = "4";
	public static final String LEVEL5 = "5";
	public static final String LEVEL6 = "6";
	public static final String LEVEL7 = "7";
	public static final String LEVEL8 = "8";
	// （1）duration. 连接持续时间，以秒为单位，连续类型。范围是 [0, 58329] 。
	private DiscreteMap duration = new DiscreteMap();
	// （2）protocol_type. 协议类型，离散类型，共有3种：TCP, UDP, ICMP。
	private DiscreteMap protocol_type = new DiscreteMap();
	// （3）service. 目标主机的网络服务类型，离散类型，共有70种
	private DiscreteMap service = new DiscreteMap();
	// （4）flag. 连接正常或错误的状态，离散类型，共11种。
	private DiscreteMap flag = new DiscreteMap();
	// （5）src_bytes. 从源主机到目标主机的数据的字节数，连续类型，范围是 [0, 1379963888]。
	private DiscreteMap src_bytes = new DiscreteMap();
	// （6）dst_bytes. 从目标主机到源主机的数据的字节数，连续类型，范围是 [0. 1309937401]。
	private DiscreteMap dst_bytes = new DiscreteMap();
	// （7）land. 若连接来自/送达同一个主机/端口则为1，否则为0，离散类型，0或1。
	private DiscreteMap land = new DiscreteMap();
	// （8）wrong_fragment. 错误分段的数量，连续类型，范围是 [0, 3]。
	private DiscreteMap wrong_fragment = new DiscreteMap();
	// （9）urgent. 加急包的个数，连续类型，范围是[0, 14]。
	private DiscreteMap urgent = new DiscreteMap();
	// （10）hot. 访问系统敏感文件和目录的次数，连续，范围是 [0, 101]
	private DiscreteMap hot = new DiscreteMap();
	// （11）num_failed_logins. 登录尝试失败的次数。连续，[0, 5]。
	private DiscreteMap num_failed_logins = new DiscreteMap();
	// （12）logged_in. 成功登录则为1，否则为0，离散，0或1。
	private DiscreteMap logged_in = new DiscreteMap();
	// （13）num_compromised. compromised条件（**）出现的次数，连续，[0, 7479]。
	private DiscreteMap num_compromised = new DiscreteMap();
	// （14）root_shell. 若获得root shell 则为1，否则为0，离散，0或1。
	private DiscreteMap root_shell = new DiscreteMap();
	// （15）su_attempted. 若出现”su root” 命令则为1，否则为0，离散，0或1。
	private DiscreteMap su_attempted = new DiscreteMap();
	// （16）num_root. root用户访问次数，连续，[0, 7468]。
	private DiscreteMap num_root = new DiscreteMap();
	// （17）num_file_creations. 文件创建操作的次数，连续，[0, 100]。
	private DiscreteMap num_file_creations = new DiscreteMap();
	// （18）num_shells. 使用shell命令的次数，连续，[0, 5]。
	private DiscreteMap num_shells = new DiscreteMap();
	// （19）num_access_files. 访问控制文件的次数，连续，[0, 9]。
	private DiscreteMap num_access_files = new DiscreteMap();
	// （20）num_outbound_cmds. 一个FTP会话中出站连接的次数，连续，0。
	private DiscreteMap num_outbound_cmds = new DiscreteMap();
	// （21）is_hot_login.登录是否属于“hot”列表（***），是为1，否则为0，离散，0或1。
	private DiscreteMap is_hot_login = new DiscreteMap();
	// （22）is_guest_login. 若是guest 登录则为1，否则为0，离散，0或1。
	private DiscreteMap is_guest_login = new DiscreteMap();
	// （23）count. 过去两秒内，与当前连接具有相同的目标主机的连接数，连续，[0, 511]。
	private DiscreteMap count = new DiscreteMap();
	// （24）srv_count. 过去两秒内，与当前连接具有相同服务的连接数，连续，[0, 511]。
	private DiscreteMap srv_count = new DiscreteMap();
	// （25）serror_rate. 过去两秒内，在与当前连接具有相同目标主机的连接中，出现“SYN” 错误的连接的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap serror_rate = new DiscreteMap();
	// （26）srv_serror_rate. 过去两秒内，在与当前连接具有相同服务的连接中，出现“SYN” 错误的连接的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap srv_serror_rate = new DiscreteMap();
	// （27）rerror_rate. 过去两秒内，在与当前连接具有相同目标主机的连接中，出现“REJ” 错误的连接的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap rerror_rate = new DiscreteMap();
	// （28）srv_rerror_rate. 过去两秒内，在与当前连接具有相同服务的连接中，出现“REJ” 错误的连接的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap srv_rerror_rate = new DiscreteMap();
	// （29）same_srv_rate. 过去两秒内，在与当前连接具有相同目标主机的连接中，与当前连接具有相同服务的连接的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap same_srv_rate = new DiscreteMap();
	// （30）diff_srv_rate. 过去两秒内，在与当前连接具有相同目标主机的连接中，与当前连接具有不同服务的连接的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap diff_srv_rate = new DiscreteMap();
	// （31）srv_diff_host_rate.
	// 过去两秒内，在与当前连接具有相同服务的连接中，与当前连接具有不同目标主机的连接的百分比，连续，[0.00, 1.00]。
	private DiscreteMap srv_diff_host_rate = new DiscreteMap();
	// （32）dst_host_count. 前100个连接中，与当前连接具有相同目标主机的连接数，连续，[0, 255]。
	private DiscreteMap dst_host_count = new DiscreteMap();
	// （33）dst_host_srv_count. 前100个连接中，与当前连接具有相同目标主机相同服务的连接数，连续，[0, 255]。
	private DiscreteMap dst_host_srv_count = new DiscreteMap();
	// （34）dst_host_same_srv_rate. 前100个连接中，与当前连接具有相同目标主机相同服务的连接所占的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap dst_host_same_srv_rate = new DiscreteMap();
	// （35）dst_host_diff_srv_rate. 前100个连接中，与当前连接具有相同目标主机不同服务的连接所占的百分比，连续，[0.00,
	// 1.00]。
	private DiscreteMap dst_host_diff_srv_rate = new DiscreteMap();
	// （36）dst_host_same_src_port_rate.
	// 前100个连接中，与当前连接具有相同目标主机相同源端口的连接所占的百分比，连续，[0.00, 1.00]。
	private DiscreteMap dst_host_same_src_port_rate = new DiscreteMap();
	// （37）dst_host_srv_diff_host_rate.
	// 前100个连接中，与当前连接具有相同目标主机相同服务的连接中，与当前连接具有不同源主机的连接所占的百分比，连续，[0.00, 1.00]。
	private DiscreteMap dst_host_srv_diff_host_rate = new DiscreteMap();
	// （38）dst_host_serror_rate.
	// 前100个连接中，与当前连接具有相同目标主机的连接中，出现SYN错误的连接所占的百分比，连续，[0.00, 1.00]。
	private DiscreteMap dst_host_serror_rate = new DiscreteMap();
	// （39）dst_host_srv_serror_rate.
	// 前100个连接中，与当前连接具有相同目标主机相同服务的连接中，出现SYN错误的连接所占的百分比，连续，[0.00, 1.00]。
	private DiscreteMap dst_host_srv_serror_rate = new DiscreteMap();
	// （40）dst_host_rerror_rate.
	// 前100个连接中，与当前连接具有相同目标主机的连接中，出现REJ错误的连接所占的百分比，连续，[0.00, 1.00]。
	private DiscreteMap dst_host_rerror_rate = new DiscreteMap();
	// （41）dst_host_srv_rerror_rate.
	// 前100个连接中，与当前连接具有相同目标主机相同服务的连接中，出现REJ错误的连接所占的百分比，连续，[0.00, 1.00]。
	private DiscreteMap dst_host_srv_rerror_rate = new DiscreteMap();

	public static void main(String[] args) throws IOException {
		KddCup99 kddcup = new KddCup99();
		InputStream is = new FileInputStream("E:/E/学习资料/数据集/kddcup99/corrected");
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr, 64 * 1024);
		String lineStr = null;
		int counts = 0;
		while ((lineStr = br.readLine()) != null) {
			String[] a = lineStr.split(",");
			if (a.length == 42 && !"normal.".equalsIgnoreCase(a[41])) {
				kddcup.duration.put(a[0]);
				kddcup.protocol_type.put(a[1]);
				kddcup.service.put(a[2]);
				kddcup.flag.put(a[3]);
				kddcup.src_bytes.put(src_bytesRender(a[4]));
				kddcup.dst_bytes.put(dst_bytesRender(a[5]));
				kddcup.land.put(a[6]);
				kddcup.wrong_fragment.put(a[7]);
				kddcup.urgent.put(a[8]);
				kddcup.hot.put(a[9]);
				kddcup.num_failed_logins.put(a[10]);
				kddcup.logged_in.put(a[11]);
				// kddcup.num_compromised.put(a[12]);
				kddcup.root_shell.put(a[13]);
				kddcup.su_attempted.put(a[14]);

				kddcup.num_shells.put(a[16]);
				counts++;
			}

		}
		System.out.println("异常记录条数：" + counts);
		br.close();
		isr.close();
		is.close();
	}

	public static String src_bytesRender(String value) {
		int intValue = Integer.parseInt(value);
		String result = null;
		if (intValue <= 100) {
			result = KddCup99.LEVEL1;
		} else if (intValue <= 500) {
			result =  KddCup99.LEVEL2;
		} else if (intValue <= 1000) {
			result =  KddCup99.LEVEL3;
		} else if (intValue <= 3000) {
			result =  KddCup99.LEVEL4;
		} else {
			result =  KddCup99.LEVEL5;
		}
		return result;
	}

	public static String dst_bytesRender(String value) {
		int intValue = Integer.parseInt(value);
		String result = null;
		if (intValue <= 100) {
			result =  KddCup99.LEVEL1;
		} else if (intValue <= 500) {
			result =  KddCup99.LEVEL2;
		} else if (intValue <= 1000) {
			result =  KddCup99.LEVEL3;
		} else if (intValue <= 3000) {
			result =  KddCup99.LEVEL4;
		} else {
			result =  KddCup99.LEVEL5;
		}
		return result;
	}

}
