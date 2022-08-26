package com.kubian.mode.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.ColumnContent;

@Transactional
@Repository
public interface ColumnContentDao extends CrudRepository<ColumnContent, Integer> {
	public List<ColumnContent> findAll();

	// 获取数据总量
	@Query("select count(1) from ColumnContent")
	public int getColuConSize();

	public Page<ColumnContent> findAll(Pageable pageable);

	@Query(nativeQuery = true, value = "select * from column_content  where create_date >= date_sub(now(), INTERVAL 3 MONTH) and (state = 1 or state = 2 or state =6) limit :start,:limit")
	public List<ColumnContent> findAll1(@Param("start") Integer start , @Param("limit") Integer limit);
	@Query(nativeQuery = true, value = "select count(1) from column_content  where create_date >= date_sub(now(), INTERVAL 3 MONTH) and (state = 1 or state = 2 or state =6)")
	public int findAll1();

	// 根据state查询所有

	@Query("from ColumnContent where state = :tag2 or state=:tag1 or is_push=1 order by top desc ,create_date DESC")
	public List<ColumnContent> getConByState(@Param("tag2") String tag2, @Param("tag1") String tag1, Pageable pageable);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where state = :tag2 or state=:tag1 or is_push=1 order by top desc ,create_date DESC")
	public List<ColumnContent> getConByState1(@Param("tag2") String tag2, @Param("tag1") String tag1,
			Pageable pageable);

	@Query("select count(1) from ColumnContent where state = :tag2 or state=:tag1 or is_push=1 order by top desc ,create_date DESC")
	public int getConByState(@Param("tag2") String tag2, @Param("tag1") String tag1);

	// 根据colId分页查询头条的所有信息
	@Query("from ColumnContent where (state = 1 or state=:tag ) and colId =:colId or is_push=:colId order by top desc ,create_date DESC")
	public List<ColumnContent> findByColId(@Param("colId") Long colId, @Param("tag") String tag, Pageable pageable);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where (state = 1 or state=:tag ) and colId =:colId or is_push=:colId order by top desc ,create_date DESC")
	public List<ColumnContent> findByColId1(@Param("colId") Long colId, @Param("tag") String tag, Pageable pageable);

	// 根据栏目colId查询所有
	@Query("select count(1) from ColumnContent where (state = 1 or state=:tag ) and colId =:colId or is_push=:colId order by create_date DESC")
	public int findByColId(@Param("colId") Long colId, @Param("tag") String tag);

	// 根据用户appUserId 或 标题conTitle 模糊查询
	@Query(nativeQuery = true, value = " select id,con_title,con_img from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0))  and (state =1 or state=:tag) or is_push=1 order by top desc,create_date DESC limit :page,:size")
	public List<ColumnContent> getColumnContentByAppUserIdOrConTitle(@Param("search_str") String search_str,
			@Param("page") Integer page, @Param("size") Integer size, @Param("tag") String tag);

	// 获取模糊查询的总数
	@Query(nativeQuery = true, value = "select count(1) from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0))  and (state = 1 or state=:tag) or is_push=1 order by create_date DESC")
	public int getColumnContentAll(@Param("search_str") String search_str, @Param("tag") String tag);

	// 模糊查询视频栏目下面内容
	@Query(nativeQuery = true, value = " select * from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0))  and (state =6 or state=:tag)  order by top desc,create_date DESC limit :page,:size")
	public List<ColumnContent> getCon(@Param("search_str") String search_str, @Param("page") Integer page,
			@Param("size") Integer size, @Param("tag") String tag);

	@Query(nativeQuery = true, value = " select count(1) from column_content where app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0)  and (state =6 or state=:tag)  order by top desc,create_date DESC")
	public int getCon(@Param("search_str") String search_str, @Param("tag") String tag);

	// 根据id查询对应信息
	public ColumnContent findById(Long id);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where id=:id")
	public ColumnContent findById1(@Param("id") Long id);

	@Query(value = "select new ColumnContent(c.id) from ColumnContent c where id=:id")
	public ColumnContent getConById(@Param("id") Long id);

