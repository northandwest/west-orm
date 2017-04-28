package entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bucuoa.west.orm.shard.annonation.ShardKey;
import com.bucuoa.west.orm.shard.annonation.ShardTable;

@Table(name = "category_list")
@ShardTable
public class CategoryList {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // id

	@Column(name = "first_category")
	private Long firstCategory; // 一级分类
	
	@Column(name = "second_category")
	private Long secondCategory; // 二级分类
	@ShardKey
	@Column(name = "third_category")
	private Long thirdCategory; // 三级分类

	@Column(name = "category_name")
	private String categoryName; // 分类名称

	@Column(name = "statux")
	private Integer statux; // 1开通 0未开通

	@Column(name = "create_date")
	private Date createDate; // 时间

	@Column(name = "modify_date")
	private Date modifyDate; // 修改时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(Long firstCategory) {
		this.firstCategory = firstCategory;
	}

	public Long getSecondCategory() {
		return secondCategory;
	}

	public void setSecondCategory(Long secondCategory) {
		this.secondCategory = secondCategory;
	}

	public Long getThirdCategory() {
		return thirdCategory;
	}

	public void setThirdCategory(Long thirdCategory) {
		this.thirdCategory = thirdCategory;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getStatux() {
		return statux;
	}

	public void setStatux(Integer statux) {
		this.statux = statux;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
