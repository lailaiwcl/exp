var ioc = {
	dataSource : {
		type : "org.nutz.dao.impl.SimpleDataSource",
		fields : {
			jdbcUrl : 'jdbc:mysql://127.0.0.1:3306/stud?useUnicode=true&characterEncoding=utf-8',
			username : 'root',
			password : 'root'
		}
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [{refer:"dataSource"}]
	}
}