	// 根据colid获取头条顶部展示的内容
	@Query("from ColumnContent where colId = :colId and hot = :hot and state = :state order by hotDate desc")
	public List<ColumnContent> findByColIdAndHot(@Param("colId") Long colId, @Param("hot") String hot,
			@Param("state") String state);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where colId = :colId and hot = :hot and state = :state order by hotDate desc")
	public List<ColumnContent> findByColIdAndHot1(@Param("colId") Long colId, @Param("hot") String hot,
			@Param("state") String state);

	// 分页获取获取当前登录用户发布的内容列表
	@Query("from ColumnContent where appUserId =:appUserId and colId =:colId and (state = 6 or state=:state) order by create_date desc")
	public List<ColumnContent> findByAppUserIdAndColId(@Param("appUserId") Long appUserId, @Param("colId") Long colId,
			@Param("state") String state, Pageable pageable);

	@Query("from ColumnContent where appUserId =:appUserId and (state = 6 or state=:state) order by create_date desc")
	public List<ColumnContent> findByAppUserIdAndColId(@Param("appUserId") Long appUserId, @Param("state") String state,
			Pageable pageable);
	
	@Query("from ColumnContent where appUserId =:appUserId and colId =:colId and state=:state order by create_date desc")
	public List<ColumnContent> pcFindByAppUserIdAndColId(@Param("appUserId") Long appUserId, @Param("colId") Long colId,
			@Param("state") String state, Pageable pageable);

	@Query("from ColumnContent where appUserId =:appUserId and state=:state order by create_date desc")
	public List<ColumnContent> pcFindByAppUserIdAndColId(@Param("appUserId") Long appUserId, @Param("state") String state,
			Pageable pageable);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where appUserId =:appUserId and (state=2 or state=6) order by create_date desc")
	public List<ColumnContent> findByAppUserId(@Param("appUserId") Long appUserId, Pageable pageable);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where appUserId =:appUserId and (state=2 or state=6) order by create_date desc")
	public List<ColumnContent> findByAppUserId(@Param("appUserId") Long appUserId);

	@Query("select count(1) from ColumnContent where appUserId =:appUserId and colId =:colId and state=:state order by create_date desc")
	public int findByAppUserIdAndColId(@Param("appUserId") Long appUserId, @Param("colId") Long colId,
			@Param("state") String state);

	@Query("select count(1) from ColumnContent where appUserId =:appUserId  and state=:state order by create_date desc")
	public int findByAppUserIdAndColId(@Param("appUserId") Long appUserId, @Param("state") String state);

	// 分页获取发现栏目的内容
	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where (state =2 or state = :tag) and colId =:colId order by top desc , create_date desc")
	public List<ColumnContent> getFindManagers(@Param("colId") Long colId, @Param("tag") String tag, Pageable pageable);

	@Query("select count(1) from ColumnContent where (state =2 or state = :tag) and colId =:colId order by top desc,create_date DESC")
	public int getFindManagers(@Param("colId") Long colId, @Param("tag") String tag);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where state =:tag2 or state = :tag1  order by top desc , create_date desc")
	public List<ColumnContent> getFindManager(@Param("tag2") String tag2, @Param("tag1") String tag1,
			Pageable pageable);

	@Query("select count(1) from ColumnContent where state =:tag2 or state = :tag1  order by top desc , create_date desc")
	public int getFindManager(@Param("tag2") String tag2, @Param("tag1") String tag1);

	// 根据用户appUserId 或 标题conTitle 模糊查询
	@Query(nativeQuery = true, value = "select * from column_content where app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0)  and (state =2 or state = :tag) order by top desc,create_date DESC limit :page,:size")
	public List<ColumnContent> getFindColumnContent(@Param("search_str") String search_str, @Param("page") Integer page,
			@Param("size") Integer size, @Param("tag") String tag);

	// 获取模糊查询的总数
	@Query(nativeQuery = true, value = "select count(1) from column_content where app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0) and (state =2 or state = :tag) order by create_date DESC")
	public int getFindColumnContentAll(@Param("search_str") String search_str, @Param("tag") String tag);

	// 根据colid获取发现栏目顶部展示的内容
	@Query("from ColumnContent where colId = :colId and hot = :hot and state =2  order by hotDate desc")
	public List<ColumnContent> getFindTopContent(@Param("colId") Long colId, @Param("hot") String hot);

