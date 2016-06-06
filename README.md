# excelorm
excel的ORM实现

Usage

1,初始化excel文件存储空间
ExcelFactory.instance("D:/exceldata");

2,新建javabean，继承BaseBean
public class User extends BaseBean {
private String name;// 用户
}

3,设置字段对应excel中的列
@ColumnAnnotation("A")
	private String name;// 用户
	
4,实例化javabean，对数据进行增删改查操作

List list = new ArrayList();
			User user1 = new User("刘峻辉1","开发部","编程");
			User user2 = new User("刘峻辉2","开发部","编程");
			User user3 = new User("刘峻辉3","开发部","编程");
			User user4 = new User("刘峻辉4","开发部","编程");
			list.add(user1);
			list.add(user2);
			list.add(user3);
			list.add(user4);
			
			user1.saveAll(list);
