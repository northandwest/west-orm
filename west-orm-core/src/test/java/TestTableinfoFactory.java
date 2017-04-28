import java.util.Date;

import com.bucuoa.west.orm.core.clazzinfo.ITableInfo;
import com.bucuoa.west.orm.core.clazzinfo.SingleTableInfo;
import com.bucuoa.west.orm.core.clazzinfo.TableinfoFactory;

import entity.CategoryList;

public class TestTableinfoFactory {

	public static void main(String[] args) {
		CategoryList entity = new CategoryList();
		entity.setCategoryName("");
		entity.setCreateDate(new Date());
		
		ITableInfo shardInfo = new SingleTableInfo(entity);

		ITableInfo tableinfo = TableinfoFactory.create(shardInfo);
	}

}