	// 分页获取热门栏目的内容
	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where (state =6 or  state = :tag ) and colId = :colId  order by hTop desc,createDate DESC")
	public List<ColumnContent> getHotContent(@Param("colId") Long colId, @Param("tag") String tag, Pageable pageable);

	@Query("select count(1) from ColumnContent where (state =6 or state = :tag) and colId = :colId   order by createDate DESC")
	public int getHotContent(@Param("colId") Long colId, @Param("tag") String tag);

	// 根据colid获取热门栏目顶部展示的内容
	@Query("from ColumnContent where colId = :colId and hot = :hot and state =6  order by hotDate desc")
	public List<ColumnContent> getTopHotContent(@Param("colId") Long colId, @Param("hot") String hot);

	// 根据用户id获取收藏的内容的信息
	@Query(nativeQuery = true, value = "select * from column_content where id in  (select column_content_id from collect where app_user_id = :appUserId) order by create_date DESC limit :page,:size")
	public List<ColumnContent> getColContentbyCollect(@Param("appUserId") Long appUserId, @Param("page") Integer page,
			@Param("size") Integer size);

	// 根据appUserId 获取内容
	// @Query("from ColumnContent where appUserId = :appUserId order by id DESC
	// limit :pagr,:size")
	// public List<ColumnContent> findByAppUserIdAndColId(Long appUserId, Long
	// colId, Pageable pageable);

	// 根据userId 获取内容
	public List<ColumnContent> findByUserId(Long userId, Pageable pageable);

	// 根据用户id获取关注的app用户的发布的内容
	@Query(nativeQuery = true, value = "select * from column_content where app_user_id in (select be_followed_id from attention where follower_id = :appUserId and judge=:judge) and (state=2 or state=6) order by create_date DESC limit :page,:size")
	public List<ColumnContent> getConByAttention(@Param("appUserId") Long appUserId, @Param("judge") Integer judge,
			@Param("page") Integer page, @Param("size") Integer size);

	@Query(nativeQuery = true, value = "select * from column_content where app_user_id in (select be_followed_id from attention where follower_id = :appUserId and judge=:judge) and (state=2 or state=6) order by create_date DESC limit :page,:size")
	public List<ColumnContent> getConByAttention1(@Param("appUserId") Long appUserId, @Param("judge") Integer judge,
			@Param("page") Integer page, @Param("size") Integer size);

	@Query(nativeQuery = true, value = "select count(1) from column_content where app_user_id in (select be_followed_id from attention where follower_id = :appUserId and judge=:judge) and (state=2 or state=6) order by create_date DESC")
	public Integer getConByAttentions(@Param("appUserId") Long appUserId, @Param("judge") Integer judge);

	// 根据用户id获取关注的后台用户的发布的内容
	@Query(nativeQuery = true, value = "select * from column_content where user_id in (select be_followed_id from attention where follower_id = :appUserId and judge=:judge) and (state=1 or state=6) order by create_date DESC limit :page,:size")
	public List<ColumnContent> getConByAttention2(@Param("appUserId") Long appUserId, @Param("judge") Integer judge,
			@Param("page") Integer page, @Param("size") Integer size);

	@Query(nativeQuery = true, value = "select * from column_content where user_id in (select be_followed_id from attention where follower_id = :appUserId and judge=:judge) and (state=1 or state=6) order by create_date DESC limit :page,:size")
	public List<ColumnContent> getConByAttention3(@Param("appUserId") Long appUserId, @Param("judge") Integer judge,
			@Param("page") Integer page, @Param("size") Integer size);

	@Query(nativeQuery = true, value = "select count(1) from column_content where user_id in (select be_followed_id from attention where follower_id = :appUserId and judge=:judge) and (state=1 or state=6) order by create_date DESC")
	public Integer getConByAttentions2(@Param("appUserId") Long appUserId, @Param("judge") Integer judge);

