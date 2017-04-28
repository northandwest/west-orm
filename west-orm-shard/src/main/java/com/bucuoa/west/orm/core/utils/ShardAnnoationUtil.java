package com.bucuoa.west.orm.core.utils;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.orm.app.extend.shard.BaseShardEntity;
import com.bucuoa.west.orm.core.base.AnnoationStopWord;
import com.bucuoa.west.orm.core.uitls.AnnoationUtil;
import com.bucuoa.west.orm.shard.ShardInfo;
import com.bucuoa.west.orm.shard.annonation.ShardKey;
import com.bucuoa.west.orm.shard.annonation.ShardTable;

public class ShardAnnoationUtil  extends AnnoationUtil {
	private final static Logger logger = LoggerFactory.getLogger(ShardAnnoationUtil.class);


	public static <T> ShardInfo getShardTableInfo(T t) {
		Class<?> clazz = t.getClass();
		ShardInfo info = new ShardInfo();
		
		if(t instanceof BaseShardEntity)
		{
			
			int directShardValue = ((BaseShardEntity) t).getDirectShardValue();
			int directTable = ((BaseShardEntity) t).getDirectTable();
			
			info.setDirectNums(directTable);
			info.setDirectShards(directShardValue);
		}
		
		if (clazz.isAnnotationPresent(ShardTable.class)) {

			ShardTable stAnnotation = (ShardTable) clazz.getAnnotation(ShardTable.class);
			String tablename = AnnoationUtil.getTablename(clazz);
			
			info.setPolicy(stAnnotation.policy());
			info.setNums(stAnnotation.nums());
			info.setTable(tablename);
			info.setShards(stAnnotation.shards());
			
			String shardkey = getShardKey(clazz);
			info.setShardKey(shardkey);
		
			Object shardValue = getObjectFieldValue(t,info.getShardKey());
			
			info.setShardValue(shardValue);
		}

		return info;
	}

	public static String getShardKey(Class<?> clazz) {
		String shardkey = null;
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String key = field.getName().trim();

				List<String> stopWords = AnnoationStopWord.getStopWords();
				if (stopWords.contains(key.toLowerCase())) {
					continue;
				}
				
				boolean tran = field.isAnnotationPresent(Transient.class);
				if (tran) {
					continue;
				}

				if (field.isAnnotationPresent(ShardKey.class)) {
					shardkey = key;
					
					break;
				}
			}

		} catch (Exception e) {
			logger.error("getId error", e);
		}
		
		return shardkey;
	}



}