	// 根据用户appUserId 或 标题conTitle 模糊查询
	@Query(nativeQuery = true, value = "select * from column_content where user_id in (select u.id from user u  where nick_name like :search_str) or app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str  and (state =2 or state = 1 or state = 6 or state = :tag1 or state = :tag2 or state = :tag3)  order by create_date DESC limit :page,:size")
	public List<ColumnContent> fuzzySearchCon(@Param("search_str") String search_str, @Param("page") Integer page,
			@Param("size") Integer size, @Param("tag1") String tag1, @Param("tag2") String tag2,
			@Param("tag3") String tag3);

	@Query(nativeQuery = true, value = "select count(1) from column_content where user_id in (select u.id from user u  where nick_name like :search_str) or app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str  or con_remark like :search_str  and (state =2 or state = 1 or state = 6 or state = :tag1 or state = :tag2 or state = :tag3)  order by create_date DESC")
	public int fuzzySearchCon(@Param("search_str") String search_str, @Param("tag1") String tag1,
			@Param("tag2") String tag2, @Param("tag3") String tag3);

	// // 根据用户id和状态分页获取数据
	@Query(value = "from ColumnContent where appUserId=:appUserId and (state=:state2 or state=:state) order by createDate desc")
	public List<ColumnContent> findByAppUserIdAndState(@Param("appUserId") Long appUserId,
			@Param("state2") String state2, @Param("state") String state, Pageable pageable);

	@Query(value = "select count(1) from ColumnContent where appUserId=:appUserId and (state=:state2 or state=:state)")
	public int findByAppUserIdAndState(@Param("appUserId") Long appUserId, @Param("state2") String state2,
			@Param("state") String state);

	// 获取置顶的内容数量
	@Query(value = "from ColumnContent where top is not null and colId = :colId and state =:state order by top desc")
	public List<ColumnContent> findColIdAndState(@Param("colId") Long colId, @Param("state") String state);

	// 获取热门的置顶的内容数量
	@Query(value = "from ColumnContent where hTop is not null and colId = :colId and (state =:state1 or state =:state2) order by hTop desc")
	public List<ColumnContent> findByTopAndColId(@Param("colId") Long colId, @Param("state1") String state1,
			@Param("state2") String state2);

	// 根据colId查询头条的置顶信息
	@Query("from ColumnContent where (state = 1 or state=:tag) and colId =:colId  and top is not null order by top DESC")
	public List<ColumnContent> getHeadConByColId(@Param("colId") Long colId, @Param("tag") String tag);

	// 根据colId查询发现的置顶信息
	@Query("from ColumnContent where (state = 2 or state=:tag) and colId =:colId  and top is not null order by top DESC")
	public List<ColumnContent> getFindConByColId(@Param("colId") Long colId, @Param("tag") String tag);

	// 根据colId查询热门的置顶信息
	@Query("from ColumnContent where state =6 and  colId =:colId and hTop is not null order by hTop DESC")
	public List<ColumnContent> getHotConByColId(@Param("colId") Long colId);

	// pc端登录的app用户根据标题模糊搜索自己发布的内容
	@Query(nativeQuery = true, value = "select * from column_content where app_user_id =:appUserId and state =:state and con_title like :con order by create_date DESC limit :page,:size  ")
	public List<ColumnContent> getpcDimSearch(@Param("appUserId") Long appUserId, @Param("con") String con,
			@Param("page") Integer page, @Param("size") Integer size, @Param("state") String state);

	@Query(nativeQuery = true, value = "select count(1) from column_content where app_user_id =:appUserId and state =:state and con_title like :con order by create_date DESC")
	public int getpcDimSearch(@Param("appUserId") Long appUserId, @Param("con") String con,
			@Param("state") String state);

	@Query(nativeQuery = true, value = "select * from column_content where con_remark like '%<%' ")
	public List<ColumnContent> setConTitleAndRemark();

	@Query(nativeQuery = true, value = "select * from column_content where con_title like '%<%' ")
	public List<ColumnContent> setConTitleAndRemark2();

	// 根据用户appUserId 或 标题conTitle 模糊查询
	@Query(nativeQuery = true, value = " select * from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str)  and state =1 and (bih=1 or bih=:tag) order by top desc,create_date DESC limit :page,:size")
	public List<ColumnContent> getColumnContentByAppUserIdOrConTitle(@Param("search_str") String search_str,
			@Param("page") Integer page, @Param("size") Integer size, @Param("tag") Integer tag);

	// 获取模糊查询的总数
	@Query(nativeQuery = true, value = "select count(1) from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str)   and state = 1 and (bih=1 or bih=:tag) order by create_date DESC")
	public int getColumnContentAll(@Param("search_str") String search_str, @Param("tag") Integer tag);

	// 根据colId分页查询头条的所有信息
	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where state = 1 and (bih=1 or bih=:tag) and colId =:colId or is_push=:colId order by top desc ,create_date DESC")
	public List<ColumnContent> findByColId(@Param("colId") Long colId, @Param("tag") Integer tag, Pageable pageable);

	// 根据栏目colId查询所有
	@Query("select count(1) from ColumnContent where state = 1 and (bih=1 or bih=:tag) and colId =:colId or is_push=:colId order by create_date DESC")
	public int findByColId(@Param("colId") Long colId, @Param("tag") Integer tag);

	// 根据用户appUserId 或 标题conTitle 模糊查询
	@Query(nativeQuery = true, value = "select * from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str) and (is_public is null or is_public!=0)  and state =2 and (bih =1 or bih = :tag) order by top desc,create_date DESC limit :page,:size")
	public List<ColumnContent> getFindColumnContent(@Param("search_str") String search_str, @Param("page") Integer page,
			@Param("size") Integer size, @Param("tag") Integer tag);

	// 获取模糊查询的总数
	@Query(nativeQuery = true, value = "select count(1) from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str) and (is_public is null or is_public!=0) and state =2 and (bih =1 or bih = :tag) order by create_date DESC")
	public int getFindColumnContentAll(@Param("search_str") String search_str, @Param("tag") Integer tag);

	// 分页获取发现栏目的内容
	@Query("select  new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where state =2 and (bih =1 or bih = :tag) and colId =:colId order by top desc , create_date desc")
	public List<ColumnContent> getFindManagers(@Param("colId") Long colId, @Param("tag") Integer tag,
			Pageable pageable);

	@Query("select count(1) from ColumnContent where state =2 and (bih =1 or bih = :tag) and colId =:colId order by top desc,create_date DESC")
	public int getFindManagers(@Param("colId") Long colId, @Param("tag") Integer tag);

	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where state = 2 and (bih =1 or bih = :tag)  order by top desc , create_date desc")
	public List<ColumnContent> getFindManager(@Param("tag") Integer tag, Pageable pageable);

	@Query("select count(1) from ColumnContent where state =2 and (bih =1 or bih = :tag)  order by top desc , create_date desc")
	public int getFindManager(@Param("tag") Integer tag);

	// 分页获取热门栏目的内容
	@Query("select new ColumnContent(c.id,c.conTitle,c.conRemark,c.conImg,c.conImg1,c.conLivePath,c.conVideoPath,c.createDate,c.praiseCount,c.playCount,c.exaCount,c.appUserId,c.userId,c.state,c.colId,c.hot) from ColumnContent c where state =6  and (bih =1 or bih = :tag) and colId = :colId  order by hTop desc,createDate DESC")
	public List<ColumnContent> getHotContent1(@Param("colId") Long colId, @Param("tag") Integer tag, Pageable pageable);

	@Query("select count(1) from ColumnContent where state =6  and (bih =1 or bih = :tag)  and colId = :colId   order by createDate DESC")
	public int getHotContent1(@Param("colId") Long colId, @Param("tag") Integer tag);

	// 模糊查询视频栏目下面内容
	@Query(nativeQuery = true, value = " select * from column_content where (app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0))  and state =6 and (bih =1 or bih = :tag)  order by top desc,create_date DESC limit :page,:size")
	public List<ColumnContent> getCon1(@Param("search_str") String search_str, @Param("page") Integer page,
			@Param("size") Integer size, @Param("tag") Integer tag);

	@Query(nativeQuery = true, value = " select count(1) from column_content where app_user_id in (select a.id from app_user a  where user_name like :search_str) or con_title like :search_str or con_remark like :search_str and (is_public is null or is_public!=0)  and state =6 and (bih =1 or bih = :tag)  order by top desc,create_date DESC")
	public int getCon1(@Param("search_str") String search_str, @Param("tag") Integer tag);

}
